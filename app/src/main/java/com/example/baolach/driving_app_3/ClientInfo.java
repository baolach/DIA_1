package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClientInfo extends Activity {

    //    DBManager db = new DBManager(this);
//    String clientsName;
//    static ArrayList<String> clientsId = new ArrayList<String>();
//    //ListView listView;
//    ArrayList<Client> list;
//    ClientInfoAdapter infoAdapter = null;


    String clientname, clientphone, clientaddress, clientlogno, clientdriverno, clientdob, clientnooflessons, clientbalancedue, clientcomments;

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

        }

        // apply to textViews
        TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        TextView phoneTextView = (TextView) findViewById(R.id.thelessondate);
        TextView addressTextView = (TextView) findViewById(R.id.thelessontime);
        TextView lognoTextView = (TextView) findViewById(R.id.thelessonlocation);
        TextView drivernoTextView = (TextView) findViewById(R.id.thelessoncomments);
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

        nameTextView.setSelected(true);
        phoneTextView.setSelected(true);
        addressTextView.setSelected(true);
        lognoTextView.setSelected(true);
        drivernoTextView.setSelected(true);
        nooflessonsTextView.setSelected(true);
        balanceTextView.setSelected(true);
        commentsTextView.setSelected(true);

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

}
