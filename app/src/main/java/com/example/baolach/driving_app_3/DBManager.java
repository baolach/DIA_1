package com.example.baolach.driving_app_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;


/**
 */
public class DBManager {

    // final means the variable name can't be renamed
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "My_Database";
    static final String CLIENT_TABLE = "Client";
    static final String LESSON_TABLE = "Lessons";
    static final String FINANCES_TABLE = "Finances";


    // Client variables
    static final String KEY_ID = "_id"; // the log_no will deal with unique clients
    static final String KEY_TASK_CLIENT_NAME = "Client_Name";
    static final String KEY_TASK_PHONE = "Phone";
    static final String KEY_TASK_ADDRESS = "Address";
    static final String KEY_TASK_LOG_NO = "Log_No";
    static final String KEY_TASK_DRIVER_NO = "Driver_No";
    static final String KEY_TASK_DOB = "Dob";
    static final String KEY_TASK_NO_OF_LESSONS = "No_Of_Lessons";
    static final String KEY_TASK_COMMENTS = "Comments";
    static final String KEY_TASK_BALANCE = "Balance";


    // Lesson variables
    static final String KEY_TASK_LESSON_NAME = "Lesson_Name";
    static final String KEY_TASK_LESSON_DATE = "Date";
    static final String KEY_TASK_LESSON_TIME = "Time";
    static final String KEY_TASK_LESSON_LOCATION = "Location";
    static final String KEY_TASK_LESSON_COMMENTS = "Comments";

    // Finances variables
    // static final String KEY_TASK_CLIENT_NAME = "Client_name";
    //static final String KEY_TASK_DRIVER_NO = "Driver_No";
    //static final String KEY_TASK_NO_OF_LESSONS = "No_Of_Lessons";
    static final String KEY_TASK_LESSONS_TO_BE_PAID = "Lessons_to_be_paid";
    static final String KEY_TASK_PRICE_PER_LESSON = "Price_per_lesson";
    //static final String KEY_TASK_BALANCE_DUE = "Balance_due";


    // Database variables
    final Context context;
    MyDatabaseHelper DBHelper;
    SQLiteDatabase db;

    private static final String CREATE_CLIENT_TABLE = "create table Client (_id integer primary key autoincrement, " +
            "Client_Name  TEXT, " +
            "Phone  INTEGER, " +
            "Address  TEXT," +
            "Log_No    TEXT," +
            "No_Of_Lessons   TEXT," +
            "Dob  DATE," + // maybe change to DATE
            "No_Of_Lessons  INTEGER," +
            "Comments TEXT," +
            "Balance INTEGER);";

    private static final String CREATE_LESSONS_TABLE = "create table Lessons(_id integer primary key autoincrement, " +
            "Lesson_Name  TEXT, " +
            "Date  DATE, " +
            "Time  TEXT," +
            "Location    TEXT," +
            "Comments   TEXT);";
    ///////////////////
//
//    private static final String CREATE_FINANCES_TABLE = "create table Finances(_id integer primary key autoincrement, " +
//            "Client_name  TEXT, " + // FK of client table
//            "Driver_No  TEXT, " + // fk
//            "No_Of_Lessons  INTEGER, " + // fk
//            "Lessons_to_be_paid  INTEGER, " + // Lessons_to_be_paid * price_per_lessons
//            "Price_per_lesson  INTEGER, " +
//            "Balance_due  INTEGER)"; // in euros
    /*
    The locationName, for example may be “Difficult Hill-start, Greenhills road”.
    LocationType would be “hill-start” and the coordinates would be the latitude
    and longitude coordinates. There would be many different locations of type
    hill-start, reverse-around the corner, tight corners, roundabouts, three point
    turns etc.
    */

    // routes table holds spatial data of the different routes so the instructor can select
    // at the start of the lesson depending on difficulty
//    private static final String CREATE_ROUTES_TABLE = "create table Routes(_id integer primary key autoincrement, " +
//            "Route_name  DATE, " +
//            "Route_difficulty  TEXT)" +
//            "coordinates  TEXT)" +
//            "Polyline_or_something_I_dont_know  TEXT)";
//
//    private static final String CREATE_LOCATIONS_TABLE = "create table Locations(_id integer primary key autoincrement, " +
//            "Location_name  TEXT, " +
//            "Location_type  TEXT)" +
//            "Lat  FLOAT)" +
//            "Lon  FLOAT)" +
//            "Geo GEOGRAPHY)";



/////////////////////
    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new MyDatabaseHelper(context); //////////////////////////  ?
    }


    private static class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CLIENT_TABLE);
            db.execSQL(CREATE_LESSONS_TABLE);
            //db.execSQL(CREATE_FINANCES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LESSON_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + FINANCES_TABLE);


            onCreate(db);
        }

    }

    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase(); // gets info from datasbase and displays it
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertClient(String name, String phone, String address, String log_no,
                             String driver_no, String dob, String no_of_lessons, String comments, String balance) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_TASK_CLIENT_NAME, name);
        initialValues.put(KEY_TASK_PHONE, phone);
        initialValues.put(KEY_TASK_ADDRESS, address);
        initialValues.put(KEY_TASK_LOG_NO, log_no);
        initialValues.put(KEY_TASK_DRIVER_NO, driver_no);
        initialValues.put(KEY_TASK_DOB, dob);
        initialValues.put(KEY_TASK_NO_OF_LESSONS, no_of_lessons);
        initialValues.put(KEY_TASK_COMMENTS, comments);
        initialValues.put(KEY_TASK_BALANCE, balance);


        return db.insert(CLIENT_TABLE, null, initialValues);
    }


    public long insertLesson(String lessonName, String lessonDate, String lessonTime, String lessonLocation, String lessonComments) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_TASK_LESSON_NAME, lessonName);
        initialValues.put(KEY_TASK_LESSON_DATE, lessonDate);
        initialValues.put(KEY_TASK_LESSON_TIME, lessonTime);
        initialValues.put(KEY_TASK_LESSON_LOCATION, lessonLocation);
        initialValues.put(KEY_TASK_LESSON_COMMENTS, lessonComments);

        return db.insert(LESSON_TABLE, null, initialValues);
    }

//    public long insertPayment(String clientName, String driverNo, Integer noOfLessons, Integer lessonsToBePaid, Integer pricePerLesson, Integer balanceDue) {
//        ContentValues initialValues = new ContentValues();
//
//        initialValues.put(KEY_TASK_CLIENT_NAME, clientName);
//        initialValues.put(KEY_TASK_DRIVER_NO, driverNo);
//        initialValues.put(KEY_TASK_NO_OF_LESSONS, noOfLessons);
//        initialValues.put(KEY_TASK_LESSONS_TO_BE_PAID, lessonsToBePaid);
//        initialValues.put(KEY_TASK_PRICE_PER_LESSON, pricePerLesson);
//        initialValues.put(KEY_TASK_BALANCE_DUE, balanceDue);
//
//        return db.insert(FINANCES_TABLE, null, initialValues);
//    }



    // GETTING ALL THE CLIENT INFO SQL
    public Cursor getAll() {
        Cursor mCursor = db.rawQuery("SELECT DISTINCT * FROM Client;", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

    }

    public Cursor getAllLessons() {
        Cursor mCursor = db.rawQuery("SELECT DISTINCT * FROM Lessons;", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void deleteClient(String clientName) {
        Cursor deleteClient = db.rawQuery("DELETE FROM Client WHERE client_name ='"+ clientName + "'",null);

        if (deleteClient != null || deleteClient.getCount() > 0) {
            deleteClient.moveToFirst();
        }
    }

}