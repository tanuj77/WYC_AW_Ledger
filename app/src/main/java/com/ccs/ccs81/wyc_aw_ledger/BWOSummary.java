package com.ccs.ccs81.wyc_aw_ledger;

import javax.xml.transform.sax.SAXResult;

/**
 * Created by Banke Bihari on 10/5/2016.
 */

public class BWOSummary {
    private String billNo, billDate, noOfDays,passenger, billAmount, amountRec, cnAmount, billDue, balance;

    public BWOSummary() {
    }

    public BWOSummary(String billNo, String billDate, String noOfDays, String billAmount, String amountRec, String cnAmount, String billDue, String balance, String passenger) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.noOfDays = noOfDays;
        this.billAmount = billAmount;
        this.amountRec = amountRec;
        this.cnAmount = cnAmount;
        this.billDue = billDue;
        this.balance = balance;
        this.passenger = passenger;

    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }


    public String getBillDate() {
        String var = String.valueOf(billDate);
        return var;
    }

    public String getNoOfDays(){return noOfDays;}
    public void setNoOfDays(String noOfDays){
        this.noOfDays = noOfDays;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }


    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }


    public String getAmountRec() {
        return amountRec;
    }

    public void setAmountRec(String amountRec) {
        this.amountRec = amountRec;
    }

    public String getCnAmount() {
        return cnAmount;
    }

    public void setCnAmount(String cnAmount) {
        this.cnAmount = cnAmount;
    }


    public String getBillDue() {
        String var = String.valueOf(billDue);
        return var;
    }

    public void setBillDue(String billDue) {
        this.billDue = billDue;
    }


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPassenger() {
        String var = String.valueOf(passenger);
        return var;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

}
