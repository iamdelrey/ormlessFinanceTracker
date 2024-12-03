package rbd.ormless.financetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.BudgetPlanDAO;
import rbd.ormless.financetracker.dao.TransactionDAO;
import rbd.ormless.financetracker.model.BudgetPlan;
import rbd.ormless.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetPlanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final BudgetPlanDAO budgetPlanDAO;
    private final TransactionDAO transactionDAO;

    public BudgetPlanService(BudgetPlanDAO budgetPlanDAO, TransactionDAO transactionDAO) {
        this.budgetPlanDAO = budgetPlanDAO;
        this.transactionDAO = transactionDAO;
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

    public BigDecimal calculateTotalBudgetForGoal(Long goalId) {
        return budgetPlanDAO.findByGoalId(Math.toIntExact(goalId))
                .stream()
                .map(BudgetPlan::getPlanAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalTransactionsForBudgetPlan(Long budgetPlanId) {
        return transactionDAO.findByBudgetPlanId(budgetPlanId)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
