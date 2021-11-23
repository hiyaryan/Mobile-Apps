package edu.asu.bsse.rmenese1.androidapp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * JsonRPC Request via HTTP (JsonRPCRequestViaHTTP.java)
 *
 * This class connects via an HTTP post request to the PlaceJsonRPCServer. It uses JsonRPC
 * sending a request containing the method and parameters and expects a response from the server
 * based on the request. Adopted and refactored from the in-class project
 * StudentJsonRPCClientViaAsyncTask presented by Tim Lindquist.
 *
 * @author Ryan Meneses
 * @version 1.0
 * @since November 23, 2021
 */
public class JsonRPCRequestViaHttp {

    private final Map<String, String> headers;
    private final URL url;

    public JsonRPCRequestViaHttp(URL url) {
        this.url = url;
        this.headers = new HashMap<>();
    }

    public String call(String requestData) throws Exception {
        android.util.Log.d(this.getClass().getSimpleName(),
                "in call, url: " + url.toString() + " requestData: " + requestData);

        return post(url, headers, requestData);
    }

    private String post(URL url, Map<String, String> headers, String data) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream out = connection.getOutputStream()) {
            out.write(data.getBytes());
            out.flush();
            out.close();
            int statusCode = connection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Unexpected status from post: " + statusCode);
            }
        }

        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();

        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();

        } finally {
            if (in != null) {
                in.close();
            }
        }

        android.util.Log.d(this.getClass().getSimpleName(),
                "json rpc request via http returned string "+bos.toString());

        return bos.toString();
    }
}
