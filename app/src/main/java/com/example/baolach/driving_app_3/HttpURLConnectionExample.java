package com.example.baolach.driving_app_3;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class HttpURLConnectionExample extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpurlconnectionexample_activity);

        System.out.println("TEST");

        String url = "http://138.68.141.18:8001/clients/?format=json"; //urlText.getText().toString();
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }

    }

    @Override
    public void onClick(View v) {
        String url = "http://138.68.141.18:8001/clients"; //urlText.getText().toString();
        ConnectivityManager connmrg = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmrg.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }
    }


    // this is executed first then download url

    private class DownloadWebPageTask extends AsyncTask<String, Void, String>
    {

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
                System.out.println(" ########### content: " + content); // this is the content of the url - ie. the data - this is passed to onPostExectute as result


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

                // this forloop gets the data from the JSONArary json
                for (int i = 0; i < json.length(); i++) {
                    JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
                    text += "Name: " + object.getString("client_name") + ", Phone: \"" + object.getString("client_phone") + "\", Address: " + object.getString("client_address") + "\n\n";
                    String clientname = object.optString("client_name").toString();
                    String clientphone = object.optString("client_phone").toString();
                    String clientaddress= object.optString("client_address").toString();

//                    System.out.println("##### client_name = " + clientname);
//                    System.out.println("##### client_phone = " + clientphone);
//                    System.out.println("##### client_address = " + clientaddress);

                }


                /////////////////
                final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml

                // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        try {
                            Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
                            String theclientsname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
                            String theclientsphone = myCursor.getString(2);
                            String theclientsaddress = myCursor.getString(3);
//                            String theclientslognumber = myCursor.getString(4);
//                            String theclientsdrivernumber = myCursor.getString(5);
//                            String theclientsdob = myCursor.getString(6);
//                            String nooflessons = myCursor.getString(7);
//                            String theclientscomments = myCursor.getString(8);
//                            String thebalance = myCursor.getString(9);


                            Intent i = new Intent(HttpURLConnectionExample.this, ClientInfo.class);

                            i.putExtra("theclientsname", theclientsname);
                            i.putExtra("theclientsphone", theclientsphone);
                            i.putExtra("theclientsaddress", theclientsaddress);
//                            i.putExtra("theclientslognumber", theclientslognumber);
//                            i.putExtra("theclientsdrivernumber", theclientsdrivernumber);
//                            i.putExtra("theclientsdob", theclientsdob);
//                            i.putExtra("nooflessons", nooflessons);
//                            i.putExtra("theclientscomments", theclientscomments);
//                            i.putExtra("thebalance", thebalance);


                            startActivity(i);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });






                ///////////////////////
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(text);
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
    }
}

//// i think I need something like this above
//// GETTING ALL THE CLIENT INFO SQL
//public Cursor getAll() {
//    Cursor mCursor = db.rawQuery("SELECT DISTINCT * FROM Client;", null);
//
//    if (mCursor != null) {
//        mCursor.moveToFirst();
//    }
//
//    return mCursor;
//
//}
//
//    public Cursor getAllLessons() {
//        Cursor mCursor = db.rawQuery("SELECT DISTINCT * FROM Lessons;", null);
//
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//
//        return mCursor;
//    }
//
//    public void deleteClient(String clientName) {
//        Cursor deleteClient = db.rawQuery("DELETE FROM Client WHERE client_name ='"+ clientName + "'",null);
//
//        if (deleteClient != null || deleteClient.getCount() > 0) {
//            deleteClient.moveToFirst();
//        }
//    }













//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.app.Activity;
//
//public class MainActivity extends Activity {
//
//    EditText etResponse;
//    TextView tvIsConnected;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        // get reference to the views
//        etResponse = (EditText) findViewById(R.id.etResponse);
//        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
//
//        // check if you are connected or not
//        if(isConnected()){
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are conncted");
//        }
//        else{
//            tvIsConnected.setText("You are NOT conncted");
//        }
//
//        // show response on the EditText etResponse
//        //etResponse.setText(GET("http://hmkcode.com/examples/index.php"));
//
//        // call AsynTask to perform network operation on separate thread
//        new HttpAsyncTask().execute("http://hmkcode.com/examples/index.php");
//    }
//
//    public static String GET(String url){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        return result;
//    }
//
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
//    public boolean isConnected(){
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }
//
//    // make network operations on a separate thread from the UI. Using AsyncTask class we can simply separate
//    // network operation task from the UI thread.
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            return GET(urls[0]);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
//            etResponse.setText(result);
//        }
//    }
//
//}

// added
/*
We will use an URLConnection for HTTP used to send and receive data over the
web and also create a HttpURLConnection by calling URL.openConnection() and
casting the result to HttpURLConnection and also set the connection timeout,
method type and must be configured with setDoInput(true).

 */
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private EditText urlText;
//    private TextView textView;
//    private Button button;
//    sqlHandler sql;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        urlText = (EditText) findViewById(R.id.editText);
//        textView = (TextView) findViewById(R.id.textView);
//        button = (Button) findViewById(R.id.button);
//        sql = new sqlHandler(this, null, null, 1);
//        button.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        String url = urlText.getText().toString();
//        ConnectivityManager connmrg = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connmrg.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()){
//            new DownloadWebPageTask().execute (url);
//        }
//        else {
//            textView.setText("No network available");
//        }
//    }
//
//    public void createJSONmethod(View view) {
//        Intent intent = new Intent(this, json.class);
//        startActivity (intent);
//    }
//
//    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try{
//                return downloadURL (params[0]);
//            }
//            catch (IOException e){
//                return "Unable to retrieve web page. Check your URL";
//            }
//        }
//
//        protected void onPostExecute (String result){
//            String text = "";
//
//            try {
//                JSONArray json = new JSONArray(result);
//                for (int i=0; i<json.length(); i++){
//                    JSONObject object = json.getJSONObject(i);
//
//                    text += "Task ID " +
//                            object.getString("id") + ", " +
//                            "The title is \"" +
//                            object.getString("title") + "\", " +
//                            "Complete is " + object.getString("completed");
//                    text += "\n\n";
//
//                    String name = object.getString("title");
//                    String desc = "";
//                    int status = object.getString("completed").equals("false") ? 0 : 1;
//
////                    sql.addTask(new Task (name, desc, status));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            textView.setText(text);
//        }
//
//
//        private String downloadURL (String myurl) throws IOException{
//            InputStream is = null;
//            int len = 500;
//
//            try {
//                URL url = new URL(myurl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//
//                conn.connect();
//                int response = conn.getResponseCode();
//                Log.d("DEBUG", "Response code is " + response);
//
//                is = conn.getInputStream();
//
//                String content = parse (is, len);
//
//                return content;
//            }
//            finally {
//                if (is != null)
//                    is.close();
//            }
//        }
//
//        private String parse (InputStream is, int len) throws IOException, UnsupportedEncodingException {
//            return readIt(is);
//        }
//
//        private String readIt(InputStream is) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            String line = null;
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line).append('\n');
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return sb.toString();
//        }
//    }
//}


/////////////////////////////// susans ^^^


//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        // we then call the sendPostRequest() method to post request - sends and receives data form the server - last step
//        try{
//            String sresponse = new SendPostRequest().execute().get();
//
//            Log.i("test", " value of response is " +sresponse );
//            Log.i("test", " about to create json array" );
//            JSONObject test = new JSONObject();
//            JSONArray jArray = new JSONArray(sresponse);
//            String[] array = {"a", "b"};
//
////            JSONArray jArray = new JSONArray(array);
//            Log.i("test", " json array created and about to use json objects" );
//            JSONObject jObject = jArray.getJSONObject(0);
//            String nameString = jObject.getString("client_name");
//
////            TextView tvclient_name = (TextView)findViewById(R.id.textView_client_name);
////            tvclient_name.setText(nameString);
//
//        }catch(Exception e){
//            Log.i("test", " had an error " + e.getMessage());
//        }
//
//    }
//
//    public class SendPostRequest extends AsyncTask<String, Void, String>
//    {
//
//        protected void onPreExecute(){}
//
//        protected String doInBackground(String... arg0) {
//
//            try{
//
//                URL url = new URL("https://studytutorial.in/post.php");
//                //URL url = new URL("http://138.68.141.18:8000/clients/?format=json");
//
//
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("test1", "abc");
//                postDataParams.put("test2", "ghj");
//                postDataParams.put("test3", "wer");
//                postDataParams.put("test4", "lkj");
//
//
//                Log.e("params",postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                // returns the response - encodes the url string of JSONObject. this url string sends the server to get the response. We
//                // get the resoinse via InoutStream(). We read the response through StringBugger object.
//                // then we return the response string into onPostExectute()
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams)); // writes all of the 5 test paramters to
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode=conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    StringBuffer sb = new StringBuffer("");
//                    String line="";
//
//                    while((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    Log.i("test", "response contains" + sb.toString());
//                    return sb.toString();
//
//
//
//
//                }
//                else {
//                    return new String("false : "+responseCode);
//                }
//
//
//            }
//            catch(Exception e){
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            // return the
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
//
//
//        }
//    }
//
//    // converts JSON object to encode url string format - used with the string to be used in a query part of url
//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while(itr.hasNext()){
//
//            String key= itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }
//}


///////////////////////// 4th attempt ^^

//    TextView content;
//    EditText fname,email,login,pass;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        content = (TextView)findViewById(R.id.content);
//        fname	= (EditText)findViewById(R.id.name);
//        email	= (EditText)findViewById(R.id.email);
//        login	= (EditText)findViewById(R.id.loginname);
//        pass	= (EditText)findViewById(R.id.password);
//        Button saveme=(Button)findViewById(R.id.save);
//
//
//        saveme.setOnClickListener(new Button.OnClickListener(){
//            public void onClick(View v)
//            {
//                //ALERT MESSAGE
//                Toast.makeText(getBaseContext(),
//                        "Please wait, connecting to server.",
//                        Toast.LENGTH_LONG).show();
//                try{
//
//                    String loginValue = URLEncoder.encode(login.getText().toString(), "UTF-8");
//                    String fnameValue = URLEncoder.encode(fname.getText().toString(), "UTF-8");
//                    String emailValue = URLEncoder.encode(email.getText().toString(), "UTF-8");
//                    String passValue  = URLEncoder.encode(pass.getText().toString(), "UTF-8");
//
//                    HttpClient Client = new DefaultHttpClient();
//                    String URL = "http://androidexample.com/media/webservice/httpget.php?user="+loginValue+
//                            "&name="+fnameValue+"&email="+emailValue+"&pass="+passValue;
//
//                    //Log.i("httpget", URL);
//                    try
//                    {
//                        HttpGet httpget = new HttpGet(URL);
//                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//
//                        String SetServerString = "";
//                        SetServerString = Client.execute(httpget, responseHandler);
//                        content.setText(SetServerString);
//                    }
//                    catch(Exception ex)
//                    {
//                        content.setText("Fail!");
//                    }
//                }
//                catch(UnsupportedEncodingException ex)
//                {
//                    content.setText("Fail111");
//                }
//            }
//        });
//    }
//}

/////////////////// ^^ attempt 3

//    public TextView tvJSON;
//    public Button btnget;
//    private static final String jsonurl="http://178.62.50.210";
//
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//        tvJSON = (TextView) findViewById(R.id.tv);
//        btnget =(Button) findViewById(R.id.bt);
//        btnget.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    getJSON(jsonurl);
//                }
//        });
//    }
//
//    private void getJSON(String url){
//        class GetJSON extends AsyncTask<String, Void, String>{
//            protected String doInBackground(String... params){
//                String uri = params[0];
//                BufferedReader bufferedReader = null;
//                try{
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json=bufferedReader.readLine()) != null){
//                        sb.append(json+"\n");
//                    }
//                    return sb.toString().trim();
//
//                }catch(Exception e){
//                    return null;
//                }
//
//            }
//
//            @Override
//            protected void onPostExecute(String s){
//                super.onPostExecute(s);
//                tvJSON.setText(s);
//            }
//        }
//
//        GetJSON gj = new GetJSON();
//        gj.execute(url);
//    }
//


/////////////////////////////////////////////// attempt 2 ^^


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

// }




//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.app.Activity;
//
//public class MainActivity extends Activity {
//
//    EditText etResponse;
//    TextView tvIsConnected;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        // get reference to the views
//        etResponse = (EditText) findViewById(R.id.etResponse);
//        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
//
//        // check if you are connected or not
//        if(isConnected()){
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are conncted");
//        }
//        else{
//            tvIsConnected.setText("You are NOT conncted");
//        }
//
//        // show response on the EditText etResponse
//        //etResponse.setText(GET("http://hmkcode.com/examples/index.php"));
//
//        // call AsynTask to perform network operation on separate thread
//        new HttpAsyncTask().execute("http://hmkcode.com/examples/index.php");
//    }
//
//    public static String GET(String url){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        return result;
//    }
//
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
//    public boolean isConnected(){
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }
//
//    // make network operations on a separate thread from the UI. Using AsyncTask class we can simply separate
//    // network operation task from the UI thread.
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            return GET(urls[0]);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
//            etResponse.setText(result);
//        }
//    }
//
//}

// added
/*
We will use an URLConnection for HTTP used to send and receive data over the
web and also create a HttpURLConnection by calling URL.openConnection() and
casting the result to HttpURLConnection and also set the connection timeout,
method type and must be configured with setDoInput(true).

 */
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private EditText urlText;
//    private TextView textView;
//    private Button button;
//    sqlHandler sql;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        urlText = (EditText) findViewById(R.id.editText);
//        textView = (TextView) findViewById(R.id.textView);
//        button = (Button) findViewById(R.id.button);
//        sql = new sqlHandler(this, null, null, 1);
//        button.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        String url = urlText.getText().toString();
//        ConnectivityManager connmrg = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connmrg.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()){
//            new DownloadWebPageTask().execute (url);
//        }
//        else {
//            textView.setText("No network available");
//        }
//    }
//
//    public void createJSONmethod(View view) {
//        Intent intent = new Intent(this, json.class);
//        startActivity (intent);
//    }
//
//    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try{
//                return downloadURL (params[0]);
//            }
//            catch (IOException e){
//                return "Unable to retrieve web page. Check your URL";
//            }
//        }
//
//        protected void onPostExecute (String result){
//            String text = "";
//
//            try {
//                JSONArray json = new JSONArray(result);
//                for (int i=0; i<json.length(); i++){
//                    JSONObject object = json.getJSONObject(i);
//
//                    text += "Task ID " +
//                            object.getString("id") + ", " +
//                            "The title is \"" +
//                            object.getString("title") + "\", " +
//                            "Complete is " + object.getString("completed");
//                    text += "\n\n";
//
//                    String name = object.getString("title");
//                    String desc = "";
//                    int status = object.getString("completed").equals("false") ? 0 : 1;
//
////                    sql.addTask(new Task (name, desc, status));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            textView.setText(text);
//        }
//
//
//        private String downloadURL (String myurl) throws IOException{
//            InputStream is = null;
//            int len = 500;
//
//            try {
//                URL url = new URL(myurl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//
//                conn.connect();
//                int response = conn.getResponseCode();
//                Log.d("DEBUG", "Response code is " + response);
//
//                is = conn.getInputStream();
//
//                String content = parse (is, len);
//
//                return content;
//            }
//            finally {
//                if (is != null)
//                    is.close();
//            }
//        }
//
//        private String parse (InputStream is, int len) throws IOException, UnsupportedEncodingException {
//            return readIt(is);
//        }
//
//        private String readIt(InputStream is) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            String line = null;
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line).append('\n');
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return sb.toString();
//        }
//    }
//}



/////////////////////////////// susans ^^^


//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        // we then call the sendPostRequest() method to post request - sends and receives data form the server - last step
//        try{
//            String sresponse = new SendPostRequest().execute().get();
//
//            Log.i("test", " value of response is " +sresponse );
//            Log.i("test", " about to create json array" );
//            JSONObject test = new JSONObject();
//            JSONArray jArray = new JSONArray(sresponse);
//            String[] array = {"a", "b"};
//
////            JSONArray jArray = new JSONArray(array);
//            Log.i("test", " json array created and about to use json objects" );
//            JSONObject jObject = jArray.getJSONObject(0);
//            String nameString = jObject.getString("client_name");
//
////            TextView tvclient_name = (TextView)findViewById(R.id.textView_client_name);
////            tvclient_name.setText(nameString);
//
//        }catch(Exception e){
//            Log.i("test", " had an error " + e.getMessage());
//        }
//
//    }
//
//    public class SendPostRequest extends AsyncTask<String, Void, String>
//    {
//
//        protected void onPreExecute(){}
//
//        protected String doInBackground(String... arg0) {
//
//            try{
//
//                URL url = new URL("https://studytutorial.in/post.php");
//                //URL url = new URL("http://138.68.141.18:8000/clients/?format=json");
//
//
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("test1", "abc");
//                postDataParams.put("test2", "ghj");
//                postDataParams.put("test3", "wer");
//                postDataParams.put("test4", "lkj");
//
//
//                Log.e("params",postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                // returns the response - encodes the url string of JSONObject. this url string sends the server to get the response. We
//                // get the resoinse via InoutStream(). We read the response through StringBugger object.
//                // then we return the response string into onPostExectute()
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams)); // writes all of the 5 test paramters to
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode=conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    StringBuffer sb = new StringBuffer("");
//                    String line="";
//
//                    while((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    Log.i("test", "response contains" + sb.toString());
//                    return sb.toString();
//
//
//
//
//                }
//                else {
//                    return new String("false : "+responseCode);
//                }
//
//
//            }
//            catch(Exception e){
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            // return the
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
//
//
//        }
//    }
//
//    // converts JSON object to encode url string format - used with the string to be used in a query part of url
//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while(itr.hasNext()){
//
//            String key= itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }
//}








    ///////////////////////// 4th attempt ^^

//    TextView content;
//    EditText fname,email,login,pass;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//
//        content = (TextView)findViewById(R.id.content);
//        fname	= (EditText)findViewById(R.id.name);
//        email	= (EditText)findViewById(R.id.email);
//        login	= (EditText)findViewById(R.id.loginname);
//        pass	= (EditText)findViewById(R.id.password);
//        Button saveme=(Button)findViewById(R.id.save);
//
//
//        saveme.setOnClickListener(new Button.OnClickListener(){
//            public void onClick(View v)
//            {
//                //ALERT MESSAGE
//                Toast.makeText(getBaseContext(),
//                        "Please wait, connecting to server.",
//                        Toast.LENGTH_LONG).show();
//                try{
//
//                    String loginValue = URLEncoder.encode(login.getText().toString(), "UTF-8");
//                    String fnameValue = URLEncoder.encode(fname.getText().toString(), "UTF-8");
//                    String emailValue = URLEncoder.encode(email.getText().toString(), "UTF-8");
//                    String passValue  = URLEncoder.encode(pass.getText().toString(), "UTF-8");
//
//                    HttpClient Client = new DefaultHttpClient();
//                    String URL = "http://androidexample.com/media/webservice/httpget.php?user="+loginValue+
//                            "&name="+fnameValue+"&email="+emailValue+"&pass="+passValue;
//
//                    //Log.i("httpget", URL);
//                    try
//                    {
//                        HttpGet httpget = new HttpGet(URL);
//                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//
//                        String SetServerString = "";
//                        SetServerString = Client.execute(httpget, responseHandler);
//                        content.setText(SetServerString);
//                    }
//                    catch(Exception ex)
//                    {
//                        content.setText("Fail!");
//                    }
//                }
//                catch(UnsupportedEncodingException ex)
//                {
//                    content.setText("Fail111");
//                }
//            }
//        });
//    }
//}

    /////////////////// ^^ attempt 3

//    public TextView tvJSON;
//    public Button btnget;
//    private static final String jsonurl="http://178.62.50.210";
//
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.httpurlconnectionexample_activity);
//        tvJSON = (TextView) findViewById(R.id.tv);
//        btnget =(Button) findViewById(R.id.bt);
//        btnget.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    getJSON(jsonurl);
//                }
//        });
//    }
//
//    private void getJSON(String url){
//        class GetJSON extends AsyncTask<String, Void, String>{
//            protected String doInBackground(String... params){
//                String uri = params[0];
//                BufferedReader bufferedReader = null;
//                try{
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json=bufferedReader.readLine()) != null){
//                        sb.append(json+"\n");
//                    }
//                    return sb.toString().trim();
//
//                }catch(Exception e){
//                    return null;
//                }
//
//            }
//
//            @Override
//            protected void onPostExecute(String s){
//                super.onPostExecute(s);
//                tvJSON.setText(s);
//            }
//        }
//
//        GetJSON gj = new GetJSON();
//        gj.execute(url);
//    }
//



/////////////////////////////////////////////// attempt 2 ^^





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

// }