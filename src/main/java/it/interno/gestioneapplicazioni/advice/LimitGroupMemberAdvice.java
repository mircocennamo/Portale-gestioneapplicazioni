package it.interno.gestioneapplicazioni.advice;

import it.interno.gestioneapplicazioni.dto.ResponseDto;
import it.interno.gestioneapplicazioni.exception.ExceededLimitGroupMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LimitGroupMemberAdvice {


    @ExceptionHandler(ExceededLimitGroupMemberException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ResponseDto<String>> exceededLimitExceptionHandler(RuntimeException e) {
          ResponseDto<String> responseDto = ResponseDto.<String>builder().code(HttpStatus.UNPROCESSABLE_ENTITY.value()).error(e.getMessage()).build();
        return new ResponseEntity(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
