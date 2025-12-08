package com.c102.ourotest.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AopTestService {

    private static final Logger logger = LoggerFactory.getLogger(AopTestService.class);

    /**
     * 외부에서 호출되는 public 메서드 - AOP가 정상 작동함
     */
    public String externalCall() {
        logger.info("✅ externalCall() 실행");
        return "External call executed - AOP should work";
    }

    /**
     * Self-invocation 테스트 1: public 메서드가 같은 클래스의 다른 public 메서드 호출
     * 문제점: internalPublicMethod는 AOP가 적용되지 않음 (self-invocation 문제)
     */
    public String callInternalPublicMethod() {
        logger.info("✅ callInternalPublicMethod() 실행");
        logger.info("🔄 내부에서 internalPublicMethod() 호출 시도...");

        // this를 통한 호출 - 프록시를 거치지 않고 직접 호출됨
        String result = this.internalPublicMethod();

        return "Called internal public method: " + result;
    }

    /**
     * Self-invocation으로 호출될 public 메서드
     * 외부에서 직접 호출하면 AOP가 작동하지만, 같은 클래스 내에서 호출하면 AOP가 작동하지 않음
     */
    public String internalPublicMethod() {
        logger.info("✅ internalPublicMethod() 실행");
        return "Internal public method executed";
    }

    /**
     * Self-invocation 테스트 2: public 메서드가 private 메서드 호출
     * 문제점: private 메서드는 애초에 프록시 대상이 아니므로 AOP가 적용되지 않음
     */
    public String callPrivateMethod() {
        logger.info("✅ callPrivateMethod() 실행");
        logger.info("🔄 내부에서 privateMethod() 호출 시도...");

        String result = this.privateMethod();

        return "Called private method: " + result;
    }

    /**
     * Private 메서드 - AOP 프록시 대상이 아님
     */
    private String privateMethod() {
        logger.info("✅ privateMethod() 실행 (AOP 적용 불가)");
        return "Private method executed (no AOP possible)";
    }

    /**
     * 체인 호출 테스트: A -> B -> C
     * A는 외부 호출이므로 AOP 작동, B와 C는 self-invocation이므로 AOP 미작동
     */
    public String chainedCall() {
        logger.info("✅ chainedCall() 실행");
        logger.info("🔄 chainedMethodB() 호출 시도...");
        return this.chainedMethodB();
    }

    public String chainedMethodB() {
        logger.info("✅ chainedMethodB() 실행");
        logger.info("🔄 chainedMethodC() 호출 시도...");
        return this.chainedMethodC();
    }

    public String chainedMethodC() {
        logger.info("✅ chainedMethodC() 실행");
        return "Chained method C executed";
    }
}
