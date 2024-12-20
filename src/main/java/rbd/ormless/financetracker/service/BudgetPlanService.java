package rbd.ormless.financetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.BudgetPlanDAO;
import rbd.ormless.financetracker.dao.NotificationDAO;
import rbd.ormless.financetracker.dao.TransactionDAO;
import rbd.ormless.financetracker.model.BudgetPlan;
import rbd.ormless.financetracker.model.Notification;
import rbd.ormless.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BudgetPlanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final BudgetPlanDAO budgetPlanDAO;
    private final TransactionDAO transactionDAO;
    private final NotificationDAO notificationDAO;
    private final AccountService accountService;

    public BudgetPlanService(BudgetPlanDAO budgetPlanDAO, TransactionDAO transactionDAO, NotificationDAO notificationDAO, AccountService accountService) {
        this.budgetPlanDAO = budgetPlanDAO;
        this.transactionDAO = transactionDAO;
        this.notificationDAO = notificationDAO;
        this.accountService = accountService;
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

    public void generateNotifications(int userId) {
        List<BudgetPlan> plans = budgetPlanDAO.findByUserId(userId);
        for (BudgetPlan plan : plans) {
            if (plan.getPlanAmount().compareTo(BigDecimal.ZERO) < 0) {
                notificationDAO.save(new Notification(
                        "Баланс плана бюджета '" + plan.getPlanName() + "' отрицательный!",
                        LocalDateTime.now(),
                        "Непрочитано",
                        userId
                ));
            }
        }
    }

}
