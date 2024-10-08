package com.temara.backend.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpLogger {

  private static final Logger logger = LoggerFactory.getLogger(HttpLogger.class);

  public void trace(HttpServletRequest request, Object body) {
    if (logger.isTraceEnabled() && request.getAttribute("traceid") == null) {

      String ipAddress = request.getHeader("x-forwarded-for: ");
      if (ipAddress == null) {
        ipAddress = request.getRemoteAddr();
      }

      long startTime = Instant.now().toEpochMilli();
      String traceId = UUID.randomUUID().toString();

      request.setAttribute("startTime", startTime);
      request.setAttribute("traceId", traceId);

      logger.trace("[REQUEST-LOG] traceId={}, ipAddress=\"{}\", request={{}}", traceId, ipAddress,
          requestToString(request, body));
    }
  }

  public void trace(HttpServletRequest request, HttpServletResponse response, Object body) {
    if (logger.isTraceEnabled()) {
      trace(request, null);

      String ipAddress = request.getHeader("x-forwarded-for: ");
      if (ipAddress == null) {
        ipAddress = request.getRemoteAddr();
      }

      long startTime = request.getAttribute("startTime") != null ? (long) request.getAttribute("startTime")
          : Instant.now().toEpochMilli();

      String traceId = (String) request.getAttribute("traceId");

      logger.trace("[RESPONSE-LOG] traceId={}, ipAddress=\"{}\", timeTaken={}, response={{}}", traceId, ipAddress,
          (Instant.now().toEpochMilli() - startTime), responseToString(request, response, body));
    }
  }

  private String requestToString(HttpServletRequest request, Object body) {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("Method = [" + request.getMethod() + "], ");
    stringBuilder.append("URL Path = [" + request.getRequestURL() + "], ");

    String headers = Collections.list(request.getHeaderNames()).stream()
        .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
        .collect(Collectors.joining(", "));

    if (headers.isEmpty()) {
      stringBuilder.append("headers: NONE, ");
    } else {
      stringBuilder.append("headers: [" + headers + "], ");
    }

    String parameters = Collections.list(request.getParameterNames()).stream()
        .map(p -> p + " : " + Arrays.asList(request.getParameterValues(p))).collect(Collectors.joining(" , "));

    if (parameters.isEmpty()) {
      stringBuilder.append("parameters: NONE");
    } else {
      stringBuilder.append("parameters: [" + parameters + "], ");
    }

    if (!Objects.isNull(body)) {
      ObjectMapper mapper = new ObjectMapper();

      try {
        stringBuilder.append("content: [" + mapper.writeValueAsString(body) + "].");
      } catch (JsonProcessingException e) {
        stringBuilder.append("content: [Unreadable]");
      }
    }
    return stringBuilder.toString();
  }

  private String responseToString(HttpServletRequest request, HttpServletResponse response, Object body) {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("Status = [" + response.getStatus() + "], ");
    String headers = response.getHeaderNames().stream()
        .map(headerName -> headerName + " : " + response.getHeaders(headerName)).collect(Collectors.joining(", "));

    if (headers.isEmpty()) {
      stringBuilder.append("headers: NONE, ");
    } else {
      stringBuilder.append("headers: " + headers + ", ");
    }

    if (!Objects.isNull(body)) {
      ObjectMapper mapper = new ObjectMapper();

      try {
        stringBuilder.append("content: [" + mapper.writeValueAsString(body) + "].");
      } catch (JsonProcessingException e) {
        stringBuilder.append("content: [Unreadable]");
      }
    }

    return stringBuilder.toString();
  }
}
