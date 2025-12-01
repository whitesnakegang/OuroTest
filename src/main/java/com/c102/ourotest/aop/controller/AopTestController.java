package com.c102.ourotest.aop.controller;

import com.c102.ourotest.aop.service.AopTestService;
import com.c102.ourotest.aop.service.FinalClassService;
import com.c102.ourotest.aop.service.StaticMethodService;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/aop-test")
public class AopTestController {

    private final AopTestService aopTestService;
    private final StaticMethodService staticMethodService;

    @Autowired(required = false)
    private FinalClassService finalClassService;

    public AopTestController(AopTestService aopTestService,
                           StaticMethodService staticMethodService) {
        this.aopTestService = aopTestService;
        this.staticMethodService = staticMethodService;
    }

    /**
     * Self-invocation 문제 테스트
     * - 외부 호출: AOP 작동 O
     * - 내부 호출 (this.method()): AOP 작동 X
     * - Private 메서드: AOP 작동 X
     */
    @GetMapping("/self-invocation")
    @ApiState(state = State.COMPLETED)
    public Map<String, Object> testSelfInvocation() {
        Map<String, Object> response = new HashMap<>();

        System.out.println("\n=== Self-Invocation Test Start ===");

        // 1. 외부 호출 (정상 작동)
        System.out.println("\n[1] 외부 호출 테스트:");
        aopTestService.externalCall();

        // 2. 내부 public 메서드 호출 (self-invocation 문제)
        System.out.println("\n[2] Self-invocation 테스트 (public -> public):");
        aopTestService.callInternalPublicMethod();

        // 3. 같은 메서드를 외부에서 호출하면 AOP 작동
        System.out.println("\n[3] 같은 메서드를 외부에서 호출:");
        aopTestService.internalPublicMethod();

        // 4. Private 메서드 호출
        System.out.println("\n[4] Private 메서드 호출 테스트:");
        aopTestService.callPrivateMethod();

        System.out.println("\n=== Self-Invocation Test End ===\n");

        response.put("message", "콘솔 로그를 확인하세요");
        response.put("expected", "외부 호출과 직접 호출은 AOP 로그 출력, self-invocation과 private은 AOP 로그 미출력");
        return response;
    }

    /**
     * CGLIB Proxy 제한사항 테스트
     * - Final 클래스: 프록시 생성 불가 (상속 불가)
     * - Static 메서드: 프록시 대상 아님
     */
    @GetMapping("/cglib-limitations")
    @ApiState(state = State.COMPLETED)
    public Map<String, Object> testCglibLimitations() {
        Map<String, Object> response = new HashMap<>();

        System.out.println("\n=== CGLIB Proxy Limitations Test Start ===");

        // 1. Final 클래스 테스트
        System.out.println("\n[1] Final 클래스 테스트:");
        if (finalClassService != null) {
            finalClassService.finalClassMethod();
        } else {
            System.out.println("⚠️ FinalClassService는 비활성화되어 있습니다.");
            System.out.println("💡 활성화하려면 application.properties에 'aop.test.final-class.enabled=true'를 추가하세요.");
            System.out.println("💡 활성화 시 'Cannot subclass final class' 오류가 발생합니다 (CGLIB 제한사항)");
            response.put("final_class_note", "Final 클래스는 CGLIB 프록시 생성 불가 - 활성화하면 애플리케이션 시작 실패");
        }

        // 2. Static 메서드 테스트
        System.out.println("\n[2] Static 메서드 테스트:");
        staticMethodService.instanceMethod();

        System.out.println("\n=== CGLIB Proxy Limitations Test End ===\n");

        response.put("message", "콘솔 로그를 확인하세요");
        response.put("expected", "Static 메서드는 AOP 로그 미출력");
        return response;
    }
}
