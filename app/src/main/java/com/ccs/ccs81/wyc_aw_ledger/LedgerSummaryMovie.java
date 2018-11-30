package com.ccs.ccs81.wyc_aw_ledger;

/**
 * Created by Banke Bihari on 10/5/2016.
 */

public class LedgerSummaryMovie extends BWODetail {
    private String vc_Type,vc_BlivNo,vc_VouchDate,vc_DrAmt,vc_CrAmt;

    public LedgerSummaryMovie() {
    }

    public LedgerSummaryMovie(String vc_Type, String vc_BlivNo, String vc_VouchDate, String vc_DrAmt, String vc_CrAmt) {
        this.vc_Type = vc_Type;
        this.vc_BlivNo = vc_BlivNo;
        this.vc_VouchDate = vc_VouchDate;
        this.vc_DrAmt = vc_DrAmt;
        this.vc_CrAmt = vc_CrAmt;

    }

    public String getVc_Type() {
        return vc_Type;
    }
    public void setVc_Type(String vc_Type) {
        this.vc_Type = vc_Type;
    }


    public String getVc_BlivNo() {
        String var = String.valueOf(vc_BlivNo);
        return var;
    }
    public void setVc_BlivNo(String vc_BlivNo) {
        this.vc_BlivNo = vc_BlivNo;
    }


    public String getVc_VouchDate() {
        String var = String.valueOf(vc_VouchDate);
        return var;
    }
    public void setVc_VouchDate(String vc_VouchDate) {
        this.vc_VouchDate = vc_VouchDate;
    }


    public String getVc_DrAmt() {
        return vc_DrAmt;
    }
    public void setVc_DrAmt(String vc_DrAmt) {
        this.vc_DrAmt = vc_DrAmt;
    }



    public String getVc_CrAmt() {
        return vc_CrAmt;
    }
    public void setVc_CrAmt(String vc_CrAmt) {
        this.vc_CrAmt = vc_CrAmt;
    }


}
