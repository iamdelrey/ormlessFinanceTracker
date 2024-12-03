package rbd.ormless.financetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rbd.ormless.financetracker.model.BudgetPlan;
import rbd.ormless.financetracker.model.Category;
import rbd.ormless.financetracker.model.User;
import rbd.ormless.financetracker.service.BudgetPlanService;
import rbd.ormless.financetracker.service.CategoryService;
import rbd.ormless.financetracker.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/budget-plans")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;
    private final UserService userService;
    private final CategoryService categoryService;

    public BudgetPlanController(BudgetPlanService budgetPlanService, UserService userService, CategoryService categoryService) {
        this.budgetPlanService = budgetPlanService;
        this.userService = userService;
        this.categoryService = categoryService;
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

    @PostMapping("/{goalId}/{idBudget}/delete-category")
    public String deleteCategory(@PathVariable int goalId, @PathVariable int idBudget,
                                 @RequestParam int idCategory,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        User user = userService.findByEmail(springUser.getUsername());
        categoryService.deleteCategory(idCategory, user.getId());
        return "redirect:/budget-plans/" + goalId + "/" + idBudget + "/details";
    }
}
