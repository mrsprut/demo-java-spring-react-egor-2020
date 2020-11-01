package org.tyaa.demo.spring.springreactmysqlmongo.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Configuration;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.utils.ErrorsGetter;

@Aspect
@Configuration
public class ErrorHandler {

    @Around("execution(* org.tyaa.demo.spring.springreactmysqlmongo.repositories.*.*(..))")
    public Object onRepositoryException(ProceedingJoinPoint pjp) throws Exception {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (Exception ex) {
            if (ErrorsGetter.getException(ex).contains("ConstraintViolationException")){
                throw new ConstraintViolationException("", null, "");
            }
            throw ex;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }

    // @Around("call(* org.tyaa.demo.springboot.simplespa.service.*.*(..))")
    @Around("execution(* org.tyaa.demo.spring.springreactmysqlmongo.services.*.*(..))")
    public Object onServiceException(ProceedingJoinPoint pjp) {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (ConstraintViolationException ex) {
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("This name is already taken")
                            .build();
        } catch (Exception ex) {
            System.err.println("Data Source Error!");
            ex.printStackTrace();
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("Unknown Data Source Error")
                            .build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }
}
