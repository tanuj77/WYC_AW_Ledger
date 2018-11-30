package com.ccs.ccs81.wyc_aw_ledger;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

/**
 * Created by Banke Bihari on 10/6/2016.
 */
public class BWODetailRecyclerViewActivity extends AppCompatActivity {
    private List<BWODetail> detailMovieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BWOCustomAdapterDetail mAdapter;
    long lresNetAmount = (long) 0.0;

    Double totalDebitAmount = 0.0,totalCreditAmount = 0.0,totalRunAmount = 0.0,closingBalance = 0.0,totalNoOfDays=0.0;
    TextView textViewTotalNetAmount, textViewTotalMonthWiseSale, textViewTotalYearWiseSale;
    Boolean runTot = false;
    ConnectionDetector cd;
    String cpCompanyUrl, cpCompanyID, sInvoiceNo, sType, scpMobileNo, scpEmailId, partyCode,startDate,endDate, cbPax="", cbCustomer="", cbSales="",format="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bwodetailrecyclerview);

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
                    BWODetailRecyclerViewActivity.this.finish();
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

        mAdapter = new BWOCustomAdapterDetail(detailMovieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onLongClick(View child, int childPosition) {
                BWODetail movie = (BWODetail) detailMovieList.get(childPosition);
                Toast.makeText(getApplicationContext(), movie.getChequeNo() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(View view, int childPosition) {
                view.setBackgroundResource(R.color.LightBlue);
                final BWODetail movie = (BWODetail) detailMovieList.get(childPosition);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BWODetailRecyclerViewActivity.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("You selected " + movie.getInvoiceNo() + " and "+movie.getTranType()+" for email");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new emailInfoAsync().execute();
                        sInvoiceNo = movie.getInvoiceNo();
                        sType = movie.getTranType();
                        if (sType.equalsIgnoreCase("AIR") || sType.equals("AIR") || sType == "AIR") {
                            format = "GST";
                        } else {
                            format = "CNK";
                        }
                        if (sType.trim().equalsIgnoreCase("CRV") || sType.equals("CRV") || sType.equals("CRV")){
                            Toast.makeText(BWODetailRecyclerViewActivity.this,"No invoice for this CRV Tansaction type",Toast.LENGTH_LONG).show();
                        }
                        if (sType.trim().equalsIgnoreCase("BRV") || sType.equals("BRV") || sType.equals("BRV")){
                            Toast.makeText(BWODetailRecyclerViewActivity.this,"No invoice for this BRV Tansaction type",Toast.LENGTH_LONG).show();
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


        textViewTotalNetAmount = (TextView) findViewById(R.id.tv_toatalnetamount);
        textViewTotalMonthWiseSale = (TextView) findViewById(R.id.tv_totalmonthwisesale);
        textViewTotalYearWiseSale = (TextView) findViewById(R.id.tv_totalyearwisesale);

        prepareMovieData();
    }


    String check = "check";

    public void prepareMovieData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("BWODetailWebserviceResponse", MODE_PRIVATE);
        String sp = sharedPreferences.getString("BWODetailResponse", null);
        String string = sp;
        String[] parts = string.split("@%");

        String[] response0 = parts[0].split(",");
        String[] response01 = parts[1].split(",");
        String[] response02 = parts[2].split(",");
        String[] response03 = parts[3].split(",");
        String[] response04 = parts[4].split(",");
        String[] response05 = parts[5].split(",");
        String[] response06 = parts[6].split(",");
        String[] response07 = parts[7].split(",");
        String[] response08 = parts[8].split(",");
        String[] response09 = parts[9].split(",");


        for (int i = 0; i < response0.length; i++) {
            if(i==0){
                String resInvoiceNo = response0[i];
                resInvoiceNo = resInvoiceNo.replace("[", " ");

                String resInvoiceDate = response01[i];
                resInvoiceDate = resInvoiceDate.replace("[", "  ");

                String resTranType = response02[i];
                resTranType = resTranType.replace("[", " ");

                String resReceiptNo = response03[i];
                resReceiptNo = resReceiptNo.replace("[", " ");

                String resReceiptDate = response04[i];
                resReceiptDate = resReceiptDate.replace("[", "");

                String resChequeNo = response05[i];
                resChequeNo = resChequeNo.replace("[", "");

                String resDebitAmount = response06[i];
                resDebitAmount = resDebitAmount.replace("[", "");

                String resCreditAmount = response07[i];
                resCreditAmount = resCreditAmount.replace("[", "");

                String resRunAmnt = response08[i];
                resRunAmnt = resRunAmnt.replace("[", "");

                String resDetailsOfService = response09[i];
                resDetailsOfService = resDetailsOfService.replace("[", "");

                BWODetail detailMovie = new BWODetail(resInvoiceNo, resInvoiceDate, resTranType, resReceiptNo, resReceiptDate, "No. Of Days",resChequeNo, resDebitAmount, resCreditAmount, resRunAmnt, resDetailsOfService);
                detailMovieList.add(detailMovie);

            }else if (i>0) {
                String resInvoiceNo = response0[i];
                resInvoiceNo = resInvoiceNo.replace("[", " ");
                resInvoiceNo = resInvoiceNo.replace("]", " ");

                String resInvoiceDate = response01[i];
                resInvoiceDate = resInvoiceDate.replace("[", "  ");
                resInvoiceDate = resInvoiceDate.replace("]", " ");
                // dresYqtax = Double.valueOf(resYqtax.trim()).longValue();


                String resTranType = response02[i];
                resTranType = resTranType.replace("[", " ");
                resTranType = resTranType.replace("]", " ");


                String resReceiptNo = response03[i];
                resReceiptNo = resReceiptNo.replace("[", " ");
                resReceiptNo = resReceiptNo.replace("]", " ");
                //  lresMonthWiseSale = Double.valueOf(resMonthWiseSale.trim()).longValue();
                // TotalMonthWiseSale = TotalMonthWiseSale + lresMonthWiseSale;

                String resReceiptDate = response04[i];
                resReceiptDate = resReceiptDate.replace("[", "");
                resReceiptDate = resReceiptDate.replace("]", "");
//            lresYearWiseSale = Double.valueOf(resYearWiseSale).longValue();
//            TotalYearWiseSale = TotalYearWiseSale + lresYearWiseSale;

                String  billDate = resReceiptDate;
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

                String resChequeNo = response05[i];
                resChequeNo = resChequeNo.replace("[", "");
                resChequeNo = resChequeNo.replace("]", "");


                String resDebitAmount = response06[i];
                resDebitAmount = resDebitAmount.replace("[", "");
                resDebitAmount = resDebitAmount.replace("]", "");
                Double dresDebitAmount = Double.parseDouble(resDebitAmount);
                totalDebitAmount = totalDebitAmount+ dresDebitAmount;
//                if (resDebitAmount.trim().equalsIgnoreCase("0.00") || resDebitAmount.trim().equals("0.00") || resDebitAmount.trim() == "0.00") {
//                    resDebitAmount = resDebitAmount.replace("0.00", " ");
//                }
//                if (resDebitAmount.trim().equalsIgnoreCase("0") || resDebitAmount.trim().equals("0") || resDebitAmount.trim() == "0") {
//                    resDebitAmount = resDebitAmount.replace("0", " ");
//                }

                String resCreditAmount = response07[i];
                resCreditAmount = resCreditAmount.replace("[", "");
                resCreditAmount = resCreditAmount.replace("]", "");
                Double dresCreditAmount = Double.parseDouble(resCreditAmount);
                totalCreditAmount = totalCreditAmount + dresCreditAmount;
//                if (resCreditAmount.trim().equalsIgnoreCase("0.00") || resCreditAmount.trim().equals("0.00") || resCreditAmount.trim() == "0.00") {
//                    resCreditAmount = resCreditAmount.replace("0.00", " ");
//                }
//                if (resCreditAmount.trim().equalsIgnoreCase("0") || resCreditAmount.trim().equals("0") || resCreditAmount.trim() == "0") {
//                    resCreditAmount = resCreditAmount.replace("0", " ");
//                }

                String resRunAmnt = response08[i];
                resRunAmnt = resRunAmnt.replace("[", "");
                resRunAmnt = resRunAmnt.replace("]", "");
                Double dresRunAmnt = Double.parseDouble(resRunAmnt);
                totalRunAmount =totalRunAmount + dresRunAmnt;

//                if (resRunAmnt.trim().equalsIgnoreCase("0.00") || resRunAmnt.trim().equals("0.00") || resRunAmnt.trim() == "0.00") {
//                    resRunAmnt = resRunAmnt.replace("0.00", " ");
//                }
//                if (resRunAmnt.trim().equalsIgnoreCase("0") || resRunAmnt.trim().equals("0") || resRunAmnt.trim() == "0") {
//                    resRunAmnt = resRunAmnt.replace("0", " ");
//                }

                String resDetailsOfService = response09[i];
                resDetailsOfService = resDetailsOfService.replace("[", "");
                resDetailsOfService = resDetailsOfService.replace("]", "");

                String value10 = "";
                String value11 = "";


                BWODetail detailMovie = new BWODetail(resInvoiceNo, resInvoiceDate, resTranType, resReceiptNo, resReceiptDate, strNoOfDays,resChequeNo, resDebitAmount, resCreditAmount, resRunAmnt, resDetailsOfService);
                detailMovieList.add(detailMovie);
            }
            if (i==(response0.length)-1){
                //closingBalance = openingBalance + totalDebitAmount-totalCreditAmount;
                NumberFormat format = new DecimalFormat("###.####");
                String stringNoOfDays = format.format(totalNoOfDays);
                String stringtotalDebitAmount = format.format(totalDebitAmount);
                String stringtotalCreditAmount = format.format(totalCreditAmount);
                String stringtotalRunAmnt = format.format(totalRunAmount);


                BWODetail detailMovie = new BWODetail("Total", "", "", "", "",stringNoOfDays, "", stringtotalDebitAmount, stringtotalCreditAmount, stringtotalRunAmnt, "");
                detailMovieList.add(detailMovie);
            }
            if (i==(response0.length)-1){
//                //closingBalance = openingBalance + totalDebitAmount-totalCreditAmount;
               NumberFormat format = new DecimalFormat("###.####");
//                String stringtotalDebitAmount = format.format(totalDebitAmount);
//                String stringtotalCreditAmount = format.format(totalCreditAmount);
                String stringtotalRunAmnt = format.format(totalRunAmount);


                BWODetail detailMovie = new BWODetail("", "", "", "", "","", "", stringtotalRunAmnt,"Closing balance", "", "");
                detailMovieList.add(detailMovie);
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