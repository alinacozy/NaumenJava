package ru.alinacozy.NauJava.dto;

import java.time.LocalDateTime;

public class ExceptionDTO
{
    private final LocalDateTime timestamp;
    private int status;
    private String message;

    private ExceptionDTO(int status, String message)
    {
        this.timestamp=LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public static ExceptionDTO create(int status, Throwable e)
    {
        return new ExceptionDTO(status, e.getMessage());
    }

    public static ExceptionDTO create(int status, String message)
    {
        return new ExceptionDTO(status, message);
    }
}

