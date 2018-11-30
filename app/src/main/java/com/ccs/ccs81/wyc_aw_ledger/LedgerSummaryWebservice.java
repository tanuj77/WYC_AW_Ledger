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
public class LedgerSummaryWebservice {

    /**
     * Variable Decleration................
     */
    String namespace = "http://tempuri.org/";
    private String url = null;
    String SOAP_ACTION;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;
    int check = 0;
    private List<Movie> movieList = new ArrayList<>();
    private List<String> listPartyCode = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private List<String> listNetAmount = new ArrayList<>();
    private List<String> listMonthWiseSale = new ArrayList<>();
    private List<String> listYearWiseSale = new ArrayList<>();
    private List<String> listRunTot = new ArrayList<>();


    private LedgerCustomAdapterDetail mAdapter;

    LedgerSummaryWebservice() {
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
    public String getConvertedWeight(String webUrl, String MethodName, String companyID, String branchName, String startDate, String endDate, String partyCode, String mobileNo,String bwoType) {

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

            //Adding String value to request object
//            request.addProperty("CompId", "GTT");
            //    request.addProperty("StrDt", "2017-02-02");
//            request.addProperty("StrBrnID", "bom");

            request.addProperty("CompID", "" + companyID);
            request.addProperty("BranchName", "" + branchName.trim());
            request.addProperty("StartDate", "" + startDate.trim());
            request.addProperty("EndDate", "" + endDate.trim());
            request.addProperty("PartyCode", "" + partyCode.trim());
            request.addProperty("Mob", "" + mobileNo.trim());
            request.addProperty("Bwotype", "" + bwoType);

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

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listPartyCode.add(jsonObject.getString("VC_TYPE").toString());
                        listName.add(jsonObject.getString("VC_BLIV_NO").toString());
                        listNetAmount.add(jsonObject.getString("VC_VOUCHDT"));
                        listMonthWiseSale.add(jsonObject.getString("VC_DR_AMOUNT").toString());
                        listYearWiseSale.add(jsonObject.getString("VC_CR_AMOUNT").toString());
                        // listRunTot.add(jsonObject.getString("RUNTOT").toString());

                    }
                    return listPartyCode + "@%" + listName + "@%" + listNetAmount + "@%" + listMonthWiseSale + "@%" + listYearWiseSale;
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
