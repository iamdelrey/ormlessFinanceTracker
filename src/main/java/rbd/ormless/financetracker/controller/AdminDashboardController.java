package rbd.ormless.financetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequestMapping("/admin-dashboard")
public class AdminDashboardController {

    private final rbd.ormless.financetracker.service.UserService userService;

    public AdminDashboardController(rbd.ormless.financetracker.service.UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewAdminDashboard(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser, Model model) {
        // Используем метод сервиса, чтобы получить роль пользователя
        String role = userService.findByEmail(springUser.getUsername()).getRole();
        if (!"ADMIN".equals(role)) {
            return "redirect:/"; // Перенаправление, если не админ
        }

        // Передаем список пользователей в модель
        model.addAttribute("users", userService.getAllUsers());
        return "admin-dashboard";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute rbd.ormless.financetracker.model.User user) {
        userService.register(user);
        return "redirect:/admin-dashboard";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin-dashboard";
    }
}
