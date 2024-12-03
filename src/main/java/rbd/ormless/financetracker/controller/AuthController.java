package rbd.ormless.financetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.model.User;
import rbd.ormless.financetracker.service.UserService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.register(user);
            model.addAttribute("success", "Вы успешно зарегистрировались! Теперь вы можете <a href='/login'>войти</a>.");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка: Пользователь с таким email уже существует!");
        }
        return "register"; // Возвращаем имя HTML-шаблона, а не строку
    }









}
