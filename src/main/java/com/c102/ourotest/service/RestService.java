package com.c102.ourotest.service;

import org.springframework.stereotype.Service;

@Service
public class RestService {

    public String getTestName(String id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Test User";
    }

    public String getTestEmail(String id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test@example.com";
    }
}
