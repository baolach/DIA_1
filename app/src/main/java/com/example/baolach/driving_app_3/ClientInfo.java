package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientInfo extends Activity {

    //    DBManager db = new DBManager(this);
//    String clientsName;
//    static ArrayList<String> clientsId = new ArrayList<String>();
//    //ListView listView;
//    ArrayList<Client> list;
//    ClientInfoAdapter infoAdapter = null;


    String clientname, clientphone, clientaddress, clientlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_client_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            clientname = bundle.getString("thelessonname");
            clientphone = bundle.getString("theclientphone");
            clientaddress = bundle.getString("theclientaddress");
            clientlocation = bundle.getString("thelessonlocation");
        }

        // apply to textViews
        TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        TextView phoneTextView = (TextView) findViewById(R.id.thelessondate);
        TextView addressTextView = (TextView) findViewById(R.id.thelessontime);
        TextView lognoTextView = (TextView) findViewById(R.id.thelessonlocation);

        nameTextView.setText(clientname);
        phoneTextView.setText(clientphone);
        addressTextView.setText(clientaddress);
        lognoTextView.setText(clientlocation);

        // connects
        String url = "http://138.68.141.18:8001/clients/?format=json"; //urlText.getText().toString();
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new ClientInfo.DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }


    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("DOWNLOAD URL: " + params[0]);
                return downloadURL(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. Check your URL";
            }
        }


        // this is executed second then onPostExectute
        private String downloadURL(String myurl) throws IOException {
            InputStream is = null;
            int len = 500;

            try {
                URL url = new URL(myurl);
                //System.out.println(" ########### Url: " + url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG", "Response code is " + response);

                is = conn.getInputStream();
                //System.out.println(" ########### input stream: " + is);


                String content = parse(is, len);
                // System.out.println(" ########### content: " + content); // this is the content of the url - ie. the data - this is passed to onPostExectute as result


                return content;
            } finally {
                if (is != null)
                    is.close();
            }
        }

//
//        protected void onPostExecute(String result) {
//            String text = "";
//
//            try {
//                //String clientValue = getIntent().getStringExtra("thelessonname");
//                System.out.println("#####");
//                System.out.println(result); // result is the data string
//                System.out.println("#####");
//
//                JSONArray json = new JSONArray(result);
//
////                // this forloop gets the data from the JSONArray json
////                for (int i = 0; i < json.length(); i++) {
////                    try {
////                        int g = 0;
////                        JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
////                        text += "Name: " + object.getString("client_name") + ", Phone: \"" + object.getString("client_phone") + "\", Address: " + object.getString("client_address") + "\n\n";
////                        String clientname = object.optString("client_name").toString();
////                        String clientphone = object.optString("client_phone").toString();
////                        String clientaddress = object.optString("client_address").toString();
////                        String logno = object.optString("log_no").toString();
////
////                        System.out.println(" ########### in the clientInfo.java : " + clientname);
////                        System.out.println(" ########### name: " + clientname);
////                        System.out.println(" ########### phone: " + clientphone);
////                        System.out.println(" ########### address: " + clientaddress);
////                        System.out.println(" ########### logno: " + logno);
//////
////
//////                        clientsId.add(clientname);
//////                        list.add(new Client(g, clientname, clientphone, clientaddress));
//////                        infoAdapter.notifyDataSetChanged();
//////                        g++;
////
////
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }// end onCreate
//
//
//        }
//    }

        private String parse(InputStream is, int len) throws IOException, UnsupportedEncodingException {
            return readIt(is);
        }

        private String readIt(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        public void updateClient(View view)
        {
            // update client by sending a post request or something
        }




    }
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListClients.class);
            startActivity(lastScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
