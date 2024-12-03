package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.BudgetPlan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

@Repository
public class BudgetPlanDAO {
    private final JdbcTemplate jdbcTemplate;

    public BudgetPlanDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BudgetPlan> findAllByGoalId(int goalId) {
        String sql = "SELECT * FROM \"Budget_plan\" WHERE id_goal = ?";
        return jdbcTemplate.query(sql, new Object[]{goalId}, this::mapRowToBudgetPlan);
    }

    public int findAccountIdByGoalId(int goalId) {
        String sql = "SELECT id_account FROM \"Goal\" WHERE id_goal = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{goalId}, Integer.class);
    }


    public void save(BudgetPlan budgetPlan) {
        String sql = "INSERT INTO \"Budget_plan\" (id_user, id_goal, plan_name, plan_amount, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, budgetPlan.getIdUser(), budgetPlan.getIdGoal(), budgetPlan.getPlanName(),
                budgetPlan.getPlanAmount(), budgetPlan.getStartDate(), budgetPlan.getEndDate());
    }

    public void update(BudgetPlan budgetPlan) {
        String sql = "UPDATE \"Budget_plan\" SET plan_name = ?, plan_amount = ?, start_date = ?, end_date = ?, plan_details = ? WHERE id_budget = ?";
        jdbcTemplate.update(sql, budgetPlan.getPlanName(), budgetPlan.getPlanAmount(), budgetPlan.getStartDate(),
                budgetPlan.getEndDate(), budgetPlan.getPlanDetails(), budgetPlan.getIdBudget());
    }

    public void delete(int idBudget) {
        String sql = "DELETE FROM \"Budget_plan\" WHERE id_budget = ?";
        jdbcTemplate.update(sql, idBudget);
    }

    public List<BudgetPlan> findByGoalId(int goalId) {
        String sql = "SELECT * FROM \"Budget_plan\" WHERE id_goal = ?";
        return jdbcTemplate.query(sql, new Object[]{goalId}, this::mapRowToBudgetPlan);
    }

    public BudgetPlan findById(int idBudget) {
        String sql = "SELECT * FROM \"Budget_plan\" WHERE id_budget = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, this::mapRowToBudgetPlan);
    }

    public BigDecimal findTotalPlanAmountByUserId(int userId) {
        String sql = "SELECT COALESCE(SUM(plan_amount), 0) FROM \"Budget_plan\" WHERE id_user = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, BigDecimal.class);
    }

    public List<BudgetPlan> findByUserId(int userId) {
        String sql = "SELECT * FROM \"Budget_plan\" WHERE id_user = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, this::mapRowToBudgetPlan);
    }

    // Маппинг строки из ResultSet в объект BudgetPlan
    private BudgetPlan mapRowToBudgetPlan(ResultSet rs, int rowNum) throws SQLException {
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
