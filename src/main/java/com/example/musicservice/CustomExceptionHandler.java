package com.example.musicservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = MusicNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMusicNotFoundException(
            MusicNotFoundException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MusicDuplicatedException.class)
    public ResponseEntity<Map<String, String>> handleMusicDuplicatedException(
            MusicDuplicatedException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.CONFLICT.value()),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", fieldError.getDefaultMessage());
            errors.add(error);
        });
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 下記の長文のコードをコメントアウト下記のRecordクラスに書き直すことができる。
     *
     * @param status
     * @param message
     * @param errors
     */
//    public static final class ErrorResponse {
//        private final HttpStatus status;
//        private final String message;
//        private final List<Map<String, String>> errors;
//
//        public ErrorResponse(HttpStatus status, String message, List<Map<String, String>> errors) {
//            this.status = status;
//            this.message = message;
//            this.errors = errors;
//        }
//
//        public HttpStatus getStatus() {
//            return status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public List<Map<String, String>> getErrors() {
//            return errors;
//        }
//
//    }
    public record ErrorResponse(HttpStatus status, String message, List<Map<String, String>> errors) {
    }
/**
 * 下記のように例外処理をした場合は他の部分に影響を及ぼすため危険とのこと。2024/4/17
 */
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
//        List<Map<String, String>> errors = new ArrayList<>();
//        Map<String, String> error = new HashMap<>();
//        error.put("message", "その楽曲は他のIDで登録されています");
//        errors.add(error);
//        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, "Duplicate entry", errors);
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
//    }
}
//　エラーハンドリング用のクラス
