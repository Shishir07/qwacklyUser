package com.qwackly.user.util;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class OrderIdgenerator {

    private LocalTime time;

    public String getUniqueOrderId(){
        time= LocalTime.now();
        return UUID.nameUUIDFromBytes(time.toString().getBytes()).toString();
    }
}
