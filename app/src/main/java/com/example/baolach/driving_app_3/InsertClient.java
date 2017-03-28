package com.example.baolach.driving_app_3;

// http://hmkcode.com/android-send-json-data-to-server/
// https://www.tutorialspoint.com/android/android_datepicker_control.htm


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class InsertClient extends Activity{// } implements OnClickListener {

//        //DBManager db = new DBManager(this);
//
//    //AutoCompleteTextView clientName;
//    EditText clientName;
//    EditText clientPhone;
//    EditText clientAddress;
//    EditText clientLogNo;
//    EditText clientDriverNo;
//    EditText clientDob;
//    EditText clientNoOfLessons;
//    EditText clientsComments;
//    EditText clientBalance;
//    Button btnPost;
//
//    Client client;

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText clientName,clientPhone,clientAddress,clientLogNo,clientDriverNo,clientDob,clientNoOfLessons, clientsComments, clientBalance;
    private Button btnPost;
    private int year, month, day;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_client_details);

//        calendar = Calendar.getInstance();
//
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        showDate(year, month+1, day);


        clientName = (EditText) findViewById(R.id.editText_clientName);
        clientPhone = (EditText) findViewById(R.id.editText_clientPhone);
        clientAddress = (EditText) findViewById(R.id.editText_clientAddress);
        clientLogNo = (EditText) findViewById(R.id.editText_clientLogNo);
        clientDriverNo = (EditText) findViewById(R.id.editText_clientDriverNo);
        clientDob = (EditText) findViewById(R.id.editText_clientDob);
        clientNoOfLessons = (EditText) findViewById(R.id.editText_clientNoOfLessons);
        clientsComments = (EditText) findViewById(R.id.editText_clientComments);
        clientBalance = (EditText) findViewById(R.id.editText_clientBalance);

        btnPost = (Button) findViewById(R.id.button_submit);



        btnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new Thread(new Runnable() {

                    public void run() {
                        insert();
                    }

                }).start();




            }

            protected void insert() {

                try {
                    String c_name = clientName.getText().toString();
                    String c_phone = clientPhone.getText().toString();
                    String c_address = clientAddress.getText().toString();
                    String c_log = clientLogNo.getText().toString();
                    String c_d_no = clientDriverNo.getText().toString();
                    String c_dob = clientDob.getText().toString();
                    String c_no_l = clientNoOfLessons.getText().toString();
                    String c_comm = clientsComments.getText().toString();
                    String c_bal = clientBalance.getText().toString();
                    PreparedStatement st2 = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2";
                    Connection c = DriverManager.getConnection(url, "root", "Cassie2007");


                    String inss = "insert into getdata_getclient values (30,?,?,?,?,?,?,?,?,?)";
                    st2 = c.prepareStatement(inss);
                    st2.setString(1, c_name);
                    st2.setString(2, c_phone);
                    st2.setString(3, c_address);
                    st2.setString(4,  c_log);
                    st2.setString(5, c_d_no);
                    st2.setString(6, c_dob);
                    st2.setString(7, c_no_l);
                    st2.setString(8, c_bal);
                    st2.setString(9, c_comm);
                    st2.execute();
                    st2.close();


                    runOnUiThread(new Runnable() {
                        public void run() {

                            clientName.setText("");
                            clientPhone.setText("");
                            clientAddress.setText("");
                            clientLogNo.setText("");
                            clientDriverNo .setText("");
                            clientDob .setText("");
                            clientNoOfLessons .setText("");
                            clientsComments .setText("");
                            clientBalance .setText("");
                            Toast.makeText(getBaseContext(), "Client has been added to the database! ", Toast.LENGTH_LONG).show();

                        }
                    });


                    st2.close();
                    c.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    } // end on create


    //    @SuppressWarnings("deprecation")
//    public void setDate(View view) {
//        showDialog(999);
//        Toast.makeText(getApplicationContext(), "Pick a date", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        // TODO Auto-generated method stub
//        if (id == 999) {
//            return new DatePickerDialog(this, myDateListener, year, month, day);
//        }
//        return null;
//    }
//
//    private DatePickerDialog.OnDateSetListener myDateListener = new
//            DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//                    // so its getting here - outputting the current date and time correctly but no values
//                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//                    System.out.println(" ########## date picker -  date: " + currentDateTimeString);
//                    arg1 = year;
//                     arg2 = month;
//                     arg3 = day;
//                    System.out.println(" ########## date picker -  year: " + arg1);
//                    System.out.println(" ########## date picker -  month: " + arg2);
//                    System.out.println(" ########## date picker -  day: " + arg3);
//
//
//                    showDate(arg1, arg2+1, arg3);
//                }
//            };
//
//    private void showDate(int year, int month, int day) {
//        clientDob.setText("");
//        clientDob.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
//
//    }
//
//    public static String POST(String url, Client client){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // 1. create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // 2. make POST request to the given URL
//            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // his way
//            // 3. build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("client_name", client.getName());
//            jsonObject.accumulate("client_phone", client.getName());
//            jsonObject.accumulate("client_address", client.getAddress());
//            jsonObject.accumulate("log_no", client.getLogno());
//            jsonObject.accumulate("driver_no", client.getDriverno());
//            jsonObject.accumulate("dob", client.getDob());
//            jsonObject.accumulate("no_of_lessons", client.getNooflessons());
//            jsonObject.accumulate("balance_due", client.getBalancedue());
//            jsonObject.accumulate("comments", client.getComments());
//
//
//            // 4. convert JSONObject to JSON to String
//            json = jsonObject.toString();
//
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        // 11. return result
//        return result;
//    }
//
//
//    public boolean isConnected(){
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }
//
//
//    @Override
//    public void onClick(View view) {
//
//        switch(view.getId()){
//            case R.id.button_submit:
//                if(!validate())
//                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
//                // call AsynTask to perform network operation on separate thread
//                new HttpAsyncTask().execute("http://138.68.141.18:8006/clients/?format=json");
//                break;
//        }
//
//    }
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        // his
//        @Override
//        protected String doInBackground(String... urls) {
//
//            client = new Client();
//            client.setName(clientName.getText().toString());
//            client.setPhone(clientPhone.getText().toString());
//            client.setAddress(clientAddress.getText().toString());
//            client.setLogno(clientLogNo.getText().toString());
//            client.setDriverno(clientDriverNo.getText().toString());
//            client.setDob(clientDob.getText().toString());
//            client.setNooflessons(clientNoOfLessons.getText().toString());
//            client.setComments(clientsComments.getText().toString());
//            client.setBalancedue(clientBalance.getText().toString());
//
//            return POST(urls[0],client);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//
//    private boolean validate(){
//        if(clientName.getText().toString().trim().equals(""))
//            return false;
//        else if(clientPhone.getText().toString().trim().equals(""))
//            return false;
//        else if(clientAddress.getText().toString().trim().equals(""))
//            return false;
//        else if(clientLogNo.getText().toString().trim().equals(""))
//            return false;
//        else if(clientDriverNo.getText().toString().trim().equals(""))
//            return false;
//        else if(clientDob.getText().toString().trim().equals(""))
//            return false;
//        else if(clientNoOfLessons.getText().toString().trim().equals(""))
//            return false;
//        else if(clientBalance.getText().toString().trim().equals(""))
//            return false;
//        else if(clientsComments.getText().toString().trim().equals(""))
//            return false;
//        else
//            return true;
//    }
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }
//
    // An intent for the user to go back to the main screen
    public void goBackScreen(View view) {
        try {
            Intent lastScreen = new Intent(this, ListClients.class);
            startActivity(lastScreen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void home(View view)
    {
        try {
            Intent home_intent = new Intent(this, MainActivity.class);
            startActivity(home_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


} // end class