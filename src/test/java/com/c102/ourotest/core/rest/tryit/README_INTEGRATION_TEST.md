# Ouroboros SDK 통합 테스트 가이드 (업데이트)

이 문서는 업데이트된 Ouroboros SDK의 통합 테스트 실행 방법을 설명합니다.

## 주요 변경사항

### API 변경사항
- ❌ **세션 발급 API 제거**: `POST /ouroboros/tries` 엔드포인트 삭제
- ✅ **Try 요청 방식 변경**: `X-Ouroboros-Try: on` 헤더 사용
- ✅ **결과 조회 API 유지**: `GET /ouroboros/tries/{tryId}` 엔드포인트 유지
- ✅ **OpenTelemetry 통합**: Baggage를 통한 자동 trace 생성

### 동작 방식 변경
1. **Try 요청 식별**: `X-Ouroboros-Try: on` 헤더로 Try 요청 식별
2. **tryId 생성**: 각 Try 요청마다 새로운 UUID 생성
3. **Baggage 전파**: OpenTelemetry Baggage로 tryId 전파
4. **Trace 생성**: 자동으로 trace가 Tempo에 전송됨
5. **결과 조회**: tryId로 분석 결과 조회

## 프로젝트 구조

```
OuroTest/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/c102/ourotest/
│   │   │       ├── OurotestApplication.java
│   │   │       └── controller/
│   │   │           └── RestController.java  # 테스트 엔드포인트 포함
│   │   └── resources/
│   │       ├── application.properties        # SDK 설정
│   │       └── ouroboros/rest/
│   │           └── ourorest.yml
│   └── test/
│       └── java/
│           └── com/c102/ourotest/
│               ├── OurotestApplicationTests.java
│               └── core/rest/tryit/
│                   └── OuroborosIntegrationTest.java  # 통합 테스트
├── build.gradle                               # SDK 의존성 포함
├── docker-compose.yml                         # Tempo 설정
├── tempo.yaml                                 # Tempo 구성
└── README_INTEGRATION_TEST.md                 # 이 파일
```

## 테스트 실행 방법

### 1. SDK 빌드 및 설치 (필요한 경우)

SDK가 로컬 Maven 저장소에 설치되어 있지 않은 경우:

```bash
# SDK 프로젝트 디렉토리로 이동
cd /path/to/ouroboros-backend

# SDK 빌드 및 로컬 Maven 저장소에 설치
./gradlew clean build publishToMavenLocal
```

### 2. Tempo 실행 (선택사항)

Trace 저장을 확인하려면 Tempo를 실행하세요:

```bash
# Tempo 컨테이너 실행
docker-compose up -d tempo

# Tempo UI 확인 (http://localhost:3200)
```

### 3. 통합 테스트 실행

```bash
# 프로젝트 루트 디렉토리에서 실행
./gradlew clean test

# 특정 테스트만 실행
./gradlew clean test --tests "OuroborosIntegrationTest.testScenario1_TryRequestFlow"

# 모든 통합 테스트 실행
./gradlew clean test --tests "OuroborosIntegrationTest.*"

# 벤치마크 테스트만 실행
./gradlew clean test --tests "OuroborosIntegrationTest.testBenchmark*"
```

### 4. 테스트 결과 확인

테스트 실행 후 `build/reports/tests/test/index.html` 파일을 열어 상세 결과를 확인할 수 있습니다.

## 테스트 시나리오

### 시나리오 1: Try 요청 및 정상 흐름 ✅
**목적**: 전체 Try 플로우의 정상 동작을 검증

1. `X-Ouroboros-Try: on` 헤더로 Try 요청
2. `TryContext`에서 tryId 확인
3. 실제 API 엔드포인트 호출 (`GET /api/users/123`)
4. `GET /ouroboros/tries/{tryId}`로 결과 조회

### 시나리오 2: 일반 요청 ✅
**목적**: Try 기능이 기존 요청 처리에 영향을 주지 않는지 확인

- `X-Ouroboros-Try` 헤더 없이 호출
- 정상 동작해야 함
- Baggage 미설정 확인

### 시나리오 3: 잘못된 Try 헤더 값 ✅
**목적**: 유효하지 않은 Try 헤더 값 처리가 무해하게 처리되는지 확인

- `X-Ouroboros-Try` 헤더가 "on"이 아닌 경우
- 정상 응답 받아야 함 (오류 발생하지 않음)

### 시나리오 4: TryContext Baggage 전파 확인 ✅
**목적**: 같은 요청 내에서 tryId가 일관되게 전파되는지 확인

- 같은 Try 요청으로 여러 번 호출
- 각 요청에서 올바른 tryId가 설정되는지 확인
- 모든 tryId가 고유한지 확인

### 시나리오 5: 결과 조회 API 검증 ✅
**목적**: 결과 조회 API의 동작 확인

- 존재하지 않는 tryId로 결과 조회
- 올바른 응답 형식 확인

### 시나리오 6: 잘못된 tryId 형식으로 결과 조회 ✅
**목적**: 유효하지 않은 UUID 형식에 대한 적절한 에러 처리

- 잘못된 UUID 형식으로 결과 조회
- 400 Bad Request 응답 확인

### 시나리오 7: 다양한 HTTP 메서드 지원 ✅
**목적**: 다양한 HTTP 메서드에서 Try 요청이 동작하는지 확인

- GET, POST 등 다양한 메서드 테스트
- Try 헤더가 올바르게 처리되는지 확인

### 시나리오 8: 동시 Try 요청 테스트 ✅
**목적**: 여러 스레드에서 동시에 Try 요청을 보내는 경우의 동작 확인

- 여러 Try 요청 동시 실행
- 모든 tryId가 고유한지 확인

### 벤치마크 1: Try 요청 처리 성능 ⚡
**목적**: 100개 Try 요청 처리 성능 측정

- 100개 Try 요청 처리 시간 측정
- 평균 응답 시간 확인

### 벤치마크 2: 일반 요청 처리 성능 ⚡
**목적**: 100개 일반 요청 처리 성능 측정

- 100개 일반 요청 처리 시간 측정
- Try 요청과의 성능 비교

## 검증 포인트

### Try 요청 식별 & Baggage ✅
- ✅ `X-Ouroboros-Try: on` 헤더 인식
- ✅ Baggage (`ouro.try_id`) 설정 확인
- ✅ 헤더 없이도 정상 동작
- ✅ 유효하지 않은 Try 헤더 값 무시
- ✅ `TryContext`를 통한 tryId 접근

### 결과 조회 API ✅
- ✅ 결과 조회 엔드포인트 동작
- ✅ 올바른 응답 형식 확인
- ✅ 존재하지 않는 tryId에 대한 적절한 응답
- ✅ 잘못된 UUID 형식에 대한 400 에러

### OpenTelemetry 통합 ✅
- ✅ OpenTelemetry Baggage 자동 설정
- ✅ Trace 자동 생성 및 Tempo 전송
- ✅ Try 요청 식별을 위한 Baggage 활용

## 테스트 실행 예시

```bash
# 1. 전체 테스트 실행
./gradlew clean test

# 2. 통합 테스트만 실행
./gradlew clean test --tests "OuroborosIntegrationTest"

# 3. 특정 시나리오만 실행
./gradlew clean test --tests "OuroborosIntegrationTest.testScenario1_TryRequestFlow"

# 4. 벤치마크만 실행
./gradlew clean test --tests "OuroborosIntegrationTest.testBenchmark*"

# 5. 상세 로그와 함께 실행
./gradlew clean test --info

# 6. 테스트 보고서 확인 (Windows)
start build/reports/tests/test/index.html

# 6. 테스트 보고서 확인 (Mac/Linux)
open build/reports/tests/test/index.html
```

## 문제 해결

### 에러 1: SDK를 찾을 수 없음
```
Could not find kr.co:ouroboros:0.0.1-SNAPSHOT
```

**해결방법**:
1. SDK 프로젝트에서 `./gradlew clean build publishToMavenLocal` 실행
2. 또는 `build.gradle`에서 SDK 버전 확인

### 에러 2: 테스트 실패
```
java.lang.AssertionError: Expected status <200> but was <404>
```

**해결방법**:
1. SDK가 올바르게 설치되었는지 확인
2. `application.properties` 설정 확인
3. SDK의 `TryController`가 제대로 등록되었는지 확인

### 에러 3: TryContext 관련 에러
```
java.lang.ClassNotFoundException: kr.co.ouroboros.core.rest.tryit.util.TryContext
```

**해결방법**:
1. SDK 빌드가 성공적으로 완료되었는지 확인
2. `build.gradle` 의존성 재확인

### 에러 4: OpenTelemetry 관련 에러
```
java.lang.NoClassDefFoundError: io/opentelemetry/api/baggage/Baggage
```

**해결방법**:
1. `build.gradle`에 `micrometer-tracing-bridge-otel` 의존성 추가 확인
2. `spring-boot-starter-actuator` 의존성 추가 확인

## 다음 단계

현재 업데이트된 SDK 기준으로 통합 테스트가 완료되었습니다. 다음 단계는:

1. **Trace 분석**: Tempo에서 실제 trace 데이터 확인
2. **성능 최적화**: Try 요청 처리 성능 개선
3. **모니터링**: 운영 환경에서의 Try 요청 모니터링

## 참고 자료

- Ouroboros SDK 문서: `../ouroboros-backend/docs/`
- OpenTelemetry Baggage: https://opentelemetry.io/docs/specs/otel/trace/api/#baggage
- Tempo Query: https://grafana.com/docs/tempo/latest/query/traceql/
- Micrometer Tracing: https://micrometer.io/docs/tracing
