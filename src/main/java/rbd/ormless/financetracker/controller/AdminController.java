//package rbd.ormless.financetracker.controller;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import rbd.ormless.financetracker.service.UserService;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final UserService userService;
//
//    public AdminController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/users")
//    public String manageUsers(Model model) {
//        model.addAttribute("users", userService.getAllUsers()); // Метод получения всех пользователей
//        return "admin/users"; // Шаблон для управления пользователями
//    }
//
//    @GetMapping
//    public String adminDashboard(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser, Model model) {
//        model.addAttribute("adminName", springUser.getUsername());
//        return "admin/dashboard";
//    }
//
//    @PostMapping("/users/delete")
//    public String deleteUser(@RequestParam int userId) {
//        userService.deleteUser(userId); // Метод удаления пользователя
//        return "redirect:/admin/users";
//    }
//
//}
