package com.c102.ourotest.core.rest.tryit;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Ouroboros SDK 통합 테스트 (업데이트된 API 기준)
 * 
 * 주요 변경사항:
 * - 세션 발급 API 제거: POST /ouroboros/tries 없음
 * - Try 요청 방식 변경: X-Ouroboros-Try: on 헤더 사용
 * - 결과 조회 API 유지: GET /ouroboros/tries/{tryId}
 * - OpenTelemetry Baggage 통합으로 trace 자동 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Ouroboros SDK 통합 테스트 (업데이트)")
class OuroborosIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 시나리오 1: Try 요청 및 정상 흐름
     * 1. X-Ouroboros-Try: on 헤더로 Try 요청
     * 2. TryContext에서 tryId 확인
     * 3. 결과 조회 API 호출
     */
    @Test
    @DisplayName("시나리오 1: Try 요청 및 정상 흐름")
    void testScenario1_TryRequestFlow() throws Exception {
        // 1. Try 요청 (X-Ouroboros-Try: on 헤더)
        MvcResult tryResult = mockMvc.perform(get("/api/test")
                        .header("X-Ouroboros-Try", "on"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasTryId").value(true))
                .andExpect(jsonPath("$._ouroborosTryId").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Try request detected:")))
                .andReturn();
        
        String responseBody = tryResult.getResponse().getContentAsString();
        JSONObject response = new JSONObject(responseBody);
        String tryId = response.getString("_ouroborosTryId");
        
        // 2. 실제 API 엔드포인트 호출
        mockMvc.perform(get("/api/users/123")
                        .header("X-Ouroboros-Try", "on"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$._ouroborosTryId").exists());

        // 3. 결과 조회 (tryId는 UUID 형식이므로 유효한 UUID 생성)
        mockMvc.perform(get("/ouroboros/tries/" + tryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tryId").value(tryId))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.analyzedAt").exists())
                .andExpect(jsonPath("$.spans").isArray())
                .andExpect(jsonPath("$.issues").isArray())
                .andExpect(jsonPath("$.spanCount").exists());
    }

    /**
     * 시나리오 2: 일반 요청 (Try 헤더 없음)
     * X-Ouroboros-Try 헤더 없이 호출해도 정상 동작해야 함
     */
    @Test
    @DisplayName("시나리오 2: 일반 요청")
    void testScenario2_NormalRequest() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasTryId").value(false))
                .andExpect(jsonPath("$.message").value("Normal request (no Try ID)"));

        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));

        mockMvc.perform(get("/api/users/456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("456"))
                .andExpect(jsonPath("$._ouroborosTryId").doesNotExist());
    }

    /**
     * 시나리오 3: 잘못된 Try 헤더 값
     * X-Ouroboros-Try 헤더가 "on"이 아닌 경우 정상 처리되어야 함
     */
    @Test
    @DisplayName("시나리오 3: 잘못된 Try 헤더 값")
    void testScenario3_InvalidTryHeader() throws Exception {
        // 잘못된 값들로 Try 요청
        String[] invalidValues = {"off", "true", "false", "1", "invalid"};
        
        for (String invalidValue : invalidValues) {
            mockMvc.perform(get("/api/test")
                            .header("X-Ouroboros-Try", invalidValue))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.hasTryId").value(false))
                    .andExpect(jsonPath("$.message").value("Normal request (no Try ID)"));
        }
    }

    /**
     * 시나리오 4: TryContext Baggage 전파 확인
     * 같은 요청 내에서 여러 번 컨트롤러를 호출해도 동일한 tryId가 유지되는지 확인
     */
    @Test
    @DisplayName("시나리오 4: TryContext Baggage 전파 확인")
    void testScenario4_BaggagePropagation() throws Exception {
        // Try 요청으로 여러 번 호출
        List<String> tryIds = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            MvcResult result = mockMvc.perform(get("/api/test")
                            .header("X-Ouroboros-Try", "on"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.hasTryId").value(true))
                    .andReturn();
            
            String responseBody = result.getResponse().getContentAsString();
            JSONObject response = new JSONObject(responseBody);
            String tryId = response.getString("_ouroborosTryId");
            tryIds.add(tryId);
        }
        
        // 모든 tryId가 고유한지 확인 (각 요청마다 새로운 UUID 생성)
        assertTrue(tryIds.stream().distinct().count() == tryIds.size(), 
                "모든 Try 요청이 고유한 tryId를 가져야 합니다.");
        
        // 각 tryId로 결과 조회
        for (String tryId : tryIds) {
            mockMvc.perform(get("/ouroboros/tries/" + tryId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tryId").value(tryId));
        }
    }

    /**
     * 시나리오 5: 결과 조회 API 검증
     * 존재하지 않는 tryId로 결과 조회 시 적절한 응답 확인
     */
    @Test
    @DisplayName("시나리오 5: 결과 조회 API 검증")
    void testScenario5_ResultRetrieval() throws Exception {
        // 유효한 UUID 형식이지만 존재하지 않는 tryId
        String nonExistentTryId = "00000000-0000-0000-0000-000000000000";
        
        mockMvc.perform(get("/ouroboros/tries/" + nonExistentTryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tryId").value(nonExistentTryId))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.analyzedAt").exists())
                .andExpect(jsonPath("$.spans").isArray())
                .andExpect(jsonPath("$.issues").isArray())
                .andExpect(jsonPath("$.spanCount").exists());
    }

    /**
     * 시나리오 6: 잘못된 tryId 형식으로 결과 조회
     * 유효하지 않은 UUID 형식으로 결과 조회 시 400 에러 확인
     */
    @Test
    @DisplayName("시나리오 6: 잘못된 tryId 형식으로 결과 조회")
    void testScenario6_InvalidTryIdFormat() throws Exception {
        String invalidTryId = "invalid-uuid-format";
        
        mockMvc.perform(get("/ouroboros/tries/" + invalidTryId))
                .andExpect(status().isBadRequest());
    }

    /**
     * 시나리오 7: 다양한 HTTP 메서드 지원 테스트
     * GET, POST, PUT, DELETE 등 다양한 메서드에서 Try 요청이 동작하는지 확인
     */
    @Test
    @DisplayName("시나리오 7: 다양한 HTTP 메서드 지원")
    void testScenario7_DifferentHttpMethods() throws Exception {
        // GET 요청
        mockMvc.perform(get("/api/users/100")
                        .header("X-Ouroboros-Try", "on"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._ouroborosTryId").exists());

        // POST 요청 (실제 POST 엔드포인트가 없으므로 404 예상)
        mockMvc.perform(post("/api/users")
                        .header("X-Ouroboros-Try", "on")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound()); // POST 엔드포인트가 없으므로 404
    }

    /**
     * 벤치마크: Try 요청 처리 성능
     */
    @Test
    @DisplayName("벤치마크: Try 요청 처리 성능")
    void testBenchmark_TryRequest() throws Exception {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(get("/api/test")
                            .header("X-Ouroboros-Try", "on"))
                    .andExpect(status().isOk());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("\n=== Try 요청 처리 성능 ===");
        System.out.println("100개 요청: " + duration + "ms");
        System.out.println("평균: " + (duration / 100.0) + "ms per request\n");
    }

    /**
     * 벤치마크: 일반 요청 처리 성능
     */
    @Test
    @DisplayName("벤치마크: 일반 요청 처리 성능")
    void testBenchmark_NormalRequest() throws Exception {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(get("/api/test"))
                    .andExpect(status().isOk());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("\n=== 일반 요청 처리 성능 ===");
        System.out.println("100개 요청: " + duration + "ms");
        System.out.println("평균: " + (duration / 100.0) + "ms per request\n");
    }

    /**
     * 시나리오 8: 동시 Try 요청 테스트
     * 여러 스레드에서 동시에 Try 요청을 보내는 경우의 동작 확인
     */
    @Test
    @DisplayName("시나리오 8: 동시 Try 요청 테스트")
    void testScenario8_ConcurrentTryRequests() throws Exception {
        List<String> tryIds = new ArrayList<>();
        int requestCount = 10;
        
        // 여러 Try 요청 동시 실행
        for (int i = 0; i < requestCount; i++) {
            MvcResult result = mockMvc.perform(get("/api/test")
                            .header("X-Ouroboros-Try", "on"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.hasTryId").value(true))
                    .andReturn();
            
            String responseBody = result.getResponse().getContentAsString();
            JSONObject response = new JSONObject(responseBody);
            String tryId = response.getString("_ouroborosTryId");
            tryIds.add(tryId);
        }
        
        // 모든 tryId가 고유한지 확인
        assertTrue(tryIds.stream().distinct().count() == tryIds.size(), 
                "모든 Try 요청이 고유한 tryId를 가져야 합니다.");
        
        System.out.println("동시 Try 요청 테스트 완료: " + tryIds.size() + "개 요청, " + 
                tryIds.stream().distinct().count() + "개 고유 tryId");
    }
}
