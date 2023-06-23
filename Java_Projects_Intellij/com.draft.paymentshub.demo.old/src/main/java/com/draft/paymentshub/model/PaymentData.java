package com.draft.paymentshub.model;

public class PaymentData {
    private CreditCardData CreditCardData;

    public PaymentData() {
        // Default constructor
    }

    public CreditCardData getCreditCardData() {
        return CreditCardData;
    }

    public void setCreditCardData(CreditCardData creditCardData) {
        this.CreditCardData = creditCardData;
    }

    public static class CreditCardData {
        private String accountNumber;
        private String expirationDate;
        private String cvv;
        private String firstName;
        private String lastName;
        private String postalCode;
        private String streetAddress;

        public CreditCardData() {
            // Default constructor
        }

        public CreditCardData(String accountNumber, String expirationDate, String cvv, String firstName, String lastName, String postalCode, String streetAddress) {
            this.accountNumber = accountNumber;
            this.expirationDate = expirationDate;
            this.cvv = cvv;
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
            this.streetAddress = streetAddress;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }
    }
}
