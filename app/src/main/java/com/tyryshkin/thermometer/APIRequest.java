package com.tyryshkin.thermometer;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;
import java.net.HttpURLConnection;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class APIRequest {

    private static APIRequest APIRequest = null;
    private String rest_base_url = null;
    private String username = "";
    private String password = "";

    public APIRequest() {}

    public void setUrl(String url) throws MalformedURLException {

        this.rest_base_url = url;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    private String encode_credentials(String username, String password) throws UnsupportedEncodingException {
        String credentials = username + ":" + password;
        return android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.DEFAULT);
    }

    public String getBaseUrl() {

        return this.rest_base_url;
    }

    public Object sendRequest(String method, String apiMethod, Object data, boolean attachmentFlag) throws MalformedURLException, IOException, JSONException, RequestException {

        String url = this.rest_base_url + apiMethod;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);

        conn.addRequestProperty("Authorization", "BASIC " + this.encode_credentials(this.username, this.password));

        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT") ) {

            // For attachments in Testrail
            conn.setRequestMethod(method.toUpperCase());


            if (attachmentFlag)   // add_attachment API requests
            {
                String boundary = "a"; //Can be any random string
                File uploadFile = new File((String)data);

                conn.setDoOutput(true);
                conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                OutputStream ostreamBody = conn.getOutputStream();
                BufferedWriter bodyWriter = new BufferedWriter(new OutputStreamWriter(ostreamBody));

                bodyWriter.write("\n\n--" + boundary + "\r\n");
                bodyWriter.write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
                        + uploadFile.getName() + "\"");
                bodyWriter.write("\r\n\r\n");
                bodyWriter.flush();

                //Read file into APIRequest
                InputStream istreamFile = new FileInputStream(uploadFile);
                int bytesRead;
                byte[] dataBuffer = new byte[1024];

                while ((bytesRead = istreamFile.read(dataBuffer)) != -1)
                {
                    ostreamBody.write(dataBuffer, 0, bytesRead);
                }

                ostreamBody.flush();

                //end of attachment, add boundary
                bodyWriter.write("\r\n--" + boundary + "--\r\n");
                bodyWriter.flush();

                //Close streams
                istreamFile.close();
                ostreamBody.close();
                bodyWriter.close();
            }

            else {
                conn.addRequestProperty("Content-Type", "application/json");

                byte[] block = new JSONObject((Map)data).toString().getBytes("UTF-8");

                conn.setDoOutput(true);
                OutputStream ostream = conn.getOutputStream();
                ostream.write(block);
                ostream.close();
            }
        }
        else if (method.equalsIgnoreCase("GET") ) {

            conn.setRequestMethod(method.toUpperCase());
            conn.addRequestProperty("Content-Type", "application/json");

        }

        int returnStatus = conn.getResponseCode();
        InputStream response;

        if (returnStatus != 200) {

            response = conn.getErrorStream();

            String text = "";
            if (response != null)
            {
                BufferedReader reader = new BufferedReader( new InputStreamReader(response, "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    text += line;
                    text += System.getProperty("line.separator");
                }

                reader.close();
            }

            System.out.println("Statusera " + text);
            Object result;

            if (text != "")
            {

                result = new JSONTokener(text).nextValue();

            }
            else
            {
                result = new JSONObject();
            }

            System.out.println("Statusera " + returnStatus + " On API Call: " + this.rest_base_url +apiMethod + "\nServer Msg: " + ((JSONObject)result).getString("error"));

            throw new RequestException(((JSONObject)result).getString("error"));
        }

        else {

            response = conn.getInputStream();

            String text = "";
            if (response != null)
            {
                BufferedReader reader = new BufferedReader( new InputStreamReader(response, "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    text += line;
                    text += System.getProperty("line.separator");
                }

                reader.close();
            }

            Object result;

            if (text != "")
            {

                result = new JSONTokener(text).nextValue();

            }
            else
            {
                result = new JSONObject();
            }

            return result;

        }
    }



}
