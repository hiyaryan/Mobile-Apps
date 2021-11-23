package edu.asu.bsse.rmenese1.androidapp;

/**
 * Method Information (MethodInformation.java)
 *
 * This class is a helper class for AsyncCollectionConnect.java to set up information on the
 * method to be executed asynchronously. Adopted from the in-class project
 * StudentJsonRPCClientViaAsyncTask presented by Tim Lindquist.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 23, 2021
 */
public class MethodInformation {
    public String method;
    public Object[] params;
    public MainActivity parent;
    public String urlString;
    public String resultAsJson;

    MethodInformation(MainActivity parent, String urlString, String method, Object[] params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}