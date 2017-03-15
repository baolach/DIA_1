package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionExample extends Activity{

    public TextView tvJSON;
    public Button btnget;
    private static final String jsonurl="http://178.62.50.210";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpurlconnectionexample_activity);
        tvJSON = (TextView) findViewById(R.id.tv);
        btnget =(Button) findViewById(R.id.bt);
        btnget.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    getJSON(jsonurl);
                }
        });
    }

    private void getJSON(String url){
        class GetJSON extends AsyncTask<String, Void, String>{
            protected String doInBackground(String... params){
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json=bufferedReader.readLine()) != null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                tvJSON.setText(s);
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute(url);
    }










//    private final String USER_AGENT = "Mozilla/5.0";
//
//    public static void main(String[] args) throws Exception {
//
//        HttpURLConnectionExample http = new HttpURLConnectionExample();
//
//        System.out.println("Testing 1 - Send Http GET request");
//        http.sendGet();
//
//        System.out.println("\nTesting 2 - Send Http POST request");
//        http.sendPost();
//
//    }
//
//    // HTTP GET request
//    private void sendGet() throws Exception {
//
//        String url = "http://localhost:8000/clients/";
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        // optional default is GET
//        con.setRequestMethod("GET");
//
//        //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//    }
//
//    // HTTP POST request
//    private void sendPost() throws Exception {
//
//        String url = "http://localhost:8000/clients/";
//        URL obj = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//
//        //add reuqest header
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//
//        // Send post request
//        con.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//        wr.writeBytes(urlParameters);
//        wr.flush();
//        wr.close();
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//    }

}