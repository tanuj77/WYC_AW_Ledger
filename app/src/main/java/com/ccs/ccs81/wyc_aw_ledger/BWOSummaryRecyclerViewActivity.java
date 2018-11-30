package com.ccs.ccs81.wyc_aw_ledger;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.DateTimePatternGenerator;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Banke Bihari on 10/6/2016.
 */
public class BWOSummaryRecyclerViewActivity extends AppCompatActivity {
    private List<BWOSummary> bwoSummaryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BWOCustomAdapterSummary mAdapter;
    Double totalBillAmount = 0.0,totalCnAmount = 0.0,totalBalance = 0.0,totalBillDue = 0.0,totalAmountRec = 0.0,totalNoOfDays = 0.0;
    TextView textViewTotalNetAmount, textViewTotalMonthWiseSale, textViewTotalYearWiseSale;
    Boolean runTot = false;
    ConnectionDetector cd;
    String cpCompanyUrl, cpCompanyID, sInvoiceNo, sType, scpMobileNo, scpEmailId, partyCode,startDate,endDate;
    String resBillNo;
    String resBillDate;
    String resPassenger;
    String resBillAmount;
    String resCnAmount;
    String resAmountRec;
    String resBillDue;
    String resBalance;
    String cbPax="";
    String cbCustomer="";
    String cbSales="";
    String format="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bwosummaryrecylerview);

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Internet Connection Error");

            // Setting Dialog Message
            alertDialog.setMessage("Please connect to working Internet connection");
            // Setting alert dialog icon
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BWOSummaryRecyclerViewActivity.this.finish();
                }
            });
            // Showing Alert Message
            alertDialog.show();

            return;
        }

        cpCompanyUrl = getIntent().getStringExtra("cpCompanyUrl");
        cpCompanyID = getIntent().getStringExtra("cpCompanyID");
        scpMobileNo = getIntent().getStringExtra("cpMobileNo");
        scpEmailId = getIntent().getStringExtra("cpEmail");
        cbPax = getIntent().getStringExtra("pax");
        cbCustomer=getIntent().getStringExtra("customer");
        cbSales=getIntent().getStringExtra("sales");
        partyCode = getIntent().getStringExtra("cpPartyCode");
        startDate=getIntent().getStringExtra("startDate");
        endDate=getIntent().getStringExtra("endDate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BWO "+partyCode+"  "+ startDate+"    " +endDate);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter = new BWOCustomAdapterSummary(bwoSummaryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onLongClick(View child, int childPosition) {
                BWOSummary movie = (BWOSummary) bwoSummaryList.get(childPosition);

            }

            @Override
            public void onClick(View view, int childPosition) {
                view.setBackgroundResource(R.color.LightBlue);

                final BWOSummary movie = (BWOSummary) bwoSummaryList.get(childPosition);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BWOSummaryRecyclerViewActivity.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("You selected " + movie.getBillNo() + " and " + movie.getPassenger() + " for email");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new emailInfoAsync().execute();
                        sInvoiceNo = movie.getBillNo();
                        // sType = movie.getInvoiceNo();
                        sType = "AIR";

                        if (sType.equalsIgnoreCase("AIR") || sType.equals("AIR") || sType == "AIR") {
                            format = "GST";
                        } else {
                            format = "CNK";
                        }
                        if (sType.trim().equalsIgnoreCase("CRV") || sType.equals("CRV") || sType.equals("CRV")){
                            Toast.makeText(BWOSummaryRecyclerViewActivity.this,"No invoice for this CRV voucher type",Toast.LENGTH_LONG).show();
                        }
                        if (sType.trim().equalsIgnoreCase("BRV") || sType.equals("BRV") || sType.equals("BRV")){
                            Toast.makeText(BWOSummaryRecyclerViewActivity.this,"No invoice for this BRV voucher type",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }
        }));

        prepareMovieData();

        textViewTotalNetAmount = (TextView) findViewById(R.id.tv_toatalnetamount);
        textViewTotalMonthWiseSale = (TextView) findViewById(R.id.tv_totalmonthwisesale);
        textViewTotalYearWiseSale = (TextView) findViewById(R.id.tv_totalyearwisesale);

    }

    String check = "check";

    public void prepareMovieData() {
        String getintent = getIntent().getStringExtra("webserviceresponse");
        String string = getintent;
        String[] parts = string.split("@%");

        String[] response0 = parts[0].split(",");
        String[] response01 = parts[1].split(",");
        String[] response02 = parts[2].split(",");
        String[] response03 = parts[3].split(",");
        String[] response04 = parts[4].split(",");
        String[] response05 = parts[5].split(",");
        String[] response06 = parts[6].split(",");
        String[] response07 = parts[7].split(",");

        for (int i = 0; i < response0.length; i++) {
            if(i==0){
                resBillNo = response0[i];
                resBillNo = resBillNo.replace("[", " ");
                resBillNo = resBillNo.replace("]", " ");

                resBillDate = response01[i];
                resBillDate = resBillDate.replace("[", "  ");
                resBillDate = resBillDate.replace("]", " ");

                resPassenger = response02[i];
                resPassenger = resPassenger.replace("[", " ");
                resPassenger = resPassenger.replace("]", " ");

                resBillAmount = response03[i];
                resBillAmount = resBillAmount.replace("[", " ");
                resBillAmount = resBillAmount.replace("]", " ");

                resAmountRec = response04[i];
                resAmountRec = resAmountRec.replace("[", "");
                resAmountRec = resAmountRec.replace("]", "");

                resCnAmount = response05[i];
                resCnAmount = resCnAmount.replace("[", "");
                resCnAmount = resCnAmount.replace("]", "");

                resBillDue = response06[i];
                resBillDue = resBillDue.replace("[", "");
                resBillDue = resBillDue.replace("]", "");

                resBalance = response07[i];
                resBalance = resBalance.replace("[", "");
                resBalance = resBalance.replace("]", "");

                BWOSummary summaryMovie = new BWOSummary(resBillNo, resBillDate,"No. of Days", resBillAmount, resAmountRec, resCnAmount, resBillDue, resBalance, resPassenger);
                bwoSummaryList.add(summaryMovie);
            }if (i>0) {

                resBillNo = response0[i];
                resBillNo = resBillNo.replace("[", " ");
                resBillNo = resBillNo.replace("]", " ");

                resBillDate = response01[i];
                resBillDate = resBillDate.replace("[", "  ");
                resBillDate = resBillDate.replace("]", " ");


                String  billDate = resBillDate;
                Date dbillDate = null,dTodaysDate=null;

                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                     dbillDate =  dateFormat.parse(billDate);
                    dTodaysDate = dateFormat.parse(thisDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long noOfDays = (dTodaysDate.getTime() - dbillDate.getTime()) / (1000 * 60 * 60 * 24);
                totalNoOfDays = totalNoOfDays + noOfDays;

                NumberFormat format = new DecimalFormat("###.####");
                String strNoOfDays = format.format(noOfDays);



                resPassenger = response02[i];
                resPassenger = resPassenger.replace("[", " ");
                resPassenger = resPassenger.replace("]", " ");


                resBillAmount = response03[i];
                resBillAmount = resBillAmount.replace("[", " ");
                resBillAmount = resBillAmount.replace("]", " ");
                Double dresBillAmount = Double.parseDouble(resBillAmount);
                totalBillAmount = totalBillAmount + dresBillAmount;

//                if (resBillAmount.trim().equalsIgnoreCase("0.00") || resBillAmount.trim().equals("0.00") || resBillAmount.trim() == "0.00") {
//                    resBillAmount = resBillAmount.replace("0.00", " ");
//                }
//                if (resBillAmount.trim().equalsIgnoreCase("0") || resBillAmount.trim().equals("0") || resBillAmount.trim() == "0") {
//                    resBillAmount = resBillAmount.replace("0", " ");
//                }

                resAmountRec = response04[i];
                resAmountRec = resAmountRec.replace("[", "");
                resAmountRec = resAmountRec.replace("]", "");
                Double dresAmountRec = Double.parseDouble(resAmountRec);
                totalAmountRec = totalAmountRec + dresAmountRec;

//                if (resAmountRec.trim().equalsIgnoreCase("0.00") || resAmountRec.trim().equals("0.00") || resAmountRec.trim() == "0.00") {
//                    resAmountRec = resAmountRec.replace("0.00", " ");
//                }
//                if (resAmountRec.trim().equalsIgnoreCase("0") || resAmountRec.trim().equals("0") || resAmountRec.trim() == "0") {
//                    resAmountRec = resAmountRec.replace("0", " ");
//                }

                resCnAmount = response05[i];
                resCnAmount = resCnAmount.replace("[", "");
                resCnAmount = resCnAmount.replace("]", "");
                Double dresCnAmount = Double.parseDouble(resCnAmount);
                totalCnAmount = totalCnAmount + dresCnAmount;
//                if (resCnAmount.trim().equalsIgnoreCase("0.00") || resCnAmount.trim().equals("0.00") || resCnAmount.trim() == "0.00") {
//                    resCnAmount = resCnAmount.replace("0.00", " ");
//                }
//                if (resCnAmount.trim().equalsIgnoreCase("0") || resCnAmount.trim().equals("0") || resCnAmount.trim() == "0") {
//                    resCnAmount = resCnAmount.replace("0", " ");
//                }

                resBillDue = response06[i];
                resBillDue = resBillDue.replace("[", "");
                resBillDue = resBillDue.replace("]", "");
                Double dresBillDue = Double.parseDouble(resBillDue);
                totalBillDue = totalBillDue + dresBillDue;
//                if (resBillDue.trim().equalsIgnoreCase("0.00") || resBillDue.trim().equals("0.00") || resBillDue.trim() == "0.00") {
//                    resBillDue = resBillDue.replace("0.00", " ");
//                }
//                if (resBillDue.trim().equalsIgnoreCase("0") || resBillDue.trim().equals("0") || resBillDue.trim() == "0") {
//                    resBillDue = resBillDue.replace("0", " ");
//                }

                resBalance = response07[i];
                resBalance = resBalance.replace("[", "");
                resBalance = resBalance.replace("]", "");
                Double dresBalance = Double.parseDouble(resBalance);
                totalBalance = totalBalance + dresBalance;
//                if (resBalance.trim().equalsIgnoreCase("0.00") || resBalance.trim().equals("0.00") || resBalance.trim() == "0.00") {
//                    resBalance = resBalance.replace("0.00", " ");
//                }
//                if (resBalance.trim().equalsIgnoreCase("0") || resBalance.trim().equals("0") || resBalance.trim() == "0") {
//                    resBalance = resBalance.replace("0", " ");
//                }


                BWOSummary summaryMovie = new BWOSummary(resBillNo, resBillDate, strNoOfDays,resBillAmount, resAmountRec, resCnAmount, resBillDue, resBalance, resPassenger);
                bwoSummaryList.add(summaryMovie);
            }
            if (i==(response0.length)-1){
                //closingBalance = openingBalance + totalDebitAmount-totalCreditAmount;
                NumberFormat format = new DecimalFormat("###.####");
                String stringNoOfDays = format.format(totalNoOfDays);
                String stringtotalAmountRec = format.format(totalAmountRec);
                String stringtotalBalance = format.format(totalBalance);
                String stringtotalBillAmount = format.format(totalBillAmount);
                String stringtotalBillDue = format.format(totalBillDue);
                String stringtotalCnAmount = format.format(totalCnAmount);


                BWOSummary summaryMovie = new BWOSummary("Total", "", stringNoOfDays,stringtotalBillAmount, stringtotalAmountRec, stringtotalCnAmount, stringtotalBillDue, stringtotalBalance, "");
                bwoSummaryList.add(summaryMovie);
            }
            if (i==(response0.length)-1){
                //closingBalance = openingBalance + totalDebitAmount-totalCreditAmount;
                NumberFormat format = new DecimalFormat("###.####");

                String stringtotalBillDue = format.format(totalBillDue);



                BWOSummary summaryMovie = new BWOSummary("", "", "","", "Closing Balance",stringtotalBillDue, "", "", "");
                bwoSummaryList.add(summaryMovie);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    class emailInfoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            cpCompanyUrl = getIntent().getStringExtra("cpCompanyUrl");
            cpCompanyID = getIntent().getStringExtra("cpCompanyID");
            scpMobileNo = getIntent().getStringExtra("cpMobileNo");
            scpEmailId = getIntent().getStringExtra("cpEmail");
            cbPax = getIntent().getStringExtra("pax");
            cbCustomer=getIntent().getStringExtra("customer");
            cbSales=getIntent().getStringExtra("sales");
            EmailWebservice emailWebservice = new EmailWebservice();
            emailWebservice.getConvertedWeight(cpCompanyUrl, "AirInvoiceAutoMail", cpCompanyID, sInvoiceNo, sType, scpMobileNo, scpEmailId, format,cbPax,cbCustomer,cbSales);

            return null;
        }
    }
}