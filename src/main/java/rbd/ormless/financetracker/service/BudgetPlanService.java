package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.BudgetPlanDAO;
import rbd.ormless.financetracker.model.BudgetPlan;

import java.util.List;

@Service
public class BudgetPlanService {
    private final BudgetPlanDAO budgetPlanDAO;

    public BudgetPlanService(BudgetPlanDAO budgetPlanDAO) {
        this.budgetPlanDAO = budgetPlanDAO;
    }

    public List<BudgetPlan> getBudgetPlansByGoalId(int goalId) {
        return budgetPlanDAO.findAllByGoalId(goalId);
    }

    public void addBudgetPlan(BudgetPlan budgetPlan) {
        budgetPlanDAO.save(budgetPlan);
    }

    public void updateBudgetPlan(BudgetPlan budgetPlan) {
        budgetPlanDAO.update(budgetPlan);
    }

    public void deleteBudgetPlan(int idBudget) {
        budgetPlanDAO.delete(idBudget);
    }

    public int getAccountIdByGoalId(int goalId) {
        return budgetPlanDAO.findAccountIdByGoalId(goalId);
    }

    public BudgetPlan getBudgetPlanById(int idBudget) {
        return budgetPlanDAO.findById(idBudget);
    }
}
