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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// This class displays the client names in a list to the user
public class ListLessons extends Activity
{

    // making an array list to put the lesson variables being read in from the database
    static ArrayList<String> lessonName = new ArrayList<String>();
    static ArrayList<String> lessonDate = new ArrayList<String>();
    static ArrayList<String> lessonTime = new ArrayList<String>();
    static ArrayList<String> lessonLocation = new ArrayList<String>();
    static ArrayList<String> lessonComments = new ArrayList<String>();
    static ArrayList<String> lessonId = new ArrayList<String>();

    ListView listView; // blue listview
    ArrayList<Lesson> list;
    LessonAdapter adapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lessons);

        lessonName.clear();
        lessonDate.clear();
        lessonTime.clear();
        lessonLocation.clear();
        lessonComments.clear();
        lessonId.clear();


        // creates variables to be shown and interacted with in the activity
        // sets up listView and Adapter to accept the data from the url
        listView = (ListView) findViewById(R.id.listView_lessons); // the listview ID in list_clients.xml
        list = new ArrayList<>();
        adapter = new LessonAdapter(this, R.layout.lessoninfo, list); // this sets adapter to the ClientAdapter which uses client.xml
        listView.setAdapter(adapter); // sets the listview in ListLesson activity output the adapter within the listView

        // connects to the url containing the lessons in the database
        String url = "http://138.68.141.18:8006/lessons/?format=json";
        ConnectivityManager connmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connmgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebPageTask().execute(url);
        } else {
            System.out.println("No network available");
        }

        // the list is declared above and generated and the variables added in onPostExecuted.
        // Then if items clicked they are sent with the intent to the LessonsInfo activity


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // itemId keeps track of where in the list it is
                int itemId = (int) id;

                String lessonname = lessonName.get(itemId);
                String lessondate = lessonDate.get(itemId);
                String lessontime = lessonTime.get(itemId);
                String lessonlocation = lessonLocation.get(itemId);
                String lessoncomments = lessonComments.get(itemId);
                String lessonid = lessonId.get(itemId);

                // creates new intent and sends over the lesson information when item is clicked
                // accepted by Bundle in LessonInfo
                Intent i = new Intent(ListLessons.this, LessonInfo.class);
                i.putExtra("thelessonname", lessonname);
                i.putExtra("thelessondate", lessondate);
                i.putExtra("thelessontime", lessontime);
                i.putExtra("thelessonlocation", lessonlocation);
                i.putExtra("thelessoncomments", lessoncomments);
                i.putExtra("id", lessonid);

                startActivity(i);

            }
        });



    } // end on create

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
                String content = parse(is, len);
                return content;
            } finally {
                if (is != null)
                    is.close();
            }
        }

        protected void onPostExecute(String result) {
            // the url is declared in onCreate, here the JSONObject is created and the data from the JSON string from the url is added to the appropriate variable
            try {
                JSONArray json = new JSONArray(result);

                // this forloop gets the data into the JSONArray json for each client lesson and displays in list
                for (int i = 0; i < json.length(); i++) {
                    try {

                        int g = 0;// locates the position in the array for the list item
                        JSONObject object = json.getJSONObject(i); // reads the array into the JSONOBject object
                        String lessonname = object.optString("lesson_name").toString();
                        String lessondate = object.optString("lesson_date").toString();
                        String lessontime = object.optString("lesson_time").toString();
                        String lessonlocation = object.optString("lesson_location").toString();
                        String lessoncomments = object.optString("lesson_comments").toString();
                        String lessonid = object.optString("id").toString(); // id from the db is sent to keep unique

                        // adds to the array list
                        // the adapter then takes this list and loads it into a format in LessonInfo.xml which is then displayed
                        lessonName.add(lessonname);
                        lessonDate.add(lessondate);
                        lessonTime.add(lessontime);
                        lessonLocation.add(lessonlocation);
                        lessonComments .add(lessoncomments);
                        lessonId.add(lessonid);


                        list.add(new Lesson(g, lessonname, lessondate, lessontime, lessonlocation, lessoncomments, lessonid));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }// end onCreate

        }
    }





    // parses the url and reads the JSON in as a string which is readable
    private String parse(InputStream is, int len) throws IOException {
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


    public void newLesson(View view)
    {
        try {
            Intent client_name_intent = new Intent(this, SelectClient.class);
            Toast.makeText(getBaseContext(), "Select client for the lesson", Toast.LENGTH_LONG).show();
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

    public void home(View view)
    {
        try {
            Intent home_intent = new Intent(this, MainActivity.class);
            startActivity(home_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

