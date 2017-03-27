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

// This class displays the client names in a list to the user
public class ListLessons extends Activity
{

    static ArrayList<String> lessonName = new ArrayList<String>();
    static ArrayList<String> lessonDate = new ArrayList<String>();
    static ArrayList<String> lessonTime = new ArrayList<String>();
    static ArrayList<String> lessonLocation = new ArrayList<String>();
    static ArrayList<String> lessonComments = new ArrayList<String>();


    ListView listView;
    ArrayList<Lesson> list;
    LessonAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lessons);


        // creates variables to be shown and interacted with in the activity
        // sets up listView and Adapter to accept the data from the urlListView listView = (ListView) findViewById(R.id.listView_clients);
        listView = (ListView) findViewById(R.id.listView_lessons); // the listview ID in list_clients.xml
        list = new ArrayList<>();
        adapter = new LessonAdapter(this, R.layout.lessoninfo, list); // this sets adapter to the ClientAdapter which uses client.xml
        listView.setAdapter(adapter); // makes the listview in ListCLients activity output the adapter within the listView

        String url = "http://138.68.141.18:8006/lessons/?format=json"; //urlText.getText().toString();
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

                String lessonname = lessonName.get(itemId);
                String lessondate = lessonDate.get(itemId);
                String lessontime = lessonTime.get(itemId);
                String lessonlocation = lessonLocation.get(itemId);
                String lessoncoments = lessonComments.get(itemId);


                // creates new intent and sends over the client information when item is clicked
                Intent i = new Intent(ListLessons.this, LessonInfo.class);
                i.putExtra("thelessonname", lessonname);
                i.putExtra("thelessondate", lessondate);
                i.putExtra("thelessontime", lessontime);
                i.putExtra("thelessonlocation", lessonlocation);
                i.putExtra("thelessoncomments", lessoncoments);


                startActivity(i);

            }
        });

        // old sqlite
//        final ListView listView = (ListView) findViewById(R.id.listView_lessons); // listView from the list_lessons.xml
//        try {
//            db.open();
//            Cursor result = db.getAllLessons();
//            LessonCursorAdapter cursorAdapter = new LessonCursorAdapter(ListLessons.this, result);
//            listView.setAdapter(cursorAdapter);
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
//                try {
//                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
//                    String lessonname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
//                    String lessondate = myCursor.getString(2);
//                    String lessontime = myCursor.getString(3);
//                    String lessonlocation = myCursor.getString(4);
//                    String lessoncomments = myCursor.getString(5);
//
//                    Intent i = new Intent(ListLessons.this, LessonInfo.class);
//
//                    i.putExtra("thelessonname", lessonname);
//                    i.putExtra("thelessondate", lessondate);
//                    i.putExtra("thelessontime", lessontime);
//                    i.putExtra("thelessonlocation", lessonlocation);
//                    i.putExtra("thelessoncomments", lessoncomments);
//
//                    startActivity(i);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

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
                        String lessonname = object.optString("lesson_name").toString();
                        String lessondate = object.optString("lesson_date").toString();
                        String lessontime = object.optString("lesson_time").toString();
                        String lessonlocation = object.optString("lesson_location").toString();
                        String lessoncomments = object.optString("lesson_comments").toString();


                        // adds to the array list
                        lessonName.add(lessonname);
                        lessonDate.add(lessondate);
                        lessonTime.add(lessontime);
                        lessonLocation.add(lessonlocation);
                        lessonComments .add(lessoncomments);

                        list.add(new Lesson(g, lessonname, lessondate, lessontime, lessonlocation, lessoncomments ));
                        adapter.notifyDataSetChanged();
                        g++;


                        System.out.println("##### ListLesson.java");
                        System.out.println("##### lessonname: " + lessonname);
                        System.out.println("##### lessondate: " + lessondate);
                        System.out.println("##### lessontime: " + lessontime);
                        System.out.println("##### lessonlocation: " + lessonlocation);
                        System.out.println("##### lessoncomments: " + lessoncomments);


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


    public void newLesson(View view)
    {
        try {
            Intent client_name_intent = new Intent(this, InsertLesson.class);
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














    // old sqlite way
//    public void listLessonName(View view) {
//        try {
//            Intent lesson_name_intent = new Intent(this, InsertLesson.class);
//            startActivity(lesson_name_intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void goBackScreen(View view) {
//        try {
//            Intent lastScreen = new Intent(this, AdminActivity.class);
//            startActivity(lastScreen);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

}

