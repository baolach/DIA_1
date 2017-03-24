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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ClientInfo extends Activity {

//    DBManager db = new DBManager(this);
//    String clientsName;
    static ArrayList<String> clientsId = new ArrayList<String>();
    //ListView listView;
    ArrayList<Client> list;
    ClientInfoAdapter infoAdapter = null;

    TextView clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_client_info);





        // sets up listView and Adapter to accept the data from the urlListView listView = (ListView) findViewById(R.id.listView_clients);
        //listView = (ListView) findViewById(R.id.listView_infoclients); // the listview ID in list_clients.xml
        // list = new ArrayList<>(); // makes new arrayList

        //clientname = (TextView) findViewById(R.id.thelessonname);
        //clientname.setAdapter(infoAdapter);


        //infoAdapter = new ClientInfoAdapter(this, R.layout.clientinfo, list); // this sets adapter to the ClientAdapter which uses client.xml

        //listView.setAdapter(infoAdapter = new ClientInfoAdapter(this, R.layout.client, list)); // this sets adapter to the ClientAdapter which uses client.xml



        String url = "http://138.68.141.18:8001/clients/?format=json"; //urlText.getText().toString();
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new ClientInfo.DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }


//        Intent clientData = getIntent();
//
//        //put data from list clients activity into new string so you can delete it
//        clientsName = clientData.getStringExtra("theclientsname");
//        String TheClientsPhone= clientData.getStringExtra("theclientsphone");
//        String TheClientsAddress = clientData.getStringExtra("theclientsaddress");
//        String TheClientsLogNumber = clientData.getStringExtra("theclientslognumber");
//        String TheClientDriverNumber = clientData.getStringExtra("theclientsdrivernumber");
//        String TheClientsDob= clientData.getStringExtra("theclientsdob");
//        String NoOfLessons= clientData.getStringExtra("numberoflessons");
//        String TheClientsComments = clientData.getStringExtra("theclientscomments");
//        String TheClientBalance = clientData.getStringExtra("thebalance");
//
//
//        //create variable which references output field
//        final TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
//        final TextView phoneTextView = (TextView) findViewById(R.id.thelessondate);
//        final TextView addressTextView = (TextView) findViewById(R.id.thelessontime);
//        final TextView lognoTextView = (TextView) findViewById(R.id.thelessonlocation);
//        final TextView drivernoTextView = (TextView) findViewById(R.id.thelessoncomments);
//
//        final TextView dobTextView = (TextView) findViewById(R.id.theclientsdob);
//        final TextView nooflessonsTextView = (TextView) findViewById(R.id.numberoflessons);
//        final TextView commentsTextView = (TextView) findViewById(R.id.theclientscomments);
//        final TextView balanceTextView = (TextView) findViewById(R.id.thebalance);
//
//
//        // setting the TextViews to display what the info the user entered
//        nameTextView.setText(clientsName);
//        phoneTextView.setText(TheClientsPhone);
//        addressTextView.setText(TheClientsAddress);
//        lognoTextView.setText(TheClientsLogNumber);
//        drivernoTextView.setText(TheClientDriverNumber);
//        dobTextView.setText(TheClientsDob);
//        nooflessonsTextView.setText(NoOfLessons);
//        commentsTextView.setText(TheClientsComments);
//        balanceTextView.setText(TheClientBalance);
//
//
//
//        Button deleteButton = (Button)findViewById(R.id.delete_client_btn);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                DBManager dbManager = new DBManager(ClientInfo.this);
//                try {
//                    dbManager.open();
//                    dbManager.deleteClient(clientsName);
//                    Toast.makeText(getApplicationContext(), "Client deleted", Toast.LENGTH_SHORT).show();
//
//                } catch (SQLException e) {
//                    Toast.makeText(getApplicationContext(), "Client could not be deleted", Toast.LENGTH_SHORT).show();
//                } finally {
//                    dbManager.close();
//
//                    Intent intent = new Intent(ClientInfo.this, ListClients.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears previous screens
//                    startActivity(intent); // this loads new intent (ListClients
//
//                }
//
//            }
//        });


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


        protected void onPostExecute(String result) {
            String text = "";

            try {
                  String clientValue = getIntent().getStringExtra("thelessonname");

//                String phoneValue = getIntent().getStringExtra("thelessondate");
//                String addressValue = getIntent().getStringExtra("thelessontime");
//                String logNoValue = getIntent().getStringExtra("thelessonlocation");






                //clientName.setText(value);
                //System.out.println(clientName);

                //String TheClientsPhone= i.getStringExtra("theclientsphone");
                //String key = getIntent().getStringExtra("thelessonname");

                TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
                nameTextView.setText(clientValue);
//                TextView phoneTextView = (TextView) findViewById(R.id.thelessondate);
//                nameTextView.setText(phoneValue);
//                TextView addressTextView = (TextView) findViewById(R.id.thelessontime);
//                nameTextView.setText(addressValue);
//                TextView logNoTextView = (TextView) findViewById(R.id.thelessonlocation);
//                nameTextView.setText(logNoValue);



                System.out.println("#####");
                System.out.println(result); // result is the data string
                System.out.println("#####");

                JSONArray json = new JSONArray(result);

                // this forloop gets the data from the JSONArray json
                for (int i = 0; i < json.length(); i++) {
                    try {
                        int g = 0;
                        JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
                        text += "Name: " + object.getString("client_name") + ", Phone: \"" + object.getString("client_phone") + "\", Address: " + object.getString("client_address") + "\n\n";
                        String clientname = object.optString("client_name").toString();
                        String clientphone = object.optString("client_phone").toString();
                        String clientaddress = object.optString("client_address").toString();
                        String logno = object.optString("log_no").toString();

                        System.out.println(" ########### in the clientInfo.java : " + clientname);
                        System.out.println(" ########### name: " + clientname);
                        System.out.println(" ########### phone: " + clientphone);
                        System.out.println(" ########### address: " + clientaddress);
                        System.out.println(" ########### logno: " + logno);


//                        clientsId.add(clientname);
//                        list.add(new Client(g, clientname, clientphone, clientaddress));
//                        infoAdapter.notifyDataSetChanged();
//                        g++;



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }// end onCreate


        }
    }

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

//        public void listClientName(View view)
//        {
//            try {
//                Intent client_name_intent = new Intent(this, InsertClient.class);
//                startActivity(client_name_intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListClients.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }




}
