package com.c102.ourotest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api")
public class OpenApiController {

    private final ObjectMapper yamlMapper;
    private final ObjectMapper jsonMapper;

    public OpenApiController() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.jsonMapper = new ObjectMapper();
    }

    /**
     * openapi.yml 파일을 JSON으로 변환하여 반환
     * @return JSON 형태의 OpenAPI 스펙
     */
    @GetMapping(value = "/openapi.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOpenApiJson() {
        try {
            // resources/static/openapi.yml 파일 읽기
            ClassPathResource resource = new ClassPathResource("static/newtopia.yaml");
            String yamlContent = Files.readString(resource.getFile().toPath());
            
            // YAML을 Object로 파싱
            Object yamlObject = yamlMapper.readValue(yamlContent, Object.class);
            
            // Object를 JSON으로 변환
            String jsonString = jsonMapper.writeValueAsString(yamlObject);
            Object jsonObject = jsonMapper.readValue(jsonString, Object.class);
            
            return ResponseEntity.ok(jsonObject);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"YAML 파일을 읽을 수 없습니다: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"YAML을 JSON으로 변환하는 중 오류가 발생했습니다: " + e.getMessage() + "\"}");
        }
    }

    /**
     * openapi.yml 파일의 원본 YAML 내용을 반환
     * @return YAML 형태의 OpenAPI 스펙
     */
    @GetMapping(value = "/openapi.yml", produces = "application/x-yaml")
    public ResponseEntity<String> getOpenApiYaml() {
        try {
            ClassPathResource resource = new ClassPathResource("static/openapi.yml");
            String yamlContent = Files.readString(resource.getFile().toPath());
            return ResponseEntity.ok()
                    .header("Content-Type", "application/x-yaml")
                    .body(yamlContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("YAML 파일을 읽을 수 없습니다: " + e.getMessage());
        }
    }
}
