package ru.alinacozy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alinacozy.NauJava.entity.User;
import ru.alinacozy.NauJava.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("message", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String adduser(User user, Model model) {
        try {
            userService.addUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            // Пользователь уже существует
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }  catch (Exception ex) {
            // Логируем ошибку для администратора
            System.out.println("Ошибка при регистрации пользователя: " + ex.getMessage());
            model.addAttribute("message", "Техническая ошибка. Пожалуйста, попробуйте позже.");
            return "registration";
        }
    }

}
