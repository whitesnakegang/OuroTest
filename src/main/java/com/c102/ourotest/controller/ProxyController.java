package com.c102.ourotest.controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/proxy/tempo")
public class ProxyController {

    private static final String TEMPO_BASE = "http://localhost:3200";
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) throws IOException {
        String requestUri = request.getRequestURI();
        String downstreamPath = requestUri.replaceFirst("^/proxy/tempo", "");
        if (downstreamPath.isEmpty()) {
            downstreamPath = "/";
        }

        String query = request.getQueryString();
        String targetUrl = TEMPO_BASE + downstreamPath + (query != null ? ("?" + query) : "");

        HttpMethod method;
        try {
            method = HttpMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException ex) {
            method = HttpMethod.GET;
        }

        HttpHeaders forwardHeaders = extractHeaders(request);
        // Remove hop-by-hop headers
        forwardHeaders.remove(HttpHeaders.HOST);
        forwardHeaders.remove(HttpHeaders.CONTENT_LENGTH);

        HttpEntity<byte[]> entity = new HttpEntity<>(body, forwardHeaders);
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(URI.create(targetUrl), method, entity, byte[].class);
            HttpHeaders responseHeaders = filterResponseHeaders(response.getHeaders());
            return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"Tempo proxy failed\",\"message\":\"" + escape(ex.getMessage()) + "\"}").getBytes());
        }
    }

    private static HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                headers.add(name, values.nextElement());
            }
        }
        return headers;
    }

    private static HttpHeaders filterResponseHeaders(HttpHeaders original) {
        HttpHeaders filtered = new HttpHeaders();
        for (String key : original.keySet()) {
            if (HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(key) || HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(key)) {
                continue;
            }
            filtered.put(key, original.getOrDefault(key, Collections.emptyList()));
        }
        return filtered;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}


