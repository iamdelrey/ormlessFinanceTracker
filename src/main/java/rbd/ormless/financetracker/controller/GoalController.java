package rbd.ormless.financetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.model.Goal;
import rbd.ormless.financetracker.model.User;
import rbd.ormless.financetracker.service.AccountService;
import rbd.ormless.financetracker.service.BudgetPlanService;
import rbd.ormless.financetracker.service.GoalService;
import rbd.ormless.financetracker.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;
    private final AccountService accountService;
    private BudgetPlanService budgetPlanService;

    public GoalController(GoalService goalService, UserService userService, AccountService accountService) {
        this.goalService = goalService;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public String listAccountsForGoals(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            return "redirect:/login";
        }

        String email = springUser.getUsername();
        User user = userService.findByEmail(email);

        if (user != null) {
            model.addAttribute("accounts", accountService.getAllAccounts(user.getId()));
            return "select-account-goals";
        } else {
            return "redirect:/error";
        }
    }


    @GetMapping("/{accountId}")
    public String listGoals(@PathVariable int accountId, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            return "redirect:/login";
        }

        List<Goal> goals = goalService.getGoalsByAccountId(accountId);
        if (goals == null || goals.isEmpty()) {
            System.out.println("Нет целей для аккаунта с ID: " + accountId);
        } else {
            goals.forEach(goal -> System.out.println("Цель: " + goal.getIdGoal() + " - " + goal.getGoalName()));
        }

        model.addAttribute("goals", goals);
        model.addAttribute("accountId", accountId);
        model.addAttribute("goal", new Goal()); // Добавляем пустую цель для формы
        return "goals";
    }
    @PostMapping("/{accountId}/batch-update")
    public String batchUpdateGoals(@PathVariable int accountId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser, @RequestParam Map<String, String> formData) {
        List<Goal> goals = new ArrayList<>();

        try {
            // Получение ID пользователя из текущей сессии
            String email = springUser.getUsername();
            User user = userService.findByEmail(email);
            int userId = user.getId();

            formData.forEach((key, value) -> {
                if (key.matches("goals\\[\\d+\\]\\.idGoal")) {
                    int idGoal = Integer.parseInt(value);
                    Goal goal = new Goal();
                    goal.setIdGoal(idGoal);
                    goal.setIdAccount(accountId);
                    goal.setIdUser(userId); // Установка idUser

                    String baseKey = "goals[" + idGoal + "].";
                    goal.setGoalName(formData.get(baseKey + "goalName"));
                    goal.setGoalAmount(Integer.parseInt(formData.get(baseKey + "goalAmount")));

                    System.out.println("Обновляем цель: " + goal);
                    goals.add(goal);
                }
            });

            for (Goal goal : goals) {
                goalService.updateGoal(goal);
                System.out.println("Цель успешно обновлена: " + goal);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }

        return "redirect:/goals/" + accountId;
    }


    @PostMapping("/{accountId}/add")
    public String addGoal(@PathVariable int accountId, @ModelAttribute Goal goal, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        String email = springUser.getUsername();
        User user = userService.findByEmail(email);
        goal.setIdUser(user.getId());
        goal.setIdAccount(accountId);
        goalService.addGoal(goal);
        return "redirect:/goals/" + accountId;
    }

    @PostMapping("/{accountId}/update")
    public String updateGoal(@PathVariable int accountId, @ModelAttribute Goal goal) {
        goal.setIdAccount(accountId);
        goalService.updateGoal(goal);
        return "redirect:/goals/" + accountId;
    }

    @PostMapping("/{accountId}/delete")
    public String deleteGoal(@PathVariable int accountId, @RequestParam int goalId) {
        goalService.deleteGoal(goalId, accountId);
        return "redirect:/goals/" + accountId;
    }

    @GetMapping("/goals/{goalId}/total-budget")
    public String getTotalBudgetForGoal(@PathVariable Long goalId, Model model) {
        BigDecimal totalBudget = budgetPlanService.calculateTotalBudgetForGoal(goalId);
        model.addAttribute("totalBudget", totalBudget);
        return "goals";
    }

}
