package com.qwackly.user.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class LoggingAspect
{

    @Autowired
    private LoggerWrapper logger;

    @Pointcut("execution(* com.qwackly.user.*.*.*(..) )")
    public void log()
    {

    }

    @Before("log()")
    public void beforeLog(JoinPoint joinPoint)
    {
        logger.info(("Entering " + joinPoint.getSignature()) +" with parameters "+getParamters(joinPoint));
    }

    private String getParamters(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = getParameterNames(method);

        StringBuffer params = new StringBuffer(getParams(joinPoint.getArgs(), parameterNames));

        return params.toString();
    }

    @After("log()")
    public void afterLog(JoinPoint joinPoint)
    {
        logger.info(("Exiting " + joinPoint.getSignature()) +" with parameters "+getParamters(joinPoint));
    }

    @AfterThrowing(pointcut = "(execution(* com.qwackly.user.*.*.*(..)))", throwing = "t")
    public void afterThrowingLog(JoinPoint joinPoint, Throwable t) throws Throwable
    {
        //t.printStackTrace();
        logger.error("Exception at : " + joinPoint.getSignature() +" method parameters "+getParamters(joinPoint), t);
    }

    private String getParams(final Object[] params, final String[] parameterNames) {
        StringBuilder paramsLog = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                paramsLog.append(" , ");
            }
            paramsLog.append(new ObjectStringifier().toString(params[i], parameterNames[i]));
        }

        return paramsLog.toString();
    }

    private String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        List<String> paramNames = new ArrayList<String>();
        for (Parameter param : parameters) {
            paramNames.add(param.getName());
        }

        return paramNames.toArray(new String[0]);
    }

}
