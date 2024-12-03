package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public CategoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> findByBudgetId(int budgetId) {
        String sql = "SELECT * FROM \"Category\" WHERE id_budget = ?";
        return jdbcTemplate.query(sql, new Object[]{budgetId}, this::mapRowToCategory);
    }

    public void addCategory(Category category) {
        String sql = "INSERT INTO \"Category\" (id_user, id_budget, id_goal, category_name) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, category.getIdUser(), category.getIdBudget(), category.getIdGoal(), category.getCategoryName());
    }

    public String findCategoryNameById(int idCategory) {
        String sql = "SELECT category_name FROM \"Category\" WHERE id_category = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idCategory}, String.class);
    }

    public void updateCategory(Category category) {
        String sql = "UPDATE \"Category\" SET category_name = ? WHERE id_category = ?";
        jdbcTemplate.update(sql, category.getCategoryName(), category.getIdCategory());
    }

    public void deleteCategory(int categoryId, int userId) {
        String sql = "DELETE FROM \"Category\" WHERE id_category = ? AND id_user = ?";
        jdbcTemplate.update(sql, categoryId, userId);
    }

    private Category mapRowToCategory(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setIdCategory(rs.getInt("id_category"));
        category.setIdUser(rs.getInt("id_user"));
        category.setCategoryName(rs.getString("category_name"));
        category.setIdBudget(rs.getInt("id_budget"));
        category.setIdGoal(rs.getInt("id_goal"));
        return category;
    }
}
