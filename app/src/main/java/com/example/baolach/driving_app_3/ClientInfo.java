package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
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

    Point p; // for popup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_client_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // bundle captures the parameters from the intent sent from listClients
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
            clientid = bundle.getString("id");
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

        btnDelete = (Button) findViewById(R.id.delete_client_btn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this client?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                new Thread(new Runnable() {

                    public void run() {
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

                    String sql = "DELETE from getdata_getclient where log_no= '" + clientlogno + "';";
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


                } catch (Exception e) {
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
            }
        });


        phoneTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneTextView.getText().toString()));
                    startActivity(callIntent);

                } catch (ActivityNotFoundException activityException) {
                    Log.e("", "Call failed", activityException);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }
        });

        addressTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String addr = addressTextView.getText().toString();

                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(addr));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                } catch (Exception e) {
                    Log.e("Loading map", "map failed", e);
                }

            }
        });

        ///////////////////////

//        commentsTextView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (p != null) {
//                    showPopup(ClientInfo.this, p);
//                }
//
//            }
//        });





    } // end onCreate


//    // Get the x and y position after the button is draw on screen
//// (It's important to note that we can't get the position in the onCreate(),
//// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
//    public void onWindowFocusChanged(boolean hasFocus) {
//
//        int[] location = new int[2];
//
//        // Get the x, y location and store it in the location[] array
//        // location[0] = x, location[1] = y.
//
//        //Initialize the Point with x, and y positions
//        p = new Point();
//        p.x = location[0];
//        p.y = location[1];
//    }
//
//    // The method that displays the popup.
//    private void showPopup(final Activity context, Point p) {
//        int popupWidth = 800;
//        int popupHeight = 300;
//
//        // Inflate the popup_layout.xml
//        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
//        LayoutInflater layoutInflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
//
//
//
//
//        // Creating the PopupWindow
//        final PopupWindow popup = new PopupWindow(context);
//        popup.setContentView(layout);
//        popup.setWidth(popupWidth);
//        popup.setHeight(popupHeight);
//        popup.setFocusable(true);
//
//        TextView pcommentsTextView = null; // = (TextView) findViewById(R.id.textView10);
//        ((TextView)pcommentsTextView.findViewById(R.id.textView10)).setText("test");
//        //pcommentsTextView.showAtLocation();
////        pcommentsTextView.setText(clientcomments);
//
//        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
//        int OFFSET_X = 5;
//        int OFFSET_Y = 50;
//
//        // Clear the default translucent background
//        popup.setBackgroundDrawable(new BitmapDrawable());
//
//        // Displaying the popup at the specified location, + offsets.
//        popup.showAtLocation(layout, Gravity.BOTTOM, p.x + OFFSET_X, p.y + OFFSET_Y);
//
//        // Getting a reference to Close button, and close the popup when clicked.
//        Button close = (Button) layout.findViewById(R.id.close);
//        close.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//    } // end show popUp











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