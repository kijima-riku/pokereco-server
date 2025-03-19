package com.pokereco.pokereco.responseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.LinkedHashMap;

@Component
public class ResponseBuilder {

  // 成功レスポンス
  public <T> ResponseEntity<Map<String, Object>> buildSuccessResponse(T data) {
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("data", data);

    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  // エラーレスポンス
  public ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("message", message);

    return new ResponseEntity<>(responseBody, status);
  }
}
