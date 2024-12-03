package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.GoalDAO;
import rbd.ormless.financetracker.model.Goal;

import java.util.List;

@Service
public class GoalService {
    private final GoalDAO goalDAO;

    public GoalService(GoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    public List<Goal> getGoalsByAccountId(int accountId) {
        return goalDAO.findAllByAccountId(accountId);
    }

    public void addGoal(Goal goal) {
        goalDAO.save(goal);
    }

    public void updateGoal(Goal goal) {
        goalDAO.update(goal);
    }

    public void deleteGoal(int goalId, int accountId) {
        goalDAO.delete(goalId, accountId);
    }

    public Goal getGoalById(int goalId) {
        return goalDAO.findById(goalId);
    }

}
