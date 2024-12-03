package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.Goal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GoalDAO {
    private final JdbcTemplate jdbcTemplate;

    public GoalDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Goal> findAllByAccountId(int accountId) {
        String sql = "SELECT * FROM \"Goal\" WHERE id_account = ?";
        return jdbcTemplate.query(sql, new Object[]{accountId}, this::mapRowToGoal);
    }

    public void save(Goal goal) {
        String sql = "INSERT INTO \"Goal\" (id_user, id_account, goal_name, goal_amount) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, goal.getIdUser(), goal.getIdAccount(), goal.getGoalName(), goal.getGoalAmount());
    }

    public void update(Goal goal) {
        String sql = "UPDATE \"Goal\" SET goal_name = ?, goal_amount = ? WHERE id_goal = ? AND id_account = ? AND id_user = ?";
        jdbcTemplate.update(sql, goal.getGoalName(), goal.getGoalAmount(), goal.getIdGoal(), goal.getIdAccount(), goal.getIdUser());
    }

    public void delete(int goalId, int accountId) {
        String sql = "DELETE FROM \"Goal\" WHERE id_goal = ? AND id_account = ?";
        jdbcTemplate.update(sql, goalId, accountId);
    }

    public Goal findById(int goalId) {
        String sql = "SELECT * FROM \"Goal\" WHERE id_goal = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{goalId}, this::mapRowToGoal);
    }

    private Goal mapRowToGoal(ResultSet rs, int rowNum) throws SQLException {
        Goal goal = new Goal();
        goal.setIdGoal(rs.getInt("id_goal"));
        goal.setIdUser(rs.getInt("id_user"));
        goal.setIdAccount(rs.getInt("id_account"));
        goal.setGoalName(rs.getString("goal_name"));
        goal.setGoalAmount(rs.getInt("goal_amount"));
        return goal;
    }
}
