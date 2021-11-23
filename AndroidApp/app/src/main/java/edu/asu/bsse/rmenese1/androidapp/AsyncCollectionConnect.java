package edu.asu.bsse.rmenese1.androidapp;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;

import java.net.URL;

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
    }
}
