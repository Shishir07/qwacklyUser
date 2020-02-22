package com.qwackly.user.aspects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerWrapper {
    //private static Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static String append() {
        StringBuffer params = new StringBuffer();

        /*String name = "Abhinav";
        params.append(" Name: "+name);

        String requestId = "myReqId";
        params.append(" Qwackly-RequestId: "+requestId);*/

        return params.toString();
    }

    public static void info(Object message) {
        logger.info(message+append());
        logger.debug(message+append());
    }

    public static void error(Object message, Throwable t) {
        logger.error(message+append(), t);
        logger.debug(message+append(), t);
    }

    public static void error(Object message) {
        //errorLogger.error(message+append());
        logger.error(message+append());
        logger.debug(message+append());
    }
}
