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
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Banke Bihari on 10/6/2016.
 */
public class LedgerDetailRecyclerViewActivity extends AppCompatActivity {
    private List<LedgerDetailMovie> detailMovieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LedgerCustomAdapterDetail mAdapter;
    Double totalDebitAmount = 0.0, totalCreditAmount = 0.0, totalOpeningBalance = 0.0, totalClosingBalance = 0.0, openingBalance = 0.0, closingBalance = 0.0;

    TextView textViewTotalNetAmount, textViewTotalMonthWiseSale, textViewTotalYearWiseSale;
    //Boolean minusSignDebitAMount = false, minusSigncreditAmount = false, minusSignOpeningBalance = false, minusSignClosingBalance = false;
    ConnectionDetector cd;
    String cpCompanyUrl, cpCompanyID, sInvoiceNo, sType, scpMobileNo, scpEmailId, cbPax, cbCustomer,partyCode,startDate,endDate, cbSales, format, emailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledgerdetail_recyclerview);

        cd = new ConnectionDetector(getApplicationContext());

        if (!cd.isConnectingToInternet()) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connection Error");
            alertDialog.setMessage("Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LedgerDetailRecyclerViewActivity.this.finish();
                }
            });
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

        mAdapter = new LedgerCustomAdapterDetail(detailMovieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onClick(View view, int position) {
                view.setBackgroundResource(R.color.LightBlue);

                final LedgerDetailMovie movie = (LedgerDetailMovie) detailMovieList.get(position);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LedgerDetailRecyclerViewActivity.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("You selected " + movie.getVoucherNumber() + " and " + movie.getVcDescription() + "  for email");

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new emailInfoAsync().execute();
                        sInvoiceNo = movie.getVoucherNumber().trim();
                        sType = movie.getVoucherType().trim();
                        if (sType.equalsIgnoreCase("AIR") || sType.equals("AIR") || sType == "AIR") {
                            format = "GST";
                        } else {
                            format = "CNK";
                        }

                        if (sType.trim().equalsIgnoreCase("CRV") || sType.equals("CRV") || sType.equals("CRV")) {
                            Toast.makeText(LedgerDetailRecyclerViewActivity.this, "No invoice for this CRV voucher type", Toast.LENGTH_LONG).show();
                        }
                        if (sType.trim().equalsIgnoreCase("BRV") || sType.equals("BRV") || sType.equals("BRV")) {
                            Toast.makeText(LedgerDetailRecyclerViewActivity.this, "No invoice for this BRV voucher type", Toast.LENGTH_LONG).show();
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

            public void onLongClick(View view, int position) {
//                LedgerDetailMovie movie = (LedgerDetailMovie) detailMovieList.get(position);
//                Toast.makeText(getApplicationContext(), movie.getChequeNo() + " is selected!", Toast.LENGTH_SHORT).show();
            }
        }));
        prepareMovieData();

        textViewTotalNetAmount = (TextView) findViewById(R.id.tv_toatalnetamount);
        textViewTotalMonthWiseSale = (TextView) findViewById(R.id.tv_totalmonthwisesale);
        textViewTotalYearWiseSale = (TextView) findViewById(R.id.tv_totalyearwisesale);

    }

    String check = "check";

    public void prepareMovieData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LedgerDetailWebserviceResponse", MODE_PRIVATE);
        String sp = sharedPreferences.getString("LedgerDetailResponse", null);
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
        String[] response10 = parts[10].split(",");
        String[] response11 = parts[11].split(",");


        for (int i = 0; i < response0.length; i++) {
            if (i == 0) {
                String resVoucherType = response0[i];
                resVoucherType = resVoucherType.replace("[", " ");
                resVoucherType = resVoucherType.replace("]", " ");

                String resVoucherNumber = response01[i];
                resVoucherNumber = resVoucherNumber.replace("[", "  ");
                resVoucherNumber = resVoucherNumber.replace("]", "  ");

                String resVoucherDate = response02[i];
                resVoucherDate = resVoucherDate.replace("[", " ");
                resVoucherDate = resVoucherDate.replace("]", " ");

                String resBillNo = response03[i];
                resBillNo = resBillNo.replace("[", " ");
                resBillNo = resBillNo.replace("]", " ");

                String resBillDate = response04[i];
                resBillDate = resBillDate.replace("[", "");
                resBillDate = resBillDate.replace("]", "");

                String resNumber = response05[i];
                resNumber = resNumber.replace("[", "");
                resNumber = resNumber.replace("]", "");

                String resDate = response06[i];
                resDate = resDate.replace("[", "");
                resDate = resDate.replace("]", "");

                String resDebitAmount = response07[i];
                resDebitAmount = resDebitAmount.replace("[", "");
                resDebitAmount = resDebitAmount.replace("]", "");

                String resCreditAmount = response08[i];
                resCreditAmount = resCreditAmount.replace("[", "");
                resCreditAmount = resCreditAmount.replace("]", "");

                String resClosingBalance = response09[i];
                resClosingBalance = resClosingBalance.replace("[", "");
                resClosingBalance = resClosingBalance.replace("]", "");

                String resOpening = response10[i];
                resOpening = resOpening.replace("[", "");
                resOpening = resOpening.replace("]", "");

                String resVcDescription = response11[i];
                resVcDescription = resVcDescription.replace("[", "");
                resVcDescription = resVcDescription.replace("]", "");

                LedgerDetailMovie detailMovie = new LedgerDetailMovie(resVoucherType, resVoucherNumber, resVoucherDate, resBillNo, resBillDate, resNumber, resDate, resDebitAmount, resCreditAmount, resClosingBalance, resOpening, resVcDescription);
                detailMovieList.add(detailMovie);

            } else if (i == 1) {
                String resVoucherType = response0[i];
                resVoucherType = resVoucherType.replace("[", " ");
                resVoucherType = resVoucherType.replace("]", " ");

                String resVoucherNumber = response01[i];
                resVoucherNumber = resVoucherNumber.replace("[", "  ");
                resVoucherNumber = resVoucherNumber.replace("]", "  ");

                String resVoucherDate = response02[i];
                resVoucherDate = resVoucherDate.replace("[", " ");
                resVoucherDate = resVoucherDate.replace("]", " ");

                String resBillNo = response03[i];
                resBillNo = resBillNo.replace("[", " ");
                resBillNo = resBillNo.replace("]", " ");

                String resBillDate = response04[i];
                resBillDate = resBillDate.replace("[", "");
                resBillDate = resBillDate.replace("]", "");

                String resNumber = response05[i];
                resNumber = resNumber.replace("[", "");
                resNumber = resNumber.replace("]", "");

                String resDate = response06[i];
                resDate = resDate.replace("[", "");
                resDate = resDate.replace("]", "");

                String resDebitAmount = response07[i];
                resDebitAmount = resDebitAmount.replace("[", "");
                resDebitAmount = resDebitAmount.replace("]", "");


                String resCreditAmount = response08[i];
                resCreditAmount = resCreditAmount.replace("[", "");
                resCreditAmount = resCreditAmount.replace("]", "");

                String resClosingBalance = response09[i];
                resClosingBalance = resClosingBalance.replace("[", "");
                resClosingBalance = resClosingBalance.replace("]", "");

                String resOpening = response10[i];
                resOpening = resOpening.replace("[", "");
                resOpening = resOpening.replace("]", "");

                String resVcDescription = response11[i];
                resVcDescription = resVcDescription.replace("[", "");
                resVcDescription = resVcDescription.replace("]", "");
                openingBalance = Double.parseDouble(resVcDescription);

                LedgerDetailMovie detailMovie = new LedgerDetailMovie(resVoucherType, resVoucherNumber, resVoucherDate, resBillNo, resBillDate, resNumber, resDate, resDebitAmount, resCreditAmount, resClosingBalance, resOpening, resVcDescription);
                detailMovieList.add(detailMovie);

            } else if (i > 1) {

                String resVoucherType = response0[i];
                resVoucherType = resVoucherType.replace("[", " ");
                resVoucherType = resVoucherType.replace("]", " ");

                String resVoucherNumber = response01[i];
                resVoucherNumber = resVoucherNumber.replace("[", "  ");
                resVoucherNumber = resVoucherNumber.replace("]", " ");
                // dresYqtax = Double.valueOf(resYqtax.trim()).longValue();


                String resVoucherDate = response02[i];
                resVoucherDate = resVoucherDate.replace("[", " ");
                resVoucherDate = resVoucherDate.replace("]", " ");


                String resBillNo = response03[i];
                resBillNo = resBillNo.replace("[", " ");
                resBillNo = resBillNo.replace("]", " ");
                //  lresMonthWiseSale = Double.valueOf(resMonthWiseSale.trim()).longValue();
                // TotalMonthWiseSale = TotalMonthWiseSale + lresMonthWiseSale;

                String resBillDate = response04[i];
                resBillDate = resBillDate.replace("[", "");
                resBillDate = resBillDate.replace("]", "");
//            lresYearWiseSale = Double.valueOf(resYearWiseSale).longValue();
//            TotalYearWiseSale = TotalYearWiseSale + lresYearWiseSale;

                String resNumber = response05[i];
                resNumber = resNumber.replace("[", "");
                resNumber = resNumber.replace("]", "");

                String resDate = response06[i];
                resDate = resDate.replace("[", "");
                resDate = resDate.replace("]", "");

                String resDebitAmount = response07[i];
                resDebitAmount = resDebitAmount.replace("[", "");
                resDebitAmount = resDebitAmount.replace("]", "");
                Double dresDebitAmount = Double.parseDouble(resDebitAmount);
//                if (dresDebitAmount < 0) { //////////checking minus(-) sign
//                    minusSignDebitAMount = true;
//                }
//                Double removeMinusSignDebitAmount = Math.abs(dresDebitAmount);////It treats minus(-) sign as a Plus(+) sign
                totalDebitAmount = totalDebitAmount + dresDebitAmount;


                String resCreditAmount = response08[i];
                resCreditAmount = resCreditAmount.replace("[", "");
                resCreditAmount = resCreditAmount.replace("]", "");
                Double dresCreditAmount = Double.parseDouble(resCreditAmount);
//                if (dresCreditAmount<0){
//                    minusSigncreditAmount = true;
//                }
                totalCreditAmount = totalCreditAmount + dresCreditAmount;

                String resClosingBalance = response09[i];
                resClosingBalance = resClosingBalance.replace("[", "");
                resClosingBalance = resClosingBalance.replace("]", "");
//                Double dresClosingBalance = Double.parseDouble(resClosingBalance);
//                if (dresClosingBalance<0){
//                    minusSignClosingBalance = true;
//                }
//                totalClosingBalance  = totalClosingBalance + dresClosingBalance;


                String resOpening = response10[i];
                resOpening = resOpening.replace("[", "");
                resOpening = resOpening.replace("]", "");
//                Double dresOpening = Double.parseDouble(resOpening);
//                if (dresOpening<0){
//                    minusSignOpeningBalance = true;
//                }
//                totalOpeningBalance  = totalOpeningBalance + dresOpening;

                String resVcDescription = response11[i];
                resVcDescription = resVcDescription.replace("[", "");
                resVcDescription = resVcDescription.replace("]", "");

                LedgerDetailMovie detailMovie = new LedgerDetailMovie(resVoucherType, resVoucherNumber, resVoucherDate, resBillNo, resBillDate, resNumber, resDate, resDebitAmount, resCreditAmount, resClosingBalance, resOpening, resVcDescription);
                detailMovieList.add(detailMovie);

            }
            if (i == (response0.length) - 1) {
                NumberFormat format = new DecimalFormat("###.####");
                String stringtotalDebitAmount = format.format(totalDebitAmount);
                String stringtotalCreditAmount = format.format(totalCreditAmount);
                String stringtotalClosingBalance = format.format(totalClosingBalance);
                String stringtotalOpeningBalance = format.format(totalOpeningBalance);

                LedgerDetailMovie detailMovie = new LedgerDetailMovie("Total", "", "", "", "", "", "", stringtotalDebitAmount, stringtotalCreditAmount, "", "", "");
                detailMovieList.add(detailMovie);
            }
            if (i == (response0.length) - 1) {
                closingBalance = openingBalance + totalDebitAmount - totalCreditAmount;
                NumberFormat format = new DecimalFormat("###.####");
                String stringClosingBalance = format.format(closingBalance);

                LedgerDetailMovie detailMovie = new LedgerDetailMovie("", "", "", "", "", "", "", "Closing Balance", stringClosingBalance, "", "", "");
                detailMovieList.add(detailMovie);

            }
        }


        mAdapter.notifyDataSetChanged();
    }


    class emailInfoAsync extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            cpCompanyUrl = getIntent().getStringExtra("cpCompanyUrl");
            cpCompanyID = getIntent().getStringExtra("cpCompanyID");
            scpMobileNo = getIntent().getStringExtra("cpMobileNo");
            scpEmailId = getIntent().getStringExtra("cpEmail");
            cbPax = getIntent().getStringExtra("pax");
            cbCustomer = getIntent().getStringExtra("customer");
            cbSales = getIntent().getStringExtra("sales");
            EmailWebservice emailWebservice = new EmailWebservice();
            emailResponse = emailWebservice.getConvertedWeight(cpCompanyUrl, "AirInvoiceAutoMail", cpCompanyID, sInvoiceNo, sType, scpMobileNo, scpEmailId, format, cbPax, cbCustomer, cbSales);
//            Toast.makeText(LedgerDetailRecyclerViewActivity.this,""+emailResponse,Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(LedgerDetailRecyclerViewActivity.this, "" + emailResponse, Toast.LENGTH_LONG).show();

        }
    }
}

