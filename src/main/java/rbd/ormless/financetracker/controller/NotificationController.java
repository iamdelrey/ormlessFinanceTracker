package rbd.ormless.financetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
        notificationService.deleteNotification(notificationId);
        return "redirect:/budget-plans/" + goalId;
    }

    @PostMapping("/mark-read")
    public String markAllNotificationsAsRead(@AuthenticationPrincipal User springUser, @RequestParam int goalId) {
        rbd.ormless.financetracker.model.User user = userService.findByEmail(springUser.getUsername());
        notificationService.markAllAsReadForUser(user.getId());
        return "redirect:/budget-plans/" + goalId;
    }
}
