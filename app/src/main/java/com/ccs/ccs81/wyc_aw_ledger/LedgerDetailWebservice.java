package com.ccs.ccs81.wyc_aw_ledger;

import android.graphics.Movie;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Banke Bihari on 10/1/2016.
 */
public class LedgerDetailWebservice {

    String namespace = "http://tempuri.org/";
    // private String url = "http://103.16.142.124/WinYatra_GetArlnBsFareYqTaxBrnId_PUBLISH_FINAL_Json_V2/ArlnBsFareYqTaxBrnId.asmx";
    // private String url = "http://103.16.142.124/WYAactivityBySales_V1/WS_GetSales.asmx";
    private String url = null;
    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;
    String name;
    int check = 0;

    private List<Movie> movieList = new ArrayList<>();
    private List<String> listVoucherType = new ArrayList<>();
    private List<String> listVoucherNumber = new ArrayList<>();
    private List<String> listVoucherDate = new ArrayList<>();
    private List<String> listBillNo = new ArrayList<>();
    private List<String> listBillDate = new ArrayList<>();
    private List<String> listNumber = new ArrayList<>();
    private List<String> listDate = new ArrayList<>();
    private List<String> listDebitAmount = new ArrayList<>();
    private List<String> listCreditAmount = new ArrayList<>();
    private List<String> listClosingBalance = new ArrayList<>();
    private List<String> listOpening = new ArrayList<>();
    private List<String> listVcDescription = new ArrayList<>();


    private LedgerCustomAdapterDetail mAdapter;

    LedgerDetailWebservice() {
    }


    /**
     * Set Envelope
     */
    protected void SetEnvelope(String appUrl) {
        url = appUrl + "GetBalance_json.asmx";
        try {

            // Creating SOAP envelope
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //You can comment that line if your web service is not .NET one.
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport = new AndroidHttpTransport(url);
            androidHttpTransport.debug = true;

        } catch (Exception e) {
            System.out.println("Soap Exception---->>>" + e.toString());
        }
    }

    // MethodName variable is define for which webservice function  will call
    public String getConvertedWeight(String webUrl, String MethodName, String companyID, String branchName, String startDate, String endDate, String partyCode, String mobileNo, String bwoType) {

        try {
            SOAP_ACTION = namespace + MethodName;

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);

            //Adding Double value to request object
//            PropertyInfo weightProp =new PropertyInfo();
//            weightProp.setName("CompId");
//            weightProp.setValue(compID);
//            weightProp.setType(double.class);
//            request.addProperty(weightProp);

            request.addProperty("CompID", "" + companyID.trim());
            request.addProperty("BranchName", "" + branchName.trim());
            request.addProperty("StartDate", "" + startDate.trim());
            request.addProperty("EndDate", "" + endDate.trim());
            request.addProperty("PartyCode", "" + partyCode.trim());
            request.addProperty("Mob", "" + mobileNo.trim());
            request.addProperty("Bwotype", "" + bwoType.trim());

            SetEnvelope(webUrl);

            try {

                //SOAP calling webservice
                androidHttpTransport.call(SOAP_ACTION, envelope);

                //Got Webservice response
                String result = envelope.getResponse().toString();

//http://stackoverflow.com/questions/28133531/how-to-store-and-parse-soap-object-response
//http://stackoverflow.com/questions/32969039/getting-values-from-soap-xml-response
                //    String result = "{'table': [{'AIRLINE':'098','BASFARE':'90'},{'AIRLINE':'98','BASFARE':'90'}]}";
                if (result.equals("No Data Found") || result.equalsIgnoreCase("No Data Found") || result == "No Data Found") {

                    return result;
                } else if (result.equalsIgnoreCase("Party doesn't belongs to this branch") || result.equals("Party doesn't belongs to this branch") || result == "Party doesn't belongs to this branch") {
                    return result;
                } else {

                    JSONObject jsonRootObject = new JSONObject(result);

                    // table= {{"AIRLINE":"098","BASFARE":"90"},{"AIRLINE":"098","BASFARE":"90"}};
                    //
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = jsonRootObject.optJSONArray("Table");
//listInvoiceNo.add("RadheyShyam");
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listVoucherType.add(jsonObject.getString("VoucherType").toString());
                        listVoucherNumber.add(jsonObject.getString("VoucherNumber").toString());
                        listVoucherDate.add(jsonObject.getString("VoucherDate").toString());
                        listBillNo.add(jsonObject.getString("BillNumber").toString());
                        listBillDate.add(jsonObject.getString("BillDate").toString());
                        listNumber.add(jsonObject.getString("Number").toString());
                        listDate.add(jsonObject.getString("Date").toString());
                        listDebitAmount.add(jsonObject.getString("DebitAmount").toString());
                        listCreditAmount.add(jsonObject.getString("CreditAmount").toString());
                        listClosingBalance.add(jsonObject.getString("ClosingBalance").toString());
                        listOpening.add(jsonObject.getString("OpeningBalance").toString());
                        listVcDescription.add(jsonObject.getString("VcDescription").toString());
                    }
                    return listVoucherType + "@%" + listVoucherNumber + "@%" + listVoucherDate + "@%" + listBillNo + "@%" + listBillDate + "@%" + listNumber + "@%" + listDate + "@%" + listDebitAmount + "@%" + listCreditAmount + "@%" + listClosingBalance + "@%" + listOpening + "@%" + listVcDescription;
                }

            } catch (Exception e) {
                // TODO: handle exception
                String error = "Something went wrong at server side or wrong input";
                //return e.toString();
                return error;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String error = "Something went wrong at server side or wrong input";
            //return e.toString();
            return error;
        }

    }
    /************************************/
}
