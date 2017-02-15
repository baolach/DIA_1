package com.example.baolach.driving_app_3;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

// This class displays the client names in a list to the user. You can press a button for insert, delete client also
public class ListClients extends Activity
{
    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_clients);

        final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml
        try {
            db.open();
            Cursor result = db.getAll();
            ClientCursorAdapter cursorAdapter = new ClientCursorAdapter(ListClients.this, result);
            listView.setAdapter(cursorAdapter);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                try {
                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
                    String theclientsname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
                    String theclientsphone = myCursor.getString(2);
                    String theclientsaddress = myCursor.getString(3);
                    String theclientslognumber = myCursor.getString(4);
                    String theclientsdrivernumber = myCursor.getString(5);
                    String theclientsdob = myCursor.getString(6);
                    String nooflessons = myCursor.getString(7);
                    String theclientscomments = myCursor.getString(8);

                    Intent i = new Intent(ListClients.this, ClientInfo.class);

                    i.putExtra("theclientsname", theclientsname);
                    i.putExtra("theclientsphone", theclientsphone);
                    i.putExtra("theclientsaddress", theclientsaddress);
                    i.putExtra("theclientslognumber", theclientslognumber);
                    i.putExtra("theclientsdrivernumber", theclientsdrivernumber);
                    i.putExtra("theclientsdob", theclientsdob);
                    i.putExtra("nooflessons", nooflessons);
                    i.putExtra("theclientscomments", theclientscomments);

                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    } // end onCreate

    public void listClientName(View view)
    {
        try {
            Intent client_name_intent = new Intent(this, InsertClient.class);
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


}

