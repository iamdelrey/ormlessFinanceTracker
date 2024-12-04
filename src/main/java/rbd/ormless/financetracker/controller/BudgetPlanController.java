package rbd.ormless.financetracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.dao.NotificationDAO;
import rbd.ormless.financetracker.model.*;
import rbd.ormless.financetracker.service.BudgetPlanService;
import rbd.ormless.financetracker.service.CategoryService;
import rbd.ormless.financetracker.service.TransactionService;
import rbd.ormless.financetracker.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/budget-plans")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;
    private NotificationDAO notificationDAO;


    public BudgetPlanController(BudgetPlanService budgetPlanService, UserService userService,
                                CategoryService categoryService, TransactionService transactionService,
                                NotificationDAO notificationDAO) {
        this.budgetPlanService = budgetPlanService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
        this.notificationDAO = notificationDAO;
    }

    @GetMapping("/{goalId}")
    public String listBudgetPlans(@PathVariable int goalId, Model model,
                                  @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        List<BudgetPlan> budgetPlans = budgetPlanService.getBudgetPlansByGoalId(goalId);

        // Генерация уведомлений
        budgetPlanService.generateNotifications(user.getId());

        // Получение уведомлений для отображения
        List<Notification> notifications = notificationDAO.findByUserId(user.getId());

        // Форматирование даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        notifications.forEach(notification -> {
            notification.setNotificationDateTimeFormatted(
                    notification.getNotificationDateTime().format(formatter)
            );
        });

        // Остальная логика
        BigDecimal totalBudget = budgetPlans.stream()
                .map(BudgetPlan::getPlanAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Integer, BigDecimal> transactionsTotals = new HashMap<>();
        for (BudgetPlan plan : budgetPlans) {
            BigDecimal totalTransactions = budgetPlanService.calculateTotalTransactionsForBudgetPlan(plan.getIdBudget().longValue());
            transactionsTotals.put(plan.getIdBudget(), totalTransactions);
        }

        model.addAttribute("budgetPlans", budgetPlans);
        model.addAttribute("totalBudget", totalBudget);
        model.addAttribute("transactionsTotals", transactionsTotals);
        model.addAttribute("notifications", notifications); // Передаем уведомления в модель
        model.addAttribute("goalId", goalId);
        return "budget-plans";
    }

    @PostMapping("/{goalId}/generate-notifications")
    public String generateNotifications(@PathVariable int goalId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        budgetPlanService.generateNotifications(user.getId());  // Генерируем уведомления
        return "redirect:/budget-plans/" + goalId;
    }


    @GetMapping("/{goalId}/{idBudget}/details")
    public String budgetPlanDetails(@PathVariable int goalId, @PathVariable int idBudget, Model model) {
        BudgetPlan budgetPlan = budgetPlanService.getBudgetPlanById(idBudget);
        List<Category> categories = categoryService.getCategoriesByBudgetId(idBudget);

        String[] detailsArray = budgetPlan.getPlanDetails() != null
                ? budgetPlan.getPlanDetails().split("\\n")
                : new String[0];

        model.addAttribute("budgetPlan", budgetPlan);
        model.addAttribute("categories", categories);
        model.addAttribute("detailsArray", detailsArray);
        model.addAttribute("goalId", goalId);
        return "budget-plan-details";
    }

    @PostMapping("/{goalId}/{idBudget}/details/add")
    public String addPlanDetail(@PathVariable int goalId, @PathVariable int idBudget,
                                @RequestParam String newDetail) {
        BudgetPlan budgetPlan = budgetPlanService.getBudgetPlanById(idBudget);

        String currentDetails = budgetPlan.getPlanDetails() != null ? budgetPlan.getPlanDetails() : "";
        String updatedDetails = currentDetails.isEmpty() ? newDetail : currentDetails + "\n" + newDetail;

        budgetPlan.setPlanDetails(updatedDetails);
        budgetPlanService.updateBudgetPlan(budgetPlan);

        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/details";
    }

    @PostMapping("/{goalId}/add")
    public String addBudgetPlan(@PathVariable int goalId, @ModelAttribute BudgetPlan budgetPlan,
                                @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        budgetPlan.setIdUser(user.getId());
        budgetPlan.setIdGoal(goalId);
        budgetPlanService.addBudgetPlan(budgetPlan);

        if (budgetPlan.getPlanAmount().compareTo(BigDecimal.ZERO) < 0) {
            // Использование existsNotificationForPlan
            if (!notificationDAO.existsNotificationForPlan(budgetPlan.getIdBudget(), user.getId())) {
                notificationDAO.save(new Notification(
                        "Баланс плана бюджета '" + budgetPlan.getPlanName() + "' отрицательный!",
                        LocalDateTime.now(),
                        "Непрочитано",
                        user.getId()
                ));
            }
        }

        return "redirect:/budget-plans/" + goalId;
    }
    @PostMapping("/{goalId}/update")
    public String updateBudgetPlan(@PathVariable int goalId, @ModelAttribute BudgetPlan budgetPlan,
                                   @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        budgetPlanService.updateBudgetPlan(budgetPlan);

        if (budgetPlan.getPlanAmount().compareTo(BigDecimal.ZERO) < 0) {
            // Использование existsNotificationForPlan
            if (!notificationDAO.existsNotificationForPlan(budgetPlan.getIdBudget(), user.getId())) {
                notificationDAO.save(new Notification(
                        "Баланс плана бюджета '" + budgetPlan.getPlanName() + "' отрицательный!",
                        LocalDateTime.now(),
                        "Непрочитано",
                        user.getId()
                ));
            }
        }

        return "redirect:/budget-plans/" + goalId;
    }


    @PostMapping("/{goalId}/delete")
    public String deleteBudgetPlan(@PathVariable int goalId, @RequestParam int idBudget) {
        budgetPlanService.deleteBudgetPlan(idBudget);
        return "redirect:/budget-plans/" + goalId;
    }

    @PostMapping("/{goalId}/{idBudget}/add-category")
    public String addCategory(@PathVariable int goalId, @PathVariable int idBudget,
                              @ModelAttribute Category category,
                              @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        category.setIdUser(user.getId());
        category.setIdBudget(idBudget);
        category.setIdGoal(goalId);
        categoryService.addCategory(category);
        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/details";
    }

//    @GetMapping("/{goalId}/{idBudget}/search-transactions")
//    public String searchTransactions(@PathVariable int goalId,
//                                     @PathVariable int idBudget,
//                                     @RequestParam String category,
//                                     @RequestParam BigDecimal minAmount,
//                                     @RequestParam BigDecimal maxAmount,
//                                     Model model) {
//        List<Transaction> transactions = transactionService.getTransactionsByCategoryAndAmount(category, minAmount, maxAmount);
//
//        model.addAttribute("transactions", transactions);
//        model.addAttribute("goalId", goalId);
//        model.addAttribute("idBudget", idBudget);
//        return "transactions";
//    }

    @GetMapping("/{goalId}/{idBudget}/search-transactions")
    public String searchTransactions(@PathVariable int goalId,
                                     @PathVariable int idBudget,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) BigDecimal minAmount,
                                     @RequestParam(required = false) BigDecimal maxAmount,
                                     Model model) {
        // Проверка на null или пустое значение для необязательных параметров
        if (minAmount == null) {
            minAmount = BigDecimal.ZERO; // Устанавливаем минимальное значение по умолчанию
        }
        if (maxAmount == null) {
            maxAmount = BigDecimal.valueOf(Double.MAX_VALUE); // Максимальное значение по умолчанию
        }

        // Логика поиска
        List<Transaction> transactions = transactionService.getTransactionsByCategoryAndAmount(
                category != null && !category.isEmpty() ? category : null,
                minAmount,
                maxAmount
        );

        model.addAttribute("transactions", transactions);
        model.addAttribute("goalId", goalId);
        model.addAttribute("idBudget", idBudget);
        return "transactions";
    }
    @GetMapping("/{goalId}/{idBudget}/{idCategory}/search")
    public String searchTransactions(@PathVariable int goalId,
                                     @PathVariable int idBudget,
                                     @PathVariable int idCategory,
                                     @RequestParam String transactionType,
                                     @RequestParam(required = false) BigDecimal minAmount,
                                     @RequestParam(required = false) BigDecimal maxAmount,
                                     Model model) {
        // Устанавливаем значения по умолчанию для диапазона суммы, если они отсутствуют
        if (minAmount == null) {
            minAmount = BigDecimal.ZERO;
        }
        if (maxAmount == null) {
            maxAmount = BigDecimal.valueOf(Double.MAX_VALUE);
        }

        // Получение результатов поиска
        List<Transaction> transactions = transactionService.getTransactionsByTypeAndAmount(transactionType, minAmount, maxAmount);

        BigDecimal totalTransactions = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("transactions", transactions);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("goalId", goalId);
        model.addAttribute("idBudget", idBudget);
        model.addAttribute("idCategory", idCategory);
        model.addAttribute("transactionType", transactionType);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);

        return "transactions";
    }


    @GetMapping("/{goalId}/{idBudget}/{idCategory}/transactions")
    public String listTransactions(@PathVariable int goalId,
                                   @PathVariable int idBudget,
                                   @PathVariable int idCategory,
                                   Model model) {
        String categoryName = categoryService.getCategoryNameById(idCategory);
        List<Transaction> transactions = transactionService.getTransactionsByCategoryId(categoryName);

        BigDecimal totalTransactions = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        transactions.forEach(transaction -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            transaction.setFormattedDateTime(transaction.getDateTime().format(formatter));
        });

        model.addAttribute("transactions", transactions);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("goalId", goalId);
        model.addAttribute("idBudget", idBudget);
        model.addAttribute("idCategory", idCategory);
        model.addAttribute("transactionType", ""); // Добавлено
        return "transactions";
    }

    @PostMapping("/{goalId}/{idBudget}/{idCategory}/add-transaction")
    public String addTransaction(@PathVariable int goalId,
                                 @PathVariable int idBudget,
                                 @PathVariable int idCategory,
                                 @ModelAttribute Transaction transaction,
                                 @RequestParam String dateTime,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        transaction.setIdUser(user.getId());
        transaction.setIdBudget(idBudget);
        transaction.setIdGoal(goalId);
        transaction.setCategory(categoryService.getCategoryNameById(idCategory));
        transaction.setDateTime(LocalDateTime.parse(dateTime));
        transactionService.addTransaction(transaction);
        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/" + idCategory + "/transactions";
    }

    @PostMapping("/{goalId}/{idBudget}/{idCategory}/update-transaction")
    public String updateTransaction(@PathVariable int goalId, @PathVariable int idBudget,
                                    @PathVariable int idCategory,
                                    @ModelAttribute Transaction transaction,
                                    @RequestParam String dateTime,
                                    @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        transaction.setIdUser(user.getId());
        transaction.setCategory(categoryService.getCategoryNameById(idCategory));
        transaction.setDateTime(LocalDateTime.parse(dateTime));
        transactionService.updateTransaction(transaction);
        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/" + idCategory + "/transactions";
    }


    @PostMapping("/{goalId}/{idBudget}/{idCategory}/delete-transaction")
    public String deleteTransaction(@PathVariable int goalId, @PathVariable int idBudget,
                                    @PathVariable int idCategory,
                                    @RequestParam int idTransaction,
                                    @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        transactionService.deleteTransaction(idTransaction, user.getId());
        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/" + idCategory + "/transactions";
    }

    @GetMapping("/budget-plans/{budgetPlanId}/total-transactions")
    public String getTotalTransactionsForBudgetPlan(@PathVariable Long budgetPlanId, Model model) {
        BigDecimal totalTransactions = budgetPlanService.calculateTotalTransactionsForBudgetPlan(budgetPlanId);
        model.addAttribute("totalTransactions", totalTransactions);
        return "budget-plans";
    }
    @GetMapping
    public String getDefaultBudgetPlansPage() {
        return "redirect:/goals"; // Или другое действие
    }
    @PostMapping("/notifications/mark-read")
    @ResponseBody
    public ResponseEntity<?> markNotificationsAsRead(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        notificationDAO.markAllAsReadForUser(user.getId());
        return ResponseEntity.ok().build();
    }

}
