package com.ccs.ccs81.wyc_aw_ledger;

/**
 * Created by ccs81 on 17/06/2017.
 */

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

/**
 * Created by Banke Bihari on 10/1/2016.
 */
public class EmailWebservice {

    String namespace = "http://tempuri.org/";
    private String url = null;
    String SOAP_ACTION,result=null;
    SoapObject request = null, objMessages = null;
    SoapSerializationEnvelope envelope;
    AndroidHttpTransport androidHttpTransport;
    int check = 0;

    EmailWebservice() {
    }


    /**
     * Set Envelope
     */
    protected void SetEnvelope(String appUrl) {
        url = appUrl + "AirTicketInvoiceAutomail.asmx";
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
    public String getConvertedWeight(String webUrl, String MethodName, String companyID, String invoiceNo, String type, String mobileNo, String emailId, String reportFormat, String pax, String customer, String sales) {

        try {
            SOAP_ACTION = namespace + MethodName;
            // SOAP_ACTION = "http://tempuri.org/AirInvoiceAutoMail";

            //Adding values to request object
            request = new SoapObject(namespace, MethodName);


//            request.addProperty("Compid", "ATB");
//            request.addProperty("Invoiceno", "DD703288");
//            request.addProperty("strtype", "AIR");
//            request.addProperty("strMobNo", "8744840033");
//            request.addProperty("Emailid", "tnj.chaudhary@gmail.com");
            request.addProperty("Compid", "" + companyID.trim());
            request.addProperty("Invoiceno", "" + invoiceNo.trim());
            request.addProperty("strtype", "" + type.trim());
            request.addProperty("strMobNo", "" + mobileNo.trim());
            request.addProperty("Emailid", "" + emailId.trim());
            request.addProperty("ReportFormat", "" + reportFormat.trim());
            request.addProperty("strRMinput", "" + pax.trim());
            request.addProperty("strPrtyOpt", "" + customer.trim());
            request.addProperty("strpsuedOpt", "" + sales.trim());


              SetEnvelope(webUrl);
            try {

                //SOAP calling webservice
                androidHttpTransport.call(SOAP_ACTION, envelope);

                //Got Webservice response
                String result = envelope.getResponse().toString();

                    return result;


            } catch (Exception e) {
                // TODO: handle exception
                return e.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }

    }
    /************************************/
}
