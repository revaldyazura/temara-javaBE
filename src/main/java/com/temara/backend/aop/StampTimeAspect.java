package com.temara.backend.aop;

import java.sql.Timestamp;
import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.temara.backend.base.BaseService;

@Aspect
@Component
public class StampTimeAspect extends BaseService {

  public void stampAudit(Object... os) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (Object object : os) {
      final BeanWrapper obj = new BeanWrapperImpl(object);
      try {
        Object createdBy = obj.getPropertyValue("createdBy");
        if (createdBy == null) {
          try {
            obj.setPropertyValue("createdDate", timestamp);
          } catch (Exception e) {

          }
          try {
            obj.setPropertyValue("createdBy",
                getCurrentUserId() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000")
                    : getCurrentUserId());
          } catch (Exception e) {

          }
        }
        try {
          obj.setPropertyValue("lastUpdatedDate", timestamp);
        } catch (Exception e) {

        }
        try {
          obj.setPropertyValue("lastUpdatedBy",
              getCurrentUserId() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000")
                  : getCurrentUserId());
        } catch (Exception e) {
          // TODO: handle exception
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
  }

  @Before("execution(* com.temara.backend.base.BaseRepository.save(*)) && args(parameter)")
  public void beforeSaveWithStampCreateAnnotation(Object parameter) {
    stampAudit(parameter);
  }

  @Before("execution(* com.temara.backend.base.BaseRepository.saveAll(*)) && args(parameter)")
  public void beforeSaveAllWithStampCreateAnnotation(Iterable<Object> parameters) {
    for (Object param : parameters) {
      stampAudit(param);
    }
  }

}
