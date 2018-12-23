package com.example.user.tourmate;

public class ExpenseClass {
    private String expenseDetails;
    private String expenseAmount;

    public ExpenseClass() {
    }

    public ExpenseClass(String expenseDetails, String expenseAmount) {
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDetails() {
        return expenseDetails;
    }

    public void setExpenseDetails(String expenseDetails) {
        this.expenseDetails = expenseDetails;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
}
