package com.muhammet.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.muhammet.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static com.muhammet.exception.ErrorType.BAD_REQUEST_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handlerRuntimeException(RuntimeException exception){
        ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR;
        return  new ResponseEntity<>(createMessage(errorType,exception),errorType.getHttpStatus());
    }

    /**
     * ExceptionHandler -> kendisine verilen Exception sınıfını dinlemeye başlar var eğer
     * ilgili sınıf hata fırlatırsa onu yakalar.
     *
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ResponseEntity<ResponseDto> handlerSatisException(AuthException satisException){
        return  new ResponseEntity<>(createMessage(satisException.getErrorType(),satisException), satisException.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ResponseDto> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<String> fields = new ArrayList<>();
        exception
                .getBindingResult()
                .getFieldErrors().forEach(x->fields.add(x.getField()+ ": "+ x.getDefaultMessage()));

        return new ResponseEntity<>(createMessage(BAD_REQUEST_ERROR, exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public final ResponseEntity<ResponseDto> handleJDBCConnectionException(
            HttpMessageNotReadableException exception) {
        ErrorType errorType = BAD_REQUEST_ERROR;
        return new ResponseEntity<>(createMessage(errorType, exception), errorType.getHttpStatus());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ResponseDto> handleMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ErrorType errorType = BAD_REQUEST_ERROR;
        return new ResponseEntity<>(createMessage(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public final ResponseEntity<ResponseDto> handleInvalidFormatException(
            InvalidFormatException exception) {
        ErrorType errorType = BAD_REQUEST_ERROR;
        return new ResponseEntity<>(createMessage(errorType, exception), errorType.getHttpStatus());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ResponseDto> handleMethodArgumentMisMatchException(
            MethodArgumentTypeMismatchException exception) {

        ErrorType errorType = BAD_REQUEST_ERROR;
        return new ResponseEntity<>(createMessage(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public final ResponseEntity<ResponseDto> handleMethodArgumentMisMatchException(
            MissingPathVariableException exception) {

        ErrorType errorType = BAD_REQUEST_ERROR;
        return new ResponseEntity<>(createMessage(errorType, exception), errorType.getHttpStatus());
    }


    private ResponseDto<String> createMessage(ErrorType errorType, Exception exception) {
        log.error("Tüm Hataların geçtiği ortak nokta: " + exception);
        return ResponseDto.<String>builder()
                .data("")
                .message(exception.getMessage())
                .code(errorType.getCode())
                .build();
    }


}
