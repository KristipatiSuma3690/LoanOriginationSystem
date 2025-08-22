package com.loan.dto;

public class LoanDecisionDto {
    private String decision; // "APPROVE" or "REJECT"

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
