/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

/**
 *
 * @author Elliot
 */
public class DetailsAccountDTO {

    private String accountId;
    private String accountNumber;
    private String displayAccountNumber;
    private String accountType;
    private String currencyCode;
    private String productCode;
    private String status;
    private String sucursal;
    private String openedDate;

    public DetailsAccountDTO() {
    }

    public DetailsAccountDTO(String accountId, String accountNumber, String displayAccountNumber, String accountType, String currencyCode, String productCode, String status, String sucursal, String openedDate) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.displayAccountNumber = displayAccountNumber;
        this.accountType = accountType;
        this.currencyCode = currencyCode;
        this.productCode = productCode;
        this.status = status;
        this.sucursal = sucursal;
        this.openedDate = openedDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDisplayAccountNumber() {
        return displayAccountNumber;
    }

    public void setDisplayAccountNumber(String displayAccountNumber) {
        this.displayAccountNumber = displayAccountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(String openedDate) {
        this.openedDate = openedDate;
    }

    @Override
    public String toString() {
        return "DetailsAccountDTO{" + "accountId=" + accountId + ", accountNumber=" + accountNumber + ", displayAccountNumber=" + displayAccountNumber + ", accountType=" + accountType + ", currencyCode=" + currencyCode + ", productCode=" + productCode + ", status=" + status + ", sucursal=" + sucursal + ", openedDate=" + openedDate + '}';
    }

}
