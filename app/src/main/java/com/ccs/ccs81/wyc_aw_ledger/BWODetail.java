package com.ccs.ccs81.wyc_aw_ledger;

/**
 * Created by Banke Bihari on 10/5/2016.
 */

public class BWODetail {
    private String invoiceNo, invoiceDate, tranType, receiptNo, receiptDate, noOfDays,chequeNo, detailOfService, debitAmount, creditAmount, balanceAmount, opening, vcDescription;

    public BWODetail() {
    }

    public BWODetail(String invoiceNo, String invoiceDate, String tranType, String receiptNo, String receiptDate,String noOfDays ,String chequeNo, String debitAmount, String creditAmount, String balanceAmount, String detailOfService) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.tranType = tranType;
        this.receiptNo = receiptNo;
        this.receiptDate = receiptDate;
        this.noOfDays = noOfDays;
        this.chequeNo = chequeNo;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.balanceAmount = balanceAmount;
        this.detailOfService = detailOfService;

    }

    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }


    public String getInvoiceDate() {
        String var = String.valueOf(invoiceDate);
        return var;  }
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }


    public String getTranType() {
        String var = String.valueOf(tranType);
        return var; }
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }


    public String getReceiptNo() {
        return receiptNo;
    }
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }


    public String getReceiptDate() { return receiptDate;  }
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getNoOfDays() { return noOfDays;  }
    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }


    public String getChequeNo() {
        return chequeNo;
    }
    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }


    public String getDebitAmount() {
        return debitAmount;
    }
    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }


    public String getCreditAmount() {
        String var = String.valueOf(creditAmount);
        return var;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }


    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }


    public String getDetailOfService() {
        String var = String.valueOf(detailOfService);
        return var;
    }

    public void setDetailOfService(String detailOfService) {
        this.detailOfService = detailOfService;
    }



}
