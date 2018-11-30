package com.ccs.ccs81.wyc_aw_ledger;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Banke Bihari on 10/6/2016.
 */
public class LedgerSummaryRecyclerViewActivity extends AppCompatActivity {
    private List<LedgerSummaryMovie> detailMovieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LedgerCustomAdapterSummary mAdapter;
    Double totalDebitAmount = 0.0,totalCreditAmount = 0.0,openingBalance = 0.0,closingBalance = 0.0;
    Boolean minusSignCreditAmount = false,minusSignDebitAmount=false;
    String voucherNo, voucherDate,debitAmount,creditAmount;
    TextView textViewTotalNetAmount, textViewTotalMonthWiseSale, textViewTotalYearWiseSale;
    Boolean runTot = false;
    ConnectionDetector cd;
    String cpCompanyUrl, cpCompanyID, sInvoiceNo, sType="", scpMobileNo, scpEmailId, partyCode="", cbPax="", cbCustomer="", cbSales="",format,startDate="",endDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledgerdetail_recyclerview);

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
                    LedgerSummaryRecyclerViewActivity.this.finish();
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
        toolbar.setTitle("Ledger "+partyCode+"  "+ startDate+"    " +endDate);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter = new LedgerCustomAdapterSummary(detailMovieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onClick(final View view, int position) {

                view.setBackgroundResource(R.color.LightBlue);

                final LedgerSummaryMovie movie = (LedgerSummaryMovie) detailMovieList.get(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LedgerSummaryRecyclerViewActivity.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("You selected " + movie.getVc_BlivNo() + " for email");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sInvoiceNo = movie.getVc_BlivNo();
                        sType = movie.getVc_Type();
                        if (sType.equalsIgnoreCase("AIR") || sType.equals("AIR") || sType == "AIR") {
                            format = "GST";
                        } else {
                            format = "CNK";
                        }

                        if (sType.trim().equalsIgnoreCase("CRV") || sType.equals("CRV") || sType.equals("CRV")){
                            Toast.makeText(LedgerSummaryRecyclerViewActivity.this,"No invoice for this CRV voucher type",Toast.LENGTH_LONG).show();
                        }
                        if (sType.trim().equalsIgnoreCase("BRV") || sType.equals("BRV") || sType.equals("BRV")){
                            Toast.makeText(LedgerSummaryRecyclerViewActivity.this,"No invoice for this BRV voucher type",Toast.LENGTH_LONG).show();
                        }
                        new emailInfoAsync().execute();
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

            public void onLongClick(View view, int position) {
                LedgerSummaryMovie movie = (LedgerSummaryMovie) detailMovieList.get(position);
               // Toast.makeText(getApplicationContext(), movie.getBillNo() + " is selected!", Toast.LENGTH_SHORT).show();
            }
        }));


        prepareMovieData();

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


        for (int i = 0; i < response0.length; i++) {
            if (i==0){
                String voucherType = response0[i];
                voucherType = voucherType.replace("[", " ");

               String voucherNo = response01[i];
                voucherNo = voucherNo.replace("[", "  ");

                String  voucherDate = response02[i];
                voucherDate = voucherDate.replace("[", " ");

                String debitAmount = response03[i];
                debitAmount = debitAmount.replace("[", " ");


                String creditAmount = response04[i];
                creditAmount = creditAmount.replace("[", "");

                LedgerSummaryMovie movie = new LedgerSummaryMovie(voucherType, voucherNo, voucherDate, debitAmount, creditAmount);
                detailMovieList.add(movie);
            }else if (i==1){
                String voucherType = response0[i];
                voucherType = voucherType.replace("[", " ");

                String voucherNo = response01[i];
                voucherNo = voucherNo.replace("[", "  ");

                String  voucherDate = response02[i];
                voucherDate = voucherDate.replace("[", " ");

                String debitAmount = response03[i];
                debitAmount = debitAmount.replace("[", " ");
                openingBalance = Double.parseDouble(debitAmount);

                String creditAmount = response04[i];
                creditAmount = creditAmount.replace("[", "");

                LedgerSummaryMovie movie = new LedgerSummaryMovie(voucherType, voucherNo, voucherDate, debitAmount, creditAmount);
                detailMovieList.add(movie);

            }else if (i>1) {
                String voucherType = response0[i];
                voucherType = voucherType.replace("[", " ");
                voucherType = voucherType.replace("]", " ");

               String voucherNo = response01[i];
                voucherNo = voucherNo.replace("[", "  ");
                voucherNo = voucherNo.replace("]", " ");
                // dresYqtax = Double.valueOf(resYqtax.trim()).longValue();


                String voucherDate = response02[i];
                voucherDate = voucherDate.replace("[", " ");
                voucherDate = voucherDate.replace("]", " ");


                String debitAmount = response03[i];
                debitAmount = debitAmount.replace("[", " ");
                debitAmount = debitAmount.replace("]", " ");
                Double dresDebitAmount = Double.parseDouble(debitAmount);
//                if (dresDebitAmount < 0) { //////////checking minus(-) sign
//                    minusSignDebitAmount = true;
//                }
//                Double removeMinusSignDebitAmount = Math.abs(dresDebitAmount);////It treats minus(-) sign as a Plus(+) sign
                totalDebitAmount = totalDebitAmount + dresDebitAmount;

                String creditAmount = response04[i];
                creditAmount = creditAmount.replace("[", "");
                creditAmount = creditAmount.replace("]", "");
                Double dresCreditAmount = Double.parseDouble(creditAmount);
//                if (dresCreditAmount<0){
//                    minusSignCreditAmount = true;
//                }
                totalCreditAmount  = totalCreditAmount + dresCreditAmount;

                LedgerSummaryMovie movie = new LedgerSummaryMovie(voucherType, voucherNo, voucherDate, debitAmount, creditAmount);
                detailMovieList.add(movie);
            }if (i==(response0.length)-1){
                NumberFormat format = new DecimalFormat("###.####");
                String stringtotalDebitAmount = format.format(totalDebitAmount);
                String stringtotalCreditAmount = format.format(totalCreditAmount);

                LedgerSummaryMovie movie = new LedgerSummaryMovie("Total", "", "", stringtotalDebitAmount, stringtotalCreditAmount);
                detailMovieList.add(movie);
            }
            if (i==(response0.length)-1){
                closingBalance = openingBalance + totalDebitAmount-totalCreditAmount;
                NumberFormat format = new DecimalFormat("###.####");
                String stringClosingBalance = format.format(closingBalance);


                LedgerSummaryMovie movie = new LedgerSummaryMovie("", "", "Closing Balance", stringClosingBalance,"");
                detailMovieList.add(movie);
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