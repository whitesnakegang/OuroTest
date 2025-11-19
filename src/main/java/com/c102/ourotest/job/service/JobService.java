package com.c102.ourotest.job.service;

import com.c102.ourotest.job.util.JobHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobHelper helper;
    
    public void service(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        helper.helperMethod();
    }
}
