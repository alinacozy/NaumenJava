package ru.alinacozy.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config
{

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void init() {
        System.out.println("======================================");
        System.out.println("        Приложение: " + appName);
        System.out.println("        Версия: " + appVersion);
        System.out.println("======================================");
    }
}
