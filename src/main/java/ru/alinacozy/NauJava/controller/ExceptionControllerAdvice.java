package ru.alinacozy.NauJava.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.alinacozy.NauJava.dto.ExceptionDTO;

import java.util.Optional;

@ControllerAdvice
public class ExceptionControllerAdvice
{
    @ExceptionHandler(java.lang.Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO exception(java.lang.Exception e)
    {
        return ExceptionDTO.create(500, "Непредвиденная ошибка на сервере: " + e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO exception(ResourceNotFoundException e)
    {
        return ExceptionDTO.create(404, "Запрашиваемый ресурс не существует в системе");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO exception(IllegalArgumentException e)
    {
        return ExceptionDTO.create(400, "Неверные параметры запроса. " + e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleNoHandlerFound(org.springframework.web.servlet.NoHandlerFoundException e) {
        return ExceptionDTO.create(404, "Страница не найдена. Проверьте правильность URL: " + e.getRequestURL());
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionDTO handleMethodNotAllowed(org.springframework.web.HttpRequestMethodNotSupportedException e) {
        String supportedMethods = Optional.ofNullable(e.getSupportedMethods())
                .map(methods -> String.join(", ", methods))
                .orElse("");
        return ExceptionDTO.create(405, "Метод " + e.getMethod() + " не поддерживается для этого эндпоинта. Поддерживаемые методы: " + String.join(", ", supportedMethods));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDTO handleAccessDenied(AccessDeniedException e) {
        return ExceptionDTO.create(403, "У вас недостаточно прав для доступа к этому ресурсу.");
    }

}

