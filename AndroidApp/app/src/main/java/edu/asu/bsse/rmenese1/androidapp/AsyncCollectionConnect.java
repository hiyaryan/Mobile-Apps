package edu.asu.bsse.rmenese1.androidapp;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Async Collection Connect (AsyncCollectionConnect.java)
 *
 * This class sets up an asynchronous connection prior to connecting to the PlaceJsonRPCServer.
 * Adopted and refactored from the in-class project StudentJsonRPCClientViaAsyncTask presented by
 * Tim Lindquist.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 23, 2021
 */
public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {
    @Override
    protected void onPreExecute() {
        android.util.Log.d(this.getClass().getSimpleName(),
                "in onPreExecute on " + (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... request) {
        android.util.Log.d(this.getClass().getSimpleName(),
                "in doInBackground on " + (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));

        try {
            JSONArray params = new JSONArray(request[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),
                    "params: " + params.toString());

            String requestData = "{"
                    + "\"jsonrpc\": \"2.0\", "
                    + "\"method\": \"" + request[0].method + "\", "
                    + "\"params\": " + params.toString() + ", "
                    + "\"id\": 3"
                    + "}";

            android.util.Log.d(this.getClass().getSimpleName(),
                    "requestData: " + requestData + " url: " + request[0].urlString);

            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(request[0].urlString)));
            request[0].resultAsJson = conn.call(requestData);

        } catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),
                    "exception in remote call " + ex.getMessage());
        }
        return request[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res) {
        android.util.Log.d(this.getClass().getSimpleName(),
                "in onPostExecute on " + (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));

        android.util.Log.d(this.getClass().getSimpleName(),
                " resulting is: " + res.resultAsJson);

        try {
            switch (res.method) {
                case "getNames": {
                    JSONObject jo = new JSONObject(res.resultAsJson);
                    JSONArray ja = jo.getJSONArray("result");

                    ArrayList<String> al = new ArrayList<String>();
                    for (int i = 0; i < ja.length(); i++) {
                        al.add(ja.getString(i));
                    }

                    String[] names = al.toArray(new String[0]);
                    for (int i = 0; i < names.length; i++) {

                    }
                    if (names.length > 0) {
                        try {
                            MethodInformation mi = new MethodInformation(res.parent, res.urlString, "get", new String[]{names[0]});
                            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                        } catch (Exception ex) {
                            android.util.Log.w(this.getClass().getSimpleName(),
                                    "Exception processing spinner selection: " + ex.getMessage());
                        }
                    }
                    break;
                }
                case "get": {
                    JSONObject jo = new JSONObject(res.resultAsJson);

                    break;
                }
                case "add":
                    try {
                        MethodInformation mi = new MethodInformation(res.parent, res.urlString, "getNames", new Object[]{});
                        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);

                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception processing getNames: " + ex.getMessage());
                    }

                    break;
            }

        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(),
                    "Exception: " + ex.getMessage());
        }
    }
}
