package com.diploma.fitra.controller;

import com.diploma.fitra.dto.error.ErrorDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException: exception {}", ex.getMessage(), ex);
        return ex.getBindingResult().getAllErrors().stream()
                .map(err -> new ErrorDto(err.getDefaultMessage(), LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(Exception ex) {
        log.error("handleBadRequestException: exception {}", ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(Exception ex) {
        log.error("handleNotFoundException: exception {}", ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleForbiddenException(Exception ex) {
        log.error("handleForbiddenException: exception {}", ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleUnauthorizedException(Exception ex) {
        log.error("handleUnauthorizedException: exception {}", ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), LocalDateTime.now());
    }
}
