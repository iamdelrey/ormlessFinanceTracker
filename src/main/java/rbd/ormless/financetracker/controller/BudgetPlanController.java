package rbd.ormless.financetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.model.BudgetPlan;
import rbd.ormless.financetracker.model.Category;
import rbd.ormless.financetracker.model.Transaction;
import rbd.ormless.financetracker.model.User;
import rbd.ormless.financetracker.service.BudgetPlanService;
import rbd.ormless.financetracker.service.CategoryService;
import rbd.ormless.financetracker.service.TransactionService;
import rbd.ormless.financetracker.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/budget-plans")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;


    public BudgetPlanController(BudgetPlanService budgetPlanService, UserService userService, CategoryService categoryService, TransactionService transactionService) {
        this.budgetPlanService = budgetPlanService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{goalId}")
    public String listBudgetPlans(@PathVariable int goalId, Model model) {
        List<BudgetPlan> budgetPlans = budgetPlanService.getBudgetPlansByGoalId(goalId);
        int accountId = budgetPlanService.getAccountIdByGoalId(goalId);

        model.addAttribute("budgetPlans", budgetPlans);
        model.addAttribute("goalId", goalId);
        model.addAttribute("accountId", accountId);
        return "budget-plans";
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
        return "redirect:/budget-plans/" + goalId;
    }

    @PostMapping("/{goalId}/update")
    public String updateBudgetPlan(@PathVariable int goalId, @ModelAttribute BudgetPlan budgetPlan) {
        budgetPlanService.updateBudgetPlan(budgetPlan);
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

    @GetMapping("/{goalId}/{idBudget}/{idCategory}/transactions")
    public String listTransactions(@PathVariable int goalId, @PathVariable int idBudget,
                                   @PathVariable int idCategory, Model model) {
        String categoryName = categoryService.getCategoryNameById(idCategory);
        List<Transaction> transactions = transactionService.getTransactionsByCategoryId(categoryName);

        // Преобразование LocalDateTime в строку
        transactions.forEach(transaction -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            transaction.setFormattedDateTime(transaction.getDateTime().format(formatter));
        });

        model.addAttribute("transactions", transactions);
        model.addAttribute("goalId", goalId);
        model.addAttribute("idBudget", idBudget);
        model.addAttribute("idCategory", idCategory);
        return "transactions";
    }


    @PostMapping("/{goalId}/{idBudget}/{idCategory}/add-transaction")
    public String addTransaction(@PathVariable int goalId, @PathVariable int idBudget,
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
        transaction.setDateTime(LocalDateTime.parse(dateTime)); // Парсинг даты
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


}
