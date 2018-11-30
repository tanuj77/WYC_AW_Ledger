package com.ccs.ccs81.wyc_aw_ledger;

/**
 * Created by Banke Bihari on 10/5/2016.
 */

public class LedgerDetailMovie {
    private String voucherType,voucherNumber,voucherDate,billNo,billDate,number,date,debitAmount,creditAmount,closingBalance,openingBalance,vcDescription;

//    public LedgerDetailMovie(String resVoucherType, String resVoucherNumber, String resVoucherDate, String resBillNo, String resBillDate, String resNumber, String resDate, String resDebitAmount, String resCreditAmount, String resClosingBalance, String resOpening, String resVcDescription) {
//    }

    public LedgerDetailMovie(String voucherType, String voucherNumber, String voucherDate, String billNo, String billDate, String number,String date, String debitAmount, String creditAmount, String closingBalance, String openingBalance ,String vcDescription) {
        this.voucherType = voucherType;
        this.voucherNumber = voucherNumber;
        this.voucherDate = voucherDate;
        this.billNo = billNo;
        this.billDate = billDate;
        this.number = number;
        this.date=date;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.closingBalance= closingBalance;
        this.openingBalance=openingBalance;
        this.vcDescription=vcDescription;
    }

    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }


    public String getVoucherNumber() {
        return voucherNumber;
    }
    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }


    public String getVoucherDate() {
        String var = String.valueOf(voucherDate);
        return var;
    }
    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }


    public String getBillNo() {
        String var = String.valueOf(billNo);
        return var;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }


    public String getBillDate() {
        return billDate;
    }
    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }



    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
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



    public String getClosingBalance() {
        return closingBalance;
    }
    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }
    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getVcDescription() {
        return vcDescription;
    }
    public void setVcDescription(String vcDescription) {
        this.vcDescription = vcDescription;
    }
}
