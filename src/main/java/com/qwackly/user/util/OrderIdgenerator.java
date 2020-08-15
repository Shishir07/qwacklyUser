package com.qwackly.user.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class OrderIdgenerator {

    private LocalTime time;
    private final int SHORT_ID_LENGTH = 20;
    private static final String CHARS = "0123456789";

    public String getUniqueOrderId(){
        time= LocalTime.now();
        String shortUniqueId = RandomStringUtils.random(SHORT_ID_LENGTH, CHARS);
        return shortUniqueId;
        //return UUID.nameUUIDFromBytes(time.toString().getBytes()).toString();
    }
}
