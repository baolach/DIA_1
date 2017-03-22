package com.example.baolach.driving_app_3;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

// This class displays the client names in a list to the user. You can press a button for insert, delete client also
public class ListClients extends Activity {
    // DBManager db = new DBManager(this);
    static ArrayList<String> clientsId = new ArrayList<String>();
    ListView listView;
    ArrayList<Client> list;
    ClientAdapter adapter = null;
    ClientInfoAdapter infoAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_clients); // shows the listView in ListClients activity

        // sets up listView and Adapter to accept the data from the urlListView listView = (ListView) findViewById(R.id.listView_clients);
        listView = (ListView) findViewById(R.id.listView_clients); // the listview ID in list_clients.xml
        list = new ArrayList<>(); // makes new arrayList

        adapter = new ClientAdapter(this, R.layout.client, list); // this sets adapter to the ClientAdapter which uses client.xml
        //infoAdapter = new ClientInfoAdapter((this, R.layout.client, list); // for the onClick

        listView.setAdapter(adapter); // makes the listview in ListCLients activity output the adapter within the listView
        //nfoListView




        String url = "http://138.68.141.18:8001/clients/?format=json"; //urlText.getText().toString();
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }





//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                int itemId = (int) id;
//                String haha = clientsId.get(itemId);
//                Toast.makeText(getBaseContext(), "Client: " + haha , Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });

        //listView.setOnItemClickListener(this);

//        final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml
//        try {
//            db.open();
//            Cursor result = db.getAll();
//            ClientCursorAdapter cursorAdapter = new ClientCursorAdapter(ListClients.this, result);
//            listView.setAdapter(cursorAdapter);
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int itemId = (int) id;
                    //String clientname = clientsId.get(itemId);
//                    Toast.makeText(getBaseContext(), "Client: " + haha , Toast.LENGTH_LONG).show();
//
                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
                    String clientname = clientsId.get(itemId);

//                    String theclientsname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
//                    String theclientsphone = myCursor.getString(2);
//                    String theclientsaddress = myCursor.getString(3);
                    System.out.println("String: client" + clientname);

//                    String theclientslognumber = myCursor.getString(4);
//                    String theclientsdrivernumber = myCursor.getString(5);
//                    String theclientsdob = myCursor.getString(6);
//                    String nooflessons = myCursor.getString(7);
//                    String theclientscomments = myCursor.getString(8);
//                    String thebalance = myCursor.getString(9);


                    Intent i = new Intent(ListClients.this, ClientInfo.class);

                    i.putExtra("theclientsname", clientname);
//                    i.putExtra("theclientsphone", theclientsphone);
//                    i.putExtra("theclientsaddress", theclientsaddress);
                    System.out.println("String being sent through: client" + clientname);

//                    i.putExtra("theclientslognumber", theclientslognumber);
//                    i.putExtra("theclientsdrivernumber", theclientsdrivernumber);
//                    i.putExtra("theclientsdob", theclientsdob);
//                    i.putExtra("nooflessons", nooflessons);
//                    i.putExtra("theclientscomments", theclientscomments);
//                    i.putExtra("thebalance", thebalance);


                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
//



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
                        clientsId.add(clientname);
                        list.add(new Client(g, clientname, clientphone, clientaddress));
                        adapter.notifyDataSetChanged();
                        g++;

                        ////////////////
//                        final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml
//                        try {
//                            Cursor mCursor = result.getAll();
//                            ClientCursorAdapter cursorAdapter = new ClientCursorAdapter(HttpURLConnectionExample.this, result);
//                            listView.setAdapter(cursorAdapter);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }

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

        public void listClientName(View view)
    {
        try {
            Intent client_name_intent = new Intent(this, InsertClient.class);
            startActivity(client_name_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goBackScreen(View view) {

        try {
            Intent lastScreen = new Intent(this, AdminActivity.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}



