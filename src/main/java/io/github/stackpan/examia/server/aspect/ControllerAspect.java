package io.github.stackpan.examia.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Aspect
@Component
public class ControllerAspect {

    @Around("execution(* io.github.stackpan.examia.server.http.controller.*Controller.*(.., @io.github.stackpan.examia.server.annotation.ValidUUID (*), ..))")
    public Object validateUUID(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var args = proceedingJoinPoint.getArgs();
        for (var arg : args) {
            if (arg instanceof String resourceId) {
                try {
                    UUID.fromString(resourceId);
                } catch (IllegalArgumentException exception) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

}
