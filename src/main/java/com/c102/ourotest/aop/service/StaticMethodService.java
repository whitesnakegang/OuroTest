package com.c102.ourotest.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Static 메서드 테스트
 * AOP 프록시는 인스턴스 메서드에만 적용 가능
 * Static 메서드는 클래스 레벨에서 동작하므로 프록시 패턴을 적용할 수 없음
 */
@Service
public class StaticMethodService {

    private static final Logger logger = LoggerFactory.getLogger(StaticMethodService.class);

    /**
     * 일반 인스턴스 메서드 - AOP 적용 가능
     */
    public String instanceMethod() {
        logger.info("✅ instanceMethod() 실행");
        logger.info("🔄 static 메서드 호출 시도...");
        String result = StaticMethodService.staticMethod();
        return "Instance method called static method: " + result;
    }

    /**
     * Static 메서드 - AOP 적용 불가
     * Static 메서드는 프록시 대상이 아니므로 AOP가 작동하지 않음
     */
    public static String staticMethod() {
        logger.info("✅ staticMethod() 실행 (AOP 적용 불가)");
        return "Static method executed (no AOP possible)";
    }
}
