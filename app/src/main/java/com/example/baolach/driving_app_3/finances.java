package com.example.baolach.driving_app_3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_finances);

        final ListView listView = (ListView) findViewById(R.id.listView_clients); // in the list_clients xml
        try {
            db.open();
            Cursor result = db.getAll();
            FinancesCursorAdapter cursorAdapter = new FinancesCursorAdapter(Finances.this, result);
            listView.setAdapter(cursorAdapter);
            
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        // these below need to be added also

        // When a client is clicked it goes to the ClientInfo activity and displays all info on that client
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                try {
//                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
//                    String lessonname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
//                    String lessondate = myCursor.getString(2);
//                    String lessontime = myCursor.getString(3);
//                    String lessonlocation = myCursor.getString(4);
//                    String nooflessons = myCursor.getString(5);
//                    String lessoncomments = myCursor.getString(6);
//
//                    Intent i = new Intent(Finances.this, LessonInfo.class);
//
//                    i.putExtra("thelessonname", lessonname);
//                    i.putExtra("thelessondate", lessondate);
//                    i.putExtra("thelessontime", lessontime);
//                    i.putExtra("thelessonlocation", lessonlocation);
//                    i.putExtra("thelessonlocation", nooflessons);
//                    i.putExtra("thelessoncomments", lessoncomments);
                    Cursor myCursor = (Cursor) parent.getItemAtPosition(position); // where the info is stored on what you clicked
                    String theclientsname = myCursor.getString(1); // 4th position in the clients table (LOG NUMBER)
                    String theclientsphone = myCursor.getString(2);
                    String theclientsaddress = myCursor.getString(3);
                    String theclientslognumber = myCursor.getString(4);
                    String theclientsdrivernumber = myCursor.getString(5);
                    String theclientsdob = myCursor.getString(6);
                    String nooflessons = myCursor.getString(7);
                    String theclientscomments = myCursor.getString(8);

                    Intent i = new Intent(Finances.this, ClientInfo.class);

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

    }


//    public void listLessonName(View view) {
//        try {
//            Intent lesson_name_intent = new Intent(this, InsertLesson.class);
//            startActivity(lesson_name_intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void back_screen_btn(View view) {
        try {
            Intent adminActivity = new Intent(this, MainActivity.class);
            startActivity(adminActivity);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}
