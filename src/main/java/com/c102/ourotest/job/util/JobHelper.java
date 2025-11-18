package com.c102.ourotest.job.util;

import org.springframework.stereotype.Component;

@Component
public class JobHelper {
    public void helperMethod(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
