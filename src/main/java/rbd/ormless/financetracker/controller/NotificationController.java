package rbd.ormless.financetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import rbd.ormless.financetracker.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.service.NotificationService;
import rbd.ormless.financetracker.service.UserService;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/delete")
    public String deleteNotification(@RequestParam int notificationId, @RequestParam int goalId) {
        System.out.println("Удаляем уведомление с ID: " + notificationId);
        notificationService.deleteNotification(notificationId);

        // Убедитесь, что здесь не запускаются лишние процессы
        System.out.println("Уведомление удалено. Перенаправление к целям.");
        return "redirect:/budget-plans/" + goalId;
    }

    @PostMapping("/mark-read")
    public String markAllNotificationsAsRead(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        notificationService.markAllAsReadForUser(user.getId());
        return "redirect:/budget-plans";
    }
}
