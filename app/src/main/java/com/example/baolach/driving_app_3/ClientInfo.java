package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ClientInfo extends Activity {



    private Button btnDelete;
    String clientname, clientphone, clientaddress, clientlogno, clientdriverno, clientdob, clientnooflessons, clientbalancedue, clientcomments, clientid; // ocming from the intent from listClients

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_client_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters form the intent
        if (bundle != null) {
            clientname = bundle.getString("theclientname");
            clientphone = bundle.getString("theclientphone");
            clientaddress = bundle.getString("theclientaddress");
            clientlogno = bundle.getString("thelognumber");
            clientdriverno = bundle.getString("thedriverno");
            clientdob = bundle.getString("thedob");
            clientnooflessons = bundle.getString("thenumberoflessons");
            clientbalancedue = bundle.getString("thebalance");
            clientcomments = bundle.getString("theclientscomments");
            clientid= bundle.getString("id");



        }

        // apply to textViews
        TextView nameTextView = (TextView) findViewById(R.id.theclientname);
        final TextView phoneTextView = (TextView) findViewById(R.id.theclientphone);
        final TextView addressTextView = (TextView) findViewById(R.id.theclientaddress);
        TextView lognoTextView = (TextView) findViewById(R.id.thelognumber);
        TextView drivernoTextView = (TextView) findViewById(R.id.thedrivernumber);
        TextView dobTextView = (TextView) findViewById(R.id.thedob);
        TextView nooflessonsTextView = (TextView) findViewById(R.id.thenumberoflessons);
        TextView balanceTextView = (TextView) findViewById(R.id.thebalance);
        TextView commentsTextView = (TextView) findViewById(R.id.theclientscomments);


        nameTextView.setText(clientname);
        phoneTextView.setText(clientphone);
        addressTextView.setText(clientaddress);
        lognoTextView.setText(clientlogno);
        drivernoTextView.setText(clientdriverno);
        dobTextView.setText(clientdob);
        nooflessonsTextView.setText(clientnooflessons);
        balanceTextView.setText(clientbalancedue);
        commentsTextView.setText(clientcomments);

        // needed for scrollbar on name
        nameTextView.setMovementMethod(new ScrollingMovementMethod());
        commentsTextView.setMovementMethod(new ScrollingMovementMethod());

        // needed for marquee scroll
        addressTextView.setSelected(true);

        btnDelete  = (Button) findViewById(R.id.delete_client_btn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this client?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                new Thread(new Runnable(){

                    public void run()
                    {
                        delete();
                    }

                }).start();

                finish();
            }

            protected void delete() {

                Statement deletedb = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    conn.setAutoCommit(false);
                    System.out.println("Opened database successfully");

                    deletedb = conn.createStatement();
                    System.out.println("About to delete: " + clientlogno);

                    String sql = "DELETE from getdata_getclient where log_no= '" + clientlogno + "';" ;
                    //Toast.makeText(getBaseContext(), "Client deleted from database! ", Toast.LENGTH_LONG).show();

                    deletedb.executeUpdate(sql);
                    conn.commit();


                    // once inserted into database all the edittexts resert to ""
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Client deleted from database! ", Toast.LENGTH_LONG).show();
                            Intent maps = new Intent(ClientInfo.this, ListClients.class); // lists all lessoninfo
                            startActivity(maps);

                        }
                    });

                    deletedb.close(); // close connection must be done
                    conn.close();


                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("Delete successful");




            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                builder.show();
            }});


        phoneTextView.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              //System.out.println("Hello");
              try {
                  Intent callIntent = new Intent(Intent.ACTION_DIAL);
                  callIntent.setData(Uri.parse("tel:" + phoneTextView.getText().toString()));
                  startActivity(callIntent);
              } catch (ActivityNotFoundException activityException) {
                  Log.e("Calling a Phone Number", "Call failed", activityException);
              } catch(SecurityException e){
                  e.printStackTrace();
              }

        }});

///////////////////////////////////////////////////////////////////////////////////// sending the address to google maps
        addressTextView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    String addr = addressTextView.getText().toString();
//                    String webURL = "";
//                    webURL += website;
//                    Log.d("location", webURL);
                    Uri uri = Uri.parse(addr);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(mapIntent);




//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW);
//                    mapIntent.setData(Uri.parse("daddr:" + addressTextView.getText().toString())); // destination address
//                    startActivity(mapIntent);

                } catch (Exception e) {
                    Log.e("Loading map", "map failed", e);
                }

            }});


/////////////////////////////////////////////////////////////////////////////////////
    } // end onCreate



    // when clicked brings you to the insertLesson page
    // passes the inputs in an intent and fills the editTexts with those values
    // then performs the insert again with those same params
    public void update(View view)
    {
        try{
            // creates new intent and sends over the client information when update is clicked
            // this adds these into the editTexts and then you can resave the client
            Intent i = new Intent(ClientInfo.this, UpdateClient.class);
            i.putExtra("theclientname", clientname);
            i.putExtra("theclientphone", clientphone);
            i.putExtra("theclientaddress", clientaddress);
            i.putExtra("thelognumber", clientlogno);
            i.putExtra("thedriverno", clientdriverno);
            i.putExtra("thedob", clientdob);
            i.putExtra("thenumberoflessons", clientnooflessons);
            i.putExtra("thebalance", clientbalancedue);
            i.putExtra("theclientscomments", clientcomments);
            i.putExtra("id", clientid);


//            System.out.println("lognumber is: " +clientlogno );
//            System.out.println("driver number is: " +clientdriverno );
//


            startActivity(i);
            finish();


        } catch (Exception e) {
            e.printStackTrace();
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

    public void home(View view)
    {
        try {
            Intent home_intent = new Intent(this, MainActivity.class);
            startActivity(home_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientLessons(View view) {
        try {
            Intent i = new Intent(this, ClientLessons.class);
            // pass the lesson name so we can do a select all with that name
            System.out.println(" clientInfo intent before name and id are sent to ClientLessons");
            System.out.println("clientname:" + clientname);
            System.out.println("client id:" + clientid);

            i.putExtra("theclientname", clientname);
            i.putExtra("id", clientid);

            startActivity(i);
            finish();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}

// in order to link the lesson and client tables, when you go to make a new lesson, you should pick the client
// from a dropdown list. this uses the same Id then