package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class ClientInfo extends Activity {

    DBManager db = new DBManager(this);
    String clientsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);

        Intent clientData = getIntent();

        //put data from list clients activity into new string so you can delete it
        clientsName = clientData.getStringExtra("theclientsname");
        String TheClientsPhone= clientData.getStringExtra("theclientsphone");
        String TheClientsAddress = clientData.getStringExtra("theclientsaddress");
        String TheClientsLogNumber = clientData.getStringExtra("theclientslognumber");
        String TheClientDriverNumber = clientData.getStringExtra("theclientsdrivernumber");
        String TheClientsDob= clientData.getStringExtra("theclientsdob");
        String TheClientsComments = clientData.getStringExtra("theclientscomments");

        //create variable which references output field
        final TextView nameTextView = (TextView) findViewById(R.id.thelessonname);
        final TextView phoneTextView = (TextView) findViewById(R.id.thelessondate);
        final TextView addressTextView = (TextView) findViewById(R.id.thelessontime);
        final TextView lognoTextView = (TextView) findViewById(R.id.thelessonlocation);
        final TextView drivernoTextView = (TextView) findViewById(R.id.thelessoncomments);
        final TextView dobTextView = (TextView) findViewById(R.id.theclientsdob);
        final TextView commentsTextView = (TextView) findViewById(R.id.theclientscomments);

        Button deleteButton = (Button)findViewById(R.id.delete_client_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DBManager dbManager = new DBManager(ClientInfo.this);
                try {
                    dbManager.open();
                    dbManager.deleteClient(clientsName);
                } catch (SQLException e) {
                    System.out.println("Client could not be deleted");
                } finally {
                    dbManager.close();

                    Intent intent = new Intent(ClientInfo.this, ListClients.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears previous screens
                    startActivity(intent); // this loads new intent (ListClients

                }

            }
        });

        // setting the TextViews to display what the info the user entered
        nameTextView.setText(clientsName);
        phoneTextView.setText(TheClientsPhone);
        addressTextView.setText(TheClientsAddress);
        lognoTextView.setText(TheClientsLogNumber);
        drivernoTextView.setText(TheClientDriverNumber);
        dobTextView.setText(TheClientsDob);
        commentsTextView.setText(TheClientsComments);

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
