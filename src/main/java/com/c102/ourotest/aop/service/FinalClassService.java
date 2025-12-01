package com.c102.ourotest.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Final 클래스 테스트
 * CGLIB는 클래스를 상속하여 프록시를 생성하는데, final 클래스는 상속이 불가능함
 * 따라서 Spring AOP는 final 클래스에 대해 프록시를 생성할 수 없음
 *
 * 참고: 이 클래스는 기본적으로 비활성화되어 있습니다.
 * application.properties에 aop.test.final-class.enabled=true를 추가하면 활성화되어 오류가 발생합니다.
 */
@Service
@ConditionalOnProperty(name = "aop.test.final-class.enabled", havingValue = "true", matchIfMissing = false)
public final class FinalClassService {

    private static final Logger logger = LoggerFactory.getLogger(FinalClassService.class);

    public String finalClassMethod() {
        logger.info("✅ finalClassMethod() 실행 (final class)");
        return "Final class method executed - AOP may not work with CGLIB";
    }
}
