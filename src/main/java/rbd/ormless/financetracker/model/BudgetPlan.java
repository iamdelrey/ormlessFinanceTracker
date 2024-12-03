package rbd.ormless.financetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetPlan {
    private Integer idBudget;
    private Integer idUser;
    private String planName;
    private BigDecimal planAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer idGoal;
    private String planDetails;


    public Integer getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(Integer idBudget) {
        this.idBudget = idBudget;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }


    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getIdGoal() {
        return idGoal;
    }

    public void setIdGoal(Integer idGoal) {
        this.idGoal = idGoal;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(String planDetails) {
        this.planDetails = planDetails;
    }
}
