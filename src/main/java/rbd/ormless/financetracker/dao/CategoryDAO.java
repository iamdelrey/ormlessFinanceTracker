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

    public List<Category> findAllByBudgetId(int idBudget) {
        String sql = "SELECT * FROM \"Category\" WHERE id_budget = ?";
        return jdbcTemplate.query(sql, new Object[]{idBudget}, this::mapRowToCategory);
    }

    public void save(Category category) {
        String sql = "INSERT INTO \"Category\" (id_user, id_budget, id_goal, category_name) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, category.getIdUser(), category.getIdBudget(), category.getIdGoal(), category.getCategoryName());
    }

    public void delete(int idCategory, int idUser) {
        String sql = "DELETE FROM \"Category\" WHERE id_category = ? AND id_user = ?";
        jdbcTemplate.update(sql, idCategory, idUser);
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
