package com.ccs.ccs81.wyc_aw_ledger;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Created by Banke Bihari on 04/11/2016.
 */
public class SecondActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    EditText editTextCompanyID, editTextBranch, editTextPartyCode;
    Spinner spinnerType,spinnerFinancialYear;
    TextView textViewStartDate, textViewEndDate;
    ArrayList<String> items = new ArrayList<>();
    SpinnerDialog spinnerDialog;////Link for this is: https://www.youtube.com/watch?v=wOw8V-f9Xpc
    Button buttonSubmit, buttonExcel, spinnerPartyCode;
    ProgressBar pbCircular;
    String cpCompanyID, branchName = "", customerBranch, agentBranch, startDate = "null", strStartDate = "null", cpStartDate, endDate, rvstartDate, rvendDate, aResponse, cpCompanyUrl, cpPartyCode, partyCode, cpMobileNo, mUserName, mPassword, check, res,
            makeExcelSheet = "false", state, dropDownAgentBranch = "", cpMultipleBranch = "", cpBranch = "", bwoType = "", cpEmail = "", cbPax = "F", cbCustomer = "F", cbSales = "F", manualEmailId,cpFinancialYear,strFinancialYear;
    RadioGroup radioGroup;
    RadioButton radioButtonLedgerSummary, radioButtonLedgerDetail, radioButtonBwoSummary, radioButtonBwoDetails;
    EditText editTextManualEmail;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        inItItems();
        cd = new ConnectionDetector(getApplicationContext());
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connection Error");
            alertDialog.setMessage("Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SecondActivity.this.finish();
                }
            });
            alertDialog.show();

            return;
        }


        editTextCompanyID = (EditText) findViewById(R.id.et_companyid);
        editTextBranch = (EditText) findViewById(R.id.et_branch);
        spinnerType = (Spinner) findViewById(R.id.spinner);
        //spinnerFinancialYear = (Spinner) findViewById(R.id.spinnerfinancialyear);
        textViewStartDate = (TextView) findViewById(R.id.et_startdate);
        textViewEndDate = (TextView) findViewById(R.id.et_endtdate);
        editTextPartyCode = (EditText) findViewById(R.id.et_partycode);
        //spinnerPartyCode = (Button) findViewById(R.id.spinnerpartycode);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioButtonLedgerSummary = (RadioButton) findViewById(R.id.rb_ledgersummary);
        radioButtonLedgerDetail = (RadioButton) findViewById(R.id.rb_ledgerdetail);
        radioButtonBwoSummary = (RadioButton) findViewById(R.id.rb_bwosummary);
        radioButtonBwoDetails = (RadioButton) findViewById(R.id.rb_bwodetail);
//        checkBoxPax = (CheckBox) findViewById(R.id.checkbox_pax);
//
//        checkBoxCustomer = (CheckBox) findViewById(R.id.cb_customer);
//        checkBoxSales = (CheckBox) findViewById(R.id.cb_sales);
        editTextManualEmail = (EditText) findViewById(R.id.et_manualemail);
        buttonSubmit = (Button) findViewById(R.id.btn_submit);
        buttonExcel = (Button) findViewById(R.id.btn_downloadExcel);
        pbCircular = (ProgressBar) findViewById(R.id.pbCirular);
        pbCircular.setVisibility(View.INVISIBLE);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("companyDetail", MODE_PRIVATE);
        cpCompanyID = sharedPreferences.getString("companyID", null);
        cpStartDate = sharedPreferences.getString("startDate", null);
        cpCompanyUrl = sharedPreferences.getString("companyUrl", null);
        cpPartyCode = sharedPreferences.getString("partyCode", null);
        mUserName = sharedPreferences.getString("userName", null);
        mPassword = sharedPreferences.getString("password", null);
        cpMobileNo = sharedPreferences.getString("mobileNo", null);
        dropDownAgentBranch = sharedPreferences.getString("dropDownAgentBranch", null);
        cpMultipleBranch = sharedPreferences.getString("multipleBranch", null);
        cpBranch = sharedPreferences.getString("branch", null);
        cpEmail = sharedPreferences.getString("cpEmail", null);
        cpFinancialYear = sharedPreferences.getString("financialYear", null);

        editTextCompanyID.setText(cpCompanyID);

//        spinnerDialog = new SpinnerDialog(SecondActivity.this, items, "Select Any Item");
//        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
//            @Override
//            public void onClick(String item, int position) {
//                Toast.makeText(SecondActivity.this, "Selected item is" + item, Toast.LENGTH_LONG).show();
//                editTextPartyCode.setText(item);
//            }
//        });

//        Calendar calendar = Calendar.getInstance();
//        int fyear = calendar.get(Calendar.YEAR);
//        spinnerFinancialYear.setOnItemSelectedListener(this);
//        List<String> financialYearList = new ArrayList<String>();
//        financialYearList.add("Select Financial Year");
//        //   financialYearList.add(cpFinancialYear);
//        for (int i = Integer.parseInt(cpFinancialYear); i <=fyear; i++) {
//
//            financialYearList.add(""+i);
//        }
//
//        ArrayAdapter<String> financialYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, financialYearList);
//        financialYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerFinancialYear.setAdapter(financialYearAdapter);


        if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {
            editTextBranch.setText("");
            editTextBranch.setClickable(false);
            editTextBranch.setFocusableInTouchMode(false);
            editTextBranch.setFocusable(false);
            // editTextBranch.setVisibility(View.INVISIBLE);
            spinnerType.setClickable(true);
            spinnerType.setFocusableInTouchMode(true);
            spinnerType.setFocusable(true);
            spinnerType.setVisibility(View.VISIBLE);
            spinnerType.setEnabled(true);
            // textViewAgentBranch.setVisibility(View.VISIBLE);

            if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found"){
                spinnerType.setOnItemSelectedListener(this);
                List<String> categories = new ArrayList<String>();
                categories.add("No Branch");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
            }else {
                dropDownAgentBranch = dropDownAgentBranch.replace("[", "");
                dropDownAgentBranch = dropDownAgentBranch.replace("]", "");
                String[] branch = dropDownAgentBranch.split(",");

                spinnerType.setOnItemSelectedListener(this);
                List<String> categories = new ArrayList<String>();
                categories.add("Select Agent Branch");
                for (int i = 0; i < branch.length; i++) {

                    categories.add(branch[i]);
                }
                categories.add("All");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
            }

//            editTextPartyCode.setClickable(true);
//            editTextPartyCode.setFocusable(true);
//            editTextPartyCode.setFocusableInTouchMode(true);
//            editTextPartyCode.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    spinnerDialog.showSpinerDialog();
//                }
//            });

        } else {
            if (cpMultipleBranch == "yes" || cpMultipleBranch.equalsIgnoreCase("yes") || cpMultipleBranch.equals("yes")) {
                customerBranch = cpBranch;
                editTextBranch.setText(cpBranch);
                editTextBranch.setClickable(false);
                editTextBranch.setFocusableInTouchMode(false);
                editTextBranch.setFocusable(false);
                //editTextBranch.setVisibility(View.VISIBLE);
                spinnerType.setClickable(false);
                spinnerType.setFocusableInTouchMode(false);
                spinnerType.setFocusable(false);
                spinnerType.setEnabled(false);
                // spinnerType.setVisibility(View.INVISIBLE);
                //textViewAgentBranch.setVisibility(View.INVISIBLE);
            } else {
                customerBranch = "";
                editTextBranch.setText("");
                editTextBranch.setClickable(false);
                editTextBranch.setFocusableInTouchMode(false);
                editTextBranch.setFocusable(false);
                //editTextBranch.setVisibility(View.VISIBLE);
                spinnerType.setClickable(false);
                spinnerType.setFocusableInTouchMode(false);
                spinnerType.setFocusable(false);
                spinnerType.setEnabled(false);
                // spinnerType.setVisibility(View.INVISIBLE);
                // textViewAgentBranch.setVisibility(View.INVISIBLE);
            }
            partyCode = cpPartyCode;
            editTextPartyCode.setText(cpPartyCode);
//            editTextPartyCode.setClickable(false);
//            editTextPartyCode.setFocusable(false);
//            editTextPartyCode.setFocusableInTouchMode(false);

        }


        String dateFormat = cpStartDate;
        String[] dateAray = dateFormat.split("-");
        String year = dateAray[0];
        String month = dateAray[1];
        final String day = dateAray[2];
        // String  formatedStartDate.appe
        textViewStartDate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
        startDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
//        setCurrentDateOnView();
//        addListenerOnButton();

        //////////START////////Show calendar and select date ////////
        final Calendar myStartDateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListenerStartDAte = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myStartDateCalendar.set(Calendar.YEAR, year);
                myStartDateCalendar.set(Calendar.MONTH, month);
                myStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                textViewStartDate.setText(sdf.format(myStartDateCalendar.getTime()));
                startDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(dayOfMonth)).trim();
            }
        };

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SecondActivity.this, datePickerListenerStartDAte, myStartDateCalendar
                        .get(Calendar.YEAR), myStartDateCalendar.get(Calendar.MONTH),
                        myStartDateCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                textViewEndDate.setText(sdf.format(myCalendar.getTime()));
                rvendDate = textViewEndDate.getText().toString();
                endDate = String.valueOf(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth)).trim();
            }
        };

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SecondActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


// ////////END////////Show calendar and select date ////////
        if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found"){
            spinnerType.setOnItemSelectedListener(this);
            List<String> categories = new ArrayList<String>();
            categories.add("No Branch");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(dataAdapter);
        }else {
            dropDownAgentBranch = dropDownAgentBranch.replace("[", "");
            dropDownAgentBranch = dropDownAgentBranch.replace("]", "");
            String[] branch = dropDownAgentBranch.split(",");

            spinnerType.setOnItemSelectedListener(this);
            List<String> categories = new ArrayList<String>();
            categories.add("Select Agent Branch");
            for (int i = 0; i < branch.length; i++) {

                categories.add(branch[i]);
            }
            categories.add("All");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(dataAdapter);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_bwosummary) {
                    bwoType = "summary";
                } else if (checkedId == R.id.rb_bwodetail) {
                    bwoType = "detail";
                } else if (checkedId == R.id.rb_ledgersummary) {
                    bwoType = "";
                } else if (checkedId == R.id.rb_ledgerdetail) {
                    bwoType = "ledetail";
                }
            }
        });

        buttonExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

////START/ Run time Permission to write file///////////
                if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        //just request the permission
                        ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                    editor.commit();


                } else {
                    //You already have the permission, just go ahead.
                    //proceedAfterPermission();

////END/ Run time Permission to write file////in above else block further code vl write ///////
                    makeExcelSheet = "True";
                    strStartDate = textViewStartDate.getText().toString();
                    rvstartDate = strStartDate;

                    String dateFormat = strStartDate;
                    String[] dateAray = dateFormat.split("-");
                    String day = dateAray[0];
                    String month = dateAray[1];
                    final String year = dateAray[2];
                    strStartDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
                    // branchName = editTextBranch.getText().toString();
                    if (textViewEndDate.getText().length() < 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Select End Date first");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }else if (!dateValidation(strStartDate, endDate, "yyyy-MM-dd")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage(" End Date must be grater than start date");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }  else if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {
                        partyCode = editTextPartyCode.getText().toString();
                        if (editTextPartyCode.getText().length() < 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Enter party code first");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else if (agentBranch.equalsIgnoreCase("Select Agent Branch") || agentBranch.equals("Select Agent Branch") || agentBranch == "Select Agent Branch") {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Select agent branch first");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            branchName = agentBranch;
                            pbCircular.setVisibility(View.VISIBLE);
                            new agentAsync().execute();
                        }
                    } else {
                        partyCode = cpPartyCode;
                        branchName = customerBranch;
                        new customerAsync().execute();
                    }
                }
            }
        });
//lk@uniglobeashlintravel.in
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startDate = editTextStartDate.getText().toString();
                String manualEmail = editTextManualEmail.getText().toString();
                if (manualEmail == "" || manualEmail == "null" || manualEmail.equalsIgnoreCase("") || manualEmail.equals("")) {

                } else {
                    cpEmail = manualEmail;
                }
                makeExcelSheet = "false";

                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(SecondActivity.this, "Please connect to working Internet connection", Toast.LENGTH_LONG).show();
                    return;
                }


                strStartDate = textViewStartDate.getText().toString();
                rvstartDate = strStartDate;

                String dateFormat = strStartDate;
                String[] dateAray = dateFormat.split("-");
                String day = dateAray[0];
                String month = dateAray[1];
                final String year = dateAray[2];
                strStartDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));

                if (textViewEndDate.getText().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Select End Date first");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (!dateValidation(strStartDate, endDate, "yyyy-MM-dd")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage(" End Date must be grater than start date");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {
                    partyCode = editTextPartyCode.getText().toString();
                    if (editTextPartyCode.getText().length() < 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter party code first");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (agentBranch.equalsIgnoreCase("Select Agent Branch") || agentBranch.equals("Select Agent Branch") || agentBranch == "Select Agent Branch") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Select agent branch first");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        branchName = agentBranch;
                        pbCircular.setVisibility(View.VISIBLE);
                        new agentAsync().execute();
                    }
                } else {
                    partyCode = cpPartyCode;
                    branchName = customerBranch;
                    pbCircular.setVisibility(View.VISIBLE);
                    new customerAsync().execute();
                }

            }
        });

    }

    private void inItItems() {
        for (int i = 0; i < 100; i++) {
            items.add("CCS - Party code" + (i + 1));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinner) {
            if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found") {
                agentBranch = "NoBranch";
            } else {
                agentBranch = parent.getItemAtPosition(position).toString();
                agentBranch = String.valueOf(agentBranch);
            }
        }

//        if (spinner.getId() == R.id.spinnerfinancialyear){
//            strFinancialYear = parent.getItemAtPosition(position).toString();
//            strFinancialYear = String.valueOf(strFinancialYear);
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class agentAsync extends AsyncTask<Void, Void, Void> {
        String aResponse;

        @Override
        protected Void doInBackground(Void... voids) {
            //Create Webservice class object
            AgentWebservice agentWebservice = new AgentWebservice();

            //Call Webservice class method and pass values and get response
            Log.i("BBRRaResponse", cpCompanyUrl + "" + cpCompanyID + "" + mUserName + "" + mPassword + "" + cpPartyCode);
            aResponse = agentWebservice.getConvertedWeight(cpCompanyUrl, "GetValidUser", cpCompanyID, mUserName, mPassword);
            res = aResponse;

            if (res == "Invalid User" || res.equalsIgnoreCase("Invalid User") || res.equals("Invalid User")) {
                check = "";
                startDate = "";
            } else {

                String[] strArray = res.split("%");
                check = strArray[0];
                startDate = strArray[1];
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (res == "Invalid User" || res.equalsIgnoreCase("Invalid User") || res.equals("Invalid User")) {
                Toast.makeText(SecondActivity.this, "Do Registration Again", Toast.LENGTH_LONG).show();
            } else if (check.equals("Valid User")) {
                if (bwoType.equalsIgnoreCase("summary") || bwoType.equals("summary") || bwoType == "summary") {
                    new bwoSummaryInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("detail") || bwoType.equals("detail") || bwoType == "detail") {
                    new bwoDetailInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("") || bwoType.equals("") || bwoType == "") {
                    new ledgerSummaryInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("ledetail") || bwoType.equals("ledetail") || bwoType == "ledetail") {
                    new ledgerDetailInfoAsync().execute();
                }
            }
        }
    }

    class customerAsync extends AsyncTask<Void, Void, Void> {
        String bResponse;

        @Override
        protected Void doInBackground(Void... voids) {
            //Create Webservice class object
            CustomerWebservice com = new CustomerWebservice();

            //Call Webservice class method and pass values and get response
            bResponse = com.checkValidUsernamePassword(cpCompanyUrl, "wyCheckPartyLogin", cpCompanyID, partyCode, mUserName, mPassword);

            Log.i("AndroidExampleOutput", "----" + bResponse);

            return null;
        }

        protected void onPostExecute(Void result) {
            //Toast.makeText(getApplicationContext(), bResponse, Toast.LENGTH_LONG).show();

            if (bResponse.equals("Successfully Login")) {
                if (bwoType.equalsIgnoreCase("summary") || bwoType.equals("summary") || bwoType == "summary") {
                    new bwoSummaryInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("detail") || bwoType.equals("detail") || bwoType == "detail") {
                    new bwoDetailInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("") || bwoType.equals("") || bwoType == "") {
                    new ledgerSummaryInfoAsync().execute();
                } else if (bwoType.equalsIgnoreCase("ledetail") || bwoType.equals("ledetail") || bwoType == "ledetail") {
                    new ledgerDetailInfoAsync().execute();
                }
            } else {
                Toast.makeText(SecondActivity.this, "Do Registration Again", Toast.LENGTH_LONG).show();
            }
        }
    }


    class ledgerSummaryInfoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            LedgerSummaryWebservice secondWebservice = new LedgerSummaryWebservice();
            aResponse = secondWebservice.getConvertedWeight(cpCompanyUrl, "GetPartyLedgerJson", cpCompanyID, branchName.trim(), strStartDate, endDate, partyCode, cpMobileNo, bwoType);
            Log.i("RRBBresponse", aResponse);
            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);

            if (aResponse.equalsIgnoreCase("No Data Found") || aResponse == "No Data Found") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.equalsIgnoreCase("Party doesn't belongs to this branch") || aResponse.equals("Party doesn't belongs to this branch") || aResponse == "Party doesn't belongs to this branch") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.trim().equalsIgnoreCase("Something went wrong at server side or wrong input") || aResponse.trim().equals("Something went wrong at server side or wrong input") || aResponse.trim() == "Something went wrong at server side or wrong input") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else {
                if (makeExcelSheet.equals("True") || makeExcelSheet == "True") {
                    String input = "";// = "Harey,Krishnagg"+"\n"+"Harey,Ramgg";
                    String string = aResponse;
                    String[] parts = string.split("%");

                    String[] response0 = parts[0].split(",");
                    String[] response01 = parts[1].split(",");
                    String[] response02 = parts[2].split(",");
                    String[] response03 = parts[3].split(",");
                    String[] response04 = parts[4].split(",");

                    for (int i = 0; i < response0.length; i++) {
                        String VC_TYPE = response0[i];
                        String VC_BLIV_NO = response01[i];
                        String VC_VOUCHDT = response02[i];
                        String VC_DR_AMOUNT = response03[i];
                        String VC_CR_AMOUNT = response04[i];

                        input = input + "" + VC_TYPE + "," + VC_BLIV_NO + "," + VC_VOUCHDT + "," + VC_DR_AMOUNT + "," + VC_CR_AMOUNT + "\n";
                        input = input.replace("[", "");
                        input = input.replace("]", "");

                    }

                    state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {//checking sd card or extenal device is available or not
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/Winyatra");//new folder created
                        if (!Dir.exists()) { //folder exist or not
                            Dir.mkdir();    //folder created
                        }
                        File file = new File(Dir, "Ledger Summary.csv");  //text file created


                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file); //write in text file
                            fileOutputStream.write(input.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Excel saved ", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "sd card not found", Toast.LENGTH_LONG).show();
                    }

                } else if (makeExcelSheet.equals("false") || makeExcelSheet == "false") {
                    Intent intent = new Intent(SecondActivity.this, LedgerSummaryRecyclerViewActivity.class);
                    Bundle extra = new Bundle();
                    extra.putString("webserviceresponse", aResponse);
                    extra.putString("cpCompanyUrl", cpCompanyUrl);
                    extra.putString("cpCompanyID", cpCompanyID);
                    extra.putString("cpMobileNo", cpMobileNo);
                    extra.putString("cpEmail", cpEmail);
                    extra.putString("pax", cbPax);
                    extra.putString("customer", cbCustomer);
                    extra.putString("sales", cbSales);
                    extra.putString("cpPartyCode", cpPartyCode);
                    extra.putString("startDate", rvstartDate);
                    extra.putString("endDate", rvendDate);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
        }
    }


    class ledgerDetailInfoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            LedgerDetailWebservice ledgerDetailWebservice = new LedgerDetailWebservice();
            aResponse = ledgerDetailWebservice.getConvertedWeight(cpCompanyUrl, "GetPartyLedgerJson", cpCompanyID, branchName.trim(), strStartDate, endDate, partyCode, cpMobileNo, bwoType);
            Log.i("RRBBledetail", aResponse);
            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (aResponse.equalsIgnoreCase("No Data Found") || aResponse == "No Data Found") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.equalsIgnoreCase("Party doesn't belongs to this branch") || aResponse.equals("Party doesn't belongs to this branch") || aResponse == "Party doesn't belongs to this branch") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            }else if (aResponse.trim().equalsIgnoreCase("Something went wrong at server side or wrong input") || aResponse.trim().equals("Something went wrong at server side or wrong input") || aResponse.trim() == "Something went wrong at server side or wrong input") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else {
                if (makeExcelSheet.equals("True") || makeExcelSheet == "True") {
                    String input = "";// = "Harey,Krishnagg"+"\n"+"Harey,Ramgg";
                    String string = aResponse;
                    String[] parts = string.split("%");
//input = parts[0].replace(",","\n")+","+parts[1].replace(",","\n");
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
                        String voucherType = response0[i];
                        String voucherNumber = response01[i];
                        String voucherDate = response02[i];
                        String billNo = response03[i];
                        String billDate = response04[i];
                        String number = response05[i];
                        String date = response06[i];
                        String debitAmount = response07[i];
                        String creditAmount = response08[i];
                        String closingBalance = response09[i];
                        String opening = response10[i];
                        String VcDescription = response11[i];
                        input = input + "" + voucherType + "," + voucherNumber + "," + voucherDate + "," + billNo + "," + billDate + "," + number + "," + date + "," + debitAmount + "," + creditAmount + "," + closingBalance + "," + opening + "," + VcDescription + "\n";
                        input = input.replace("[", "");
                        input = input.replace("]", "");

                    }

                    state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {//checking sd card or extenal device is available or not
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/Winyatra");//new folder created
                        if (!Dir.exists()) { //folder exist or not
                            Dir.mkdir();    //folder created
                        }
                        File file = new File(Dir, "Ledger Detail.csv");  //text file created


                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file); //write in text file
                            fileOutputStream.write(input.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Excel saved ", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "sd card not found", Toast.LENGTH_LONG).show();
                    }

                } else if (makeExcelSheet.equals("false") || makeExcelSheet == "false") {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LedgerDetailWebserviceResponse", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LedgerDetailResponse", aResponse);
                    editor.commit();

                    Intent intent = new Intent(SecondActivity.this, LedgerDetailRecyclerViewActivity.class);
                    Bundle extra = new Bundle();
                   // extra.putString("webserviceresponse", aResponse);
                    extra.putString("cpCompanyUrl", cpCompanyUrl);
                    extra.putString("cpCompanyID", cpCompanyID);
                    extra.putString("cpMobileNo", cpMobileNo);
                    extra.putString("cpEmail", cpEmail);
                    extra.putString("pax", cbPax);
                    extra.putString("customer", cbCustomer);
                    extra.putString("sales", cbSales);
                    extra.putString("cpPartyCode", cpPartyCode);
                    extra.putString("startDate", rvstartDate);
                    extra.putString("endDate", rvendDate);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
        }
    }


    class bwoSummaryInfoAsync extends AsyncTask<Void, Void, Void> {
        String bwoSummaryaResponse;

        @Override
        protected Void doInBackground(Void... voids) {
            BWOSummaryWebservice summaryWebservice = new BWOSummaryWebservice();
            bwoSummaryaResponse = summaryWebservice.getConvertedWeight(cpCompanyUrl, "GetPartyLedgerJson", cpCompanyID, branchName.trim(), strStartDate, endDate, partyCode, cpMobileNo, bwoType);
            Log.i("RRBBresponse77", bwoSummaryaResponse);
            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (bwoSummaryaResponse.equalsIgnoreCase("No Data Found") || bwoSummaryaResponse == "No Data Found") {
                Toast.makeText(SecondActivity.this, bwoSummaryaResponse, Toast.LENGTH_LONG).show();
            } else if (bwoSummaryaResponse.equalsIgnoreCase("Party doesn't belongs to this branch") || bwoSummaryaResponse.equals("Party doesn't belongs to this branch") || bwoSummaryaResponse == "Party doesn't belongs to this branch") {
                Toast.makeText(SecondActivity.this, bwoSummaryaResponse, Toast.LENGTH_LONG).show();
            }else if (bwoSummaryaResponse.trim().equalsIgnoreCase("Something went wrong at server side or wrong input") || bwoSummaryaResponse.trim().equals("Something went wrong at server side or wrong input") || bwoSummaryaResponse.trim() == "Something went wrong at server side or wrong input") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            }else {
                if (makeExcelSheet.equals("True") || makeExcelSheet == "True") {
                    String input = "";// = "Harey,Krishnagg"+"\n"+"Harey,Ramgg";
                    String string = bwoSummaryaResponse;
                    String[] parts = string.split("%");
//input = parts[0].replace(",","\n")+","+parts[1].replace(",","\n");
                    String[] response0 = parts[0].split(",");
                    String[] response01 = parts[1].split(",");
                    String[] response02 = parts[2].split(",");
                    String[] response03 = parts[3].split(",");
                    String[] response04 = parts[4].split(",");
                    String[] response05 = parts[5].split(",");
                    String[] response06 = parts[6].split(",");
                    String[] response07 = parts[7].split(",");

                    for (int i = 0; i < response0.length; i++) {
                        String BillNo = response0[i];
                        String BillDate = response01[i];
                        String Passenger = response02[i];
                        String BillAmt = response03[i];
                        String AmtRec = response04[i];
                        String CNAmt = response05[i];
                        String BillDue = response06[i];
                        String Balance = response07[i];
                        input = input + "" + BillNo + "," + BillDate + "," + BillAmt + "," + AmtRec + "," + CNAmt + "," + BillDue + "," + Balance + "," + Passenger + "\n";
                        input = input.replace("[", "");
                        input = input.replace("]", "");

                    }

                    state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {//checking sd card or extenal device is available or not
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/Winyatra");//new folder created
                        if (!Dir.exists()) { //folder exist or not
                            Dir.mkdir();    //folder created
                        }
                        File file = new File(Dir, "Billwise Outstanding Summary Report.csv");  //text file created


                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file); //write in text file
                            fileOutputStream.write(input.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Excel saved ", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "sd card not found", Toast.LENGTH_LONG).show();
                    }

                } else if (makeExcelSheet.equals("false") || makeExcelSheet == "false") {
                    Intent intent = new Intent(SecondActivity.this, BWOSummaryRecyclerViewActivity.class);
                    Bundle extra = new Bundle();
                    extra.putString("webserviceresponse", bwoSummaryaResponse);
                    extra.putString("cpCompanyUrl", cpCompanyUrl);
                    extra.putString("cpCompanyID", cpCompanyID);
                    extra.putString("cpMobileNo", cpMobileNo);
                    extra.putString("cpEmail", cpEmail);
                    extra.putString("pax", cbPax);
                    extra.putString("customer", cbCustomer);
                    extra.putString("sales", cbSales);
                    extra.putString("cpPartyCode", cpPartyCode);
                    extra.putString("startDate", rvstartDate);
                    extra.putString("endDate", rvendDate);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
        }
    }

    class bwoDetailInfoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            BWODetailWebservice detailWebservice = new BWODetailWebservice();
            aResponse = detailWebservice.getConvertedWeight(cpCompanyUrl, "GetPartyLedgerJson", cpCompanyID, branchName.trim(), strStartDate, endDate, partyCode, cpMobileNo, bwoType);
            Log.i("RRBBresponse", aResponse);
            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (aResponse.equalsIgnoreCase("No Data Found") || aResponse == "No Data Found") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.equalsIgnoreCase("Party doesn't belongs to this branch") || aResponse.equals("Party doesn't belongs to this branch") || aResponse == "Party doesn't belongs to this branch") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.trim().equalsIgnoreCase("Something went wrong at server side or wrong input") || aResponse.trim().equals("Something went wrong at server side or wrong input") || aResponse.trim() == "Something went wrong at server side or wrong input") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            }else {
                if (makeExcelSheet.equals("True") || makeExcelSheet == "True") {
                    String input = "";// = "Harey,Krishnagg"+"\n"+"Harey,Ramgg";
                    String string = aResponse;
                    String[] parts = string.split("%");
//input = parts[0].replace(",","\n")+","+parts[1].replace(",","\n");
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
                        String InvoiceNo = response0[i];
                        String InvoiceDate = response01[i];
                        String TranType = response02[i];
                        String ReceiptNumber = response03[i];
                        String ReceiptDate = response04[i];
                        String CheqNumber = response05[i];
                        String DetailofServices = response06[i];
                        String DebitAmount = response07[i];
                        String CreditAmount = response08[i];
                        String Runamt = response09[i];


                        input = input + "" + InvoiceNo + "," + InvoiceDate + "," + TranType + "," + ReceiptNumber + "," + ReceiptDate + "," + CheqNumber + "," + DetailofServices + "," + DebitAmount + "," + CreditAmount + "," + Runamt + "\n";
                        input = input.replace("[", "");
                        input = input.replace("]", "");

                    }

                    state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {//checking sd card or extenal device is available or not
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/Winyatra");//new folder created
                        if (!Dir.exists()) { //folder exist or not
                            Dir.mkdir();    //folder created
                        }
                        File file = new File(Dir, "BillwiseOutstanding Detail.csv");  //text file created


                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file); //write in text file
                            fileOutputStream.write(input.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Excel saved ", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "sd card not found", Toast.LENGTH_LONG).show();
                    }

                } else if (makeExcelSheet.equals("false") || makeExcelSheet == "false") {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("BWODetailWebserviceResponse", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("BWODetailResponse", aResponse);
                    editor.commit();

                    Intent intent = new Intent(SecondActivity.this, BWODetailRecyclerViewActivity.class);
                    Bundle extra = new Bundle();
                   // extra.putString("webserviceresponse", aResponse);
                    extra.putString("cpCompanyUrl", cpCompanyUrl);
                    extra.putString("cpCompanyID", cpCompanyID);
                    extra.putString("cpMobileNo", cpMobileNo);
                    extra.putString("cpEmail", cpEmail);
                    extra.putString("pax", cbPax);
                    extra.putString("customer", cbCustomer);
                    extra.putString("sales", cbSales);
                    extra.putString("cpPartyCode", cpPartyCode);
                    extra.putString("startDate", rvstartDate);
                    extra.putString("endDate", rvendDate);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
        }
    }
    public static boolean dateValidation(String startDate, String endDate, String dateFormat) {
        try {

            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            Date endingDate = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (endingDate.equals(startingDate)) {
                return true;
            } else if (endingDate.after(startingDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

