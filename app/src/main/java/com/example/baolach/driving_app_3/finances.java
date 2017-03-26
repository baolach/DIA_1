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

/*
when finances button is clicked on mainActivity - the FinancesCursorAdapter is loaded which lists
the clients (ListClients activity) but still in the FinancesActivity
Then you must select the client and ContentFinancesInfo.xml shows the client's payment history when the client is pressed
I need another button on the Finances activity to bring you to the payments section

insert_finance_details.xml - maybe should be UPDATE finance details and be a button option on the ContentFinancesInfo.xml

So maybe it arrives at the client table exactly the same as if the listClients>ClientInfo was selected
But there will be a paymentsActivity which shows just the payment history rather than all the client info


list_finances.xml is connect to Finances.java Activity but lists the clientnames.xml which shows the client name and log number

*/
public class Finances extends Activity
{

    static ArrayList<String> clientsName = new ArrayList<String>();
    static ArrayList<String> clientsPhone = new ArrayList<String>();
    static ArrayList<String> clientsAddress = new ArrayList<String>();
    static ArrayList<String> clientsLogNo = new ArrayList<String>();
    static ArrayList<String> clientsDriverNo = new ArrayList<String>();
    static ArrayList<String> dateOfBirth = new ArrayList<String>();
    static ArrayList<String> noOfLessons = new ArrayList<String>();
    static ArrayList<String> balanceDue = new ArrayList<String>();
    static ArrayList<String> clientComments = new ArrayList<String>();

    ListView listView;
    ArrayList<Client> list;
    FinanceAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_finances); // shows the listView in ListClients activity

        // creates variables to be shown and interacted with in the activity
        // sets up listView and Adapter to accept the data from the urlListView listView = (ListView) findViewById(R.id.listView_clients);
        listView = (ListView) findViewById(R.id.listView_clients); // the listview ID in list_clients.xml
        list = new ArrayList<>();
        adapter = new FinanceAdapter(this, R.layout.finance, list); // this sets adapter to the ClientAdapter which uses client.xml
        listView.setAdapter(adapter); // makes the listview in ListCLients activity output the adapter within the listView

        String url = "http://138.68.141.18:8006/clients/?format=json"; //urlText.getText().toString();
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }

        // the list is generated and the variables added in onPostExecuted. Then if items cliked they are sent with the intent
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = (int) id;
                //String name = clientsName.get(itemId);

                String clientname = clientsName.get(itemId);
                String phone = clientsPhone.get(itemId);
                String address = clientsAddress.get(itemId);
                String log = clientsLogNo.get(itemId);
                String driver = clientsDriverNo.get(itemId);
                String dob = dateOfBirth.get(itemId);
                String lessons = noOfLessons.get(itemId);
                String balance = balanceDue.get(itemId);
                String comments = clientComments.get(itemId);



                // creates new intent and sends over the client information when item is clicked
                Intent i = new Intent(Finances.this, ClientInfo.class);
                i.putExtra("theclientname", clientname);
                i.putExtra("theclientphone", phone);
                i.putExtra("theclientaddress", address);
                i.putExtra("thelognumber", log);
                i.putExtra("thedriverno", driver);
                i.putExtra("thedob", dob);
                i.putExtra("thenumberoflessons", lessons);
                i.putExtra("thebalance", balance);
                i.putExtra("theclientscomments", comments);

                startActivity(i);

            }
        });

    }

    // works in the background
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


        // connects to send the GET request
        // this is executed second then onPostExectute
        private String downloadURL(String myurl) throws IOException {
            InputStream is = null;
            int len = 5000;

            try {
                URL url = new URL(myurl);

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

                // this forloop gets the data into the JSONArray json
                for (int i = 0; i < json.length(); i++) {
                    try {

                        int g = 0;// locates the position in the array for the list item
                        JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
                        text += "Name: " + object.getString("client_name") + ", Phone: \"" + object.getString("client_phone") + "\", Address: " + object.getString("client_address") + "\n\n";
                        String clientname = object.optString("client_name").toString();
                        String clientphone = object.optString("client_phone").toString();
                        String clientaddress = object.optString("client_address").toString();
                        String logno = object.optString("log_no").toString();
                        String driverno = object.optString("driver_no").toString();
                        String dob = object.optString("dob").toString();
                        String nooflessons = object.optString("no_of_lessons").toString();
                        String balancedue = object.optString("balance_due").toString();
                        String comments = object.optString("comments").toString();

                        // adds to the array list
                        clientsName.add(clientname);
                        clientsPhone.add(clientphone);
                        clientsAddress.add(clientaddress);
                        clientsLogNo.add(logno);
                        clientsDriverNo .add(driverno);
                        dateOfBirth.add(dob);
                        noOfLessons.add(nooflessons);
                        balanceDue.add(balancedue);
                        clientComments.add(comments);
                        list.add(new Client(g, clientname, clientphone, clientaddress, logno, driverno, dob, nooflessons, balancedue, comments ));
                        adapter.notifyDataSetChanged();
                        g++;


                        System.out.println("##### Finances.java");
                        System.out.println("##### clientname: " + clientname);
                        System.out.println("##### balance due: " + balancedue);


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


    public void goBackScreen(View view) {

        try {
            Intent lastScreen = new Intent(Finances.this, MainActivity.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }










    // old finances
//    DBManager db = new DBManager(this);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_finances);
//
//        final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml
//        try {
//            db.open();
//            Cursor result = db.getAll();
//            FinancesCursorAdapter cursorAdapter = new FinancesCursorAdapter(Finances.this, result);
//            listView.setAdapter(cursorAdapter);
//
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//        // these below need to be added also
//
//        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
//                try {
////                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
////                    String lessonname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
////                    String lessondate = myCursor.getString(2);
////                    String lessontime = myCursor.getString(3);
////                    String lessonlocation = myCursor.getString(4);
////                    String nooflessons = myCursor.getString(5);
////                    String lessoncomments = myCursor.getString(6);
////
////                    Intent i = new Intent(Finances.this, LessonInfo.class);
////
////                    i.putExtra("thelessonname", lessonname);
////                    i.putExtra("thelessondate", lessondate);
////                    i.putExtra("thelessontime", lessontime);
////                    i.putExtra("thelessonlocation", lessonlocation);
////                    i.putExtra("thelessonlocation", nooflessons);
////                    i.putExtra("thelessoncomments", lessoncomments);
//                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
//                    String theclientsname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
//                    String theclientsphone = myCursor.getString(2);
//                    String theclientsaddress = myCursor.getString(3);
//                    String theclientslognumber = myCursor.getString(4);
//                    String theclientsdrivernumber = myCursor.getString(5);
//                    String theclientsdob = myCursor.getString(6);
//                    String nooflessons = myCursor.getString(7);
//                    String theclientscomments = myCursor.getString(8);
//
//                    Intent i = new Intent(Finances.this, ClientInfo.class);
//
//                    i.putExtra("theclientsname", theclientsname);
//                    i.putExtra("theclientsphone", theclientsphone);
//                    i.putExtra("theclientsaddress", theclientsaddress);
//                    i.putExtra("theclientslognumber", theclientslognumber);
//                    i.putExtra("theclientsdrivernumber", theclientsdrivernumber);
//                    i.putExtra("theclientsdob", theclientsdob);
//                    i.putExtra("nooflessons", nooflessons);
//                    i.putExtra("theclientscomments", theclientscomments);
//
//                    startActivity(i);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//
////    public void listLessonName(View view) {
////        try {
////            Intent lesson_name_intent = new Intent(this, InsertLesson.class);
////            startActivity(lesson_name_intent);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//
//    public void back_screen_btn(View view) {
//        try {
//            Intent adminActivity = new Intent(this, MainActivity.class);
//            startActivity(adminActivity);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }



}
