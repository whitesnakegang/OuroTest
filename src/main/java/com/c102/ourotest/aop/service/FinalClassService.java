package com.c102.ourotest.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Final 클래스 테스트
 * CGLIB는 클래스를 상속하여 프록시를 생성하는데, final 클래스는 상속이 불가능함
 * 따라서 Spring AOP는 final 클래스에 대해 프록시를 생성할 수 없음
 */
@Service
@ConditionalOnProperty(name = "ouroboros.method-tracing.mode", havingValue = "ASPECTJ", matchIfMissing = false)
public final class FinalClassService {

    private static final Logger logger = LoggerFactory.getLogger(FinalClassService.class);

    public String finalClassMethod() {
        logger.info("✅ finalClassMethod() 실행 (final class)");
        return "Final class method executed - AOP may not work with CGLIB";
    }
}
