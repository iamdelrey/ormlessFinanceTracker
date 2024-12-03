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

    public List<Category> getCategoriesByBudgetId(int idBudget) {
        return categoryDAO.findAllByBudgetId(idBudget);
    }

    public void addCategory(Category category) {
        categoryDAO.save(category);
    }

    public void deleteCategory(int idCategory, int idUser) {
        categoryDAO.delete(idCategory, idUser);
    }
}
