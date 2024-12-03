package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.RowMapper;
import rbd.ormless.financetracker.model.BudgetPlan;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetPlanRowMapper implements RowMapper<BudgetPlan> {
    @Override
    public BudgetPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        BudgetPlan budgetPlan = new BudgetPlan();
        budgetPlan.setIdBudget(rs.getInt("id_budget"));
        budgetPlan.setIdUser(rs.getInt("id_user"));
        budgetPlan.setPlanName(rs.getString("plan_name"));
        budgetPlan.setPlanAmount(rs.getBigDecimal("plan_amount"));
        budgetPlan.setStartDate(rs.getDate("start_date").toLocalDate());
        budgetPlan.setEndDate(rs.getDate("end_date").toLocalDate());
        budgetPlan.setIdGoal(rs.getInt("id_goal"));
        budgetPlan.setPlanDetails(rs.getString("plan_details"));
        return budgetPlan;
    }
}
