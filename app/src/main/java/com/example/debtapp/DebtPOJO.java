package com.example.debtapp;

public class DebtPOJO {
    private String mLender;
    private String mBorrower;
    private String mDescription;
    private double mAmount;
    private boolean mStatus;
    private String mAddress;

    public DebtPOJO() {
    }

    public DebtPOJO(String lender, String borrower, String description, double amount, boolean status) {
        mLender = lender;
        mBorrower = borrower;
        mDescription = description;
        mAmount = amount;
        mStatus = status;
    }

    public String getLender() {
        return mLender;
    }

    public void setLender(String lender) {
        mLender = lender;
    }

    public String getBorrower() {
        return mBorrower;
    }

    public void setBorrower(String borrower) {
        mBorrower = borrower;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    public boolean isSettled() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
