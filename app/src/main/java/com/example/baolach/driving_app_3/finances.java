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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    static ArrayList<String> clientId = new ArrayList<String>();

    static ArrayList<String> expenseName = new ArrayList<String>();
    static ArrayList<String> expenseAmount = new ArrayList<String>();
    static ArrayList<String> expenseDate = new ArrayList<String>();
    static ArrayList<String> expenseId = new ArrayList<String>();



    ListView listView, listView2;
    ArrayList<Client> list;
    ArrayList<Expense> list2;
    FinanceAdapter adapter = null;
    ExpenseAdapter adapter2 = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_finances); // shows the listView in ListClients activity

        clientsName.clear();
        clientsPhone.clear();
        clientsAddress.clear();
        clientsLogNo.clear();
        clientsDriverNo.clear();
        dateOfBirth.clear();
        noOfLessons.clear();
        balanceDue.clear();
        clientComments.clear();
        clientId.clear();

        expenseName.clear();
        expenseAmount.clear();
        expenseDate.clear();
        expenseId.clear();


        // creates variables to be shown and interacted with in the activity
        // sets up listView and Adapter to accept the data from the urlListView listView = (ListView) findViewById(R.id.listView_clients);
        listView = (ListView) findViewById(R.id.listView_clients); // the listview ID in list_clients.xml
        list = new ArrayList<>();
        adapter = new FinanceAdapter(this, R.layout.finance, list); // this sets adapter to the ClientAdapter which uses client.xml
        listView.setAdapter(adapter); // makes the listview in ListCLients activity output the adapter within the listView

        listView2 = (ListView) findViewById(R.id.listView_expenses); // the listview ID in list_clients.xml
        list2 = new ArrayList<>();
        adapter2 = new ExpenseAdapter(this, R.layout.finance, list2); // this sets adapter to the ClientAdapter which uses client.xml
        listView2.setAdapter(adapter2); // makes the listview in ListCLients activity output the adapter within the listView



//        String[] array = new String[]{"red", "blue", "green", "black", "white"};
//        ListView lView = (ListView) findViewById(R.id.listView_expenses);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
//        listView2.setAdapter(adapter2);

//        listView2 = (ListView) findViewById(R.id.listView_expenses); // the listview ID in list_clients.xml
//        list2 = new ArrayList<>();
//        adapter = new FinanceAdapter(this, R.layout.finance, list2); // this sets adapter to the ClientAdapter which uses client.xml
//        listView2.setAdapter(adapter); // makes the listview in ListCLients activity output the adapter within the listView


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
                String clientid = clientId.get(itemId);



                // creates new intent and sends over the client information when item is clicked
                Intent i = new Intent(Finances.this, FinanceInfo.class);
                i.putExtra("theclientname", clientname);
                i.putExtra("theclientphone", phone);
                i.putExtra("theclientaddress", address);
                i.putExtra("thelognumber", log);
                i.putExtra("thedriverno", driver);
                i.putExtra("thedob", dob);
                i.putExtra("thenumberoflessons", lessons);
                i.putExtra("thebalance", balance);
                i.putExtra("theclientscomments", comments);
                i.putExtra("id", clientid);

                startActivity(i);

            }
        });

        ////////////////////////


        Thread thread = new Thread() {

            public void run() {
                try {



                    PreparedStatement st = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    // prepares the sql statement
                    String select = "select * from getdata_getexpense;"; //

                    st = conn.prepareStatement(select);
//                  st.setString(1, expenseName);
                    //st.setString(2, clientid);
                    ResultSet rs = st.executeQuery();

                    while (rs.next()) {
                        final int g = 0;
                        final String expensename = rs.getString("expense_name");
                        final String expenseamount = rs.getString("expense_amount");
                        final String expensedate = rs.getString("expense_date");
                        final String expenseid = rs.getString("id");


                        runOnUiThread(new Runnable() {
                            public void run() {

                                expenseName.add(expensename);
                                expenseAmount.add(expenseamount);
                                expenseDate.add(expensedate);
                                expenseId.add(expenseid);


                                list2.add(new Expense(g, expensename, expenseamount, expensedate, expenseid));
                                adapter2.notifyDataSetChanged();

                            }
                        });
                    }

                    st.close();
                    conn.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();


        // dont think I need an listener, all the info is there
//        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // itemId keeps track of where in the list it is
//                int itemId = (int) id;
//
//                String expensename = expenseName.get(itemId);
//                String expenseamount = expenseAmount.get(itemId);
//                String expensedate = expenseDate.get(itemId);
//                String expenseid = expenseId.get(itemId);
//
//
//                // creates new intent and sends over the lesson information when item is clicked
//                // accepted by Bundle in LessonInfo
//                Intent i = new Intent(Finances.this, ExpenseInfo.class);
//                i.putExtra("theexpensename", expensename);
//                i.putExtra("theexpenseamount", expenseamount);
//                i.putExtra("theexpensedate", expensedate);
//                i.putExtra("id", expenseid);
//
//
//                startActivity(i);
//
//            }
//        });



    } // end onCreate

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
                        //text += "Name: " + object.getString("client_name") + ", Phone: \"" + object.getString("client_phone") + "\", Address: " + object.getString("client_address") + "\n\n";
                        String clientname = object.optString("client_name").toString();
                        String clientphone = object.optString("client_phone").toString();
                        String clientaddress = object.optString("client_address").toString();
                        String logno = object.optString("log_no").toString();
                        String driverno = object.optString("driver_no").toString();
                        String dob = object.optString("dob").toString();
                        String nooflessons = object.optString("no_of_lessons").toString();
                        String balancedue = object.optString("balance_due").toString();
                        String comments = object.optString("comments").toString();
                        String clientid = object.optString("id").toString(); // id from the db is sent to keep unique

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
                        clientId.add(clientid);

                        list.add(new Client(g, clientname, clientphone, clientaddress, logno, driverno, dob, nooflessons, balancedue, comments, clientid ));
                        adapter.notifyDataSetChanged();


//                        System.out.println("##### Finances.java");
//                        System.out.println("##### clientname: " + clientname);
//                        System.out.println("##### balance due: " + balancedue);


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



}