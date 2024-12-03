package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.CategoryDAO;
import rbd.ormless.financetracker.model.Category;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getCategoriesByBudgetId(int budgetId) {
        return categoryDAO.findByBudgetId(budgetId);
    }

    public void addCategory(Category category) {
        categoryDAO.addCategory(category);
    }

    public void updateCategory(Category category) {
        categoryDAO.updateCategory(category);
    }

    public String getCategoryNameById(int idCategory) {
        return categoryDAO.findCategoryNameById(idCategory);
    }

    public void deleteCategory(int idCategory, int idUser) {
        categoryDAO.deleteCategory(idCategory, idUser);
    }
}
