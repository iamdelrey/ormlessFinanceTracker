package rbd.ormless.financetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import rbd.ormless.financetracker.model.Account;
import rbd.ormless.financetracker.service.AccountService;
import rbd.ormless.financetracker.model.User;
import rbd.ormless.financetracker.service.UserService;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping
    public String listAccounts(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        if (userDetails == null) {
            return "redirect:/login"; // Перенаправление на страницу авторизации
        }

        // Получение email текущего пользователя
        String email = userDetails.getUsername();

        // Загрузка объекта User из базы данных
        rbd.ormless.financetracker.model.User user = accountService.getUserByEmail(email);
        if (user == null) {
            return "redirect:/login"; // Если пользователь не найден, перенаправляем на логин
        }

        model.addAttribute("accounts", accountService.getAllAccounts(user.getId()));
        return "accounts";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute Account account, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            return "redirect:/login"; // Перенаправление на страницу авторизации
        }

        // Получение ID пользователя из базы данных по email
        String email = springUser.getUsername(); // Email пользователя
        rbd.ormless.financetracker.model.User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/login"; // Если пользователь не найден
        }

        account.setIdUser(user.getId());
        accountService.addAccount(account);
        return "redirect:/accounts";
    }


    @PostMapping("/update")
    public String updateAccount(@ModelAttribute Account account, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            return "redirect:/login";
        }

        // Получаем пользователя из базы данных
        String email = springUser.getUsername();
        rbd.ormless.financetracker.model.User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        account.setIdUser(user.getId());
        accountService.updateAccount(account);
        return "redirect:/accounts";
    }


    @PostMapping("/delete")
    public String deleteAccount(@RequestParam int accountId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            return "redirect:/login";
        }

        // Получаем пользователя
        String email = springUser.getUsername();
        rbd.ormless.financetracker.model.User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        // Удаляем аккаунт
        accountService.deleteAccount(accountId, user.getId());
        return "redirect:/accounts";
    }

}
