package com.draft.paymentshub.model;

public class SubscriptionData {
    private double amount;
    private String frequency;
    private String billingDate;
    private String failureOption;
    private int numberOfPayments;
    private int retries;
    private String description;

    public SubscriptionData() {
        // Default constructor
    }

    public SubscriptionData(double amount, String frequency, String billingDate, String failureOption, int numberOfPayments, int retries, String description) {
        this.amount = amount;
        this.frequency = frequency;
        this.billingDate = billingDate;
        this.failureOption = failureOption;
        this.numberOfPayments = numberOfPayments;
        this.retries = retries;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getFailureOption() {
        return failureOption;
    }

    public void setFailureOption(String failureOption) {
        this.failureOption = failureOption;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

