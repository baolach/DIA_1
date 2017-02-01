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
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "My_Database";
    static final String CLIENT_TABLE = "Client";
    static final String LESSON_TABLE = "Lessons";

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

    // Lesson variables
    static final String KEY_TASK_LESSON_NAME = "Lesson_Name";
    static final String KEY_TASK_LESSON_DATE = "Date";
    static final String KEY_TASK_LESSON_TIME = "Time";
    static final String KEY_TASK_LESSON_LOCATION = "Location";
    static final String KEY_TASK_LESSON_COMMENTS = "Comments";

    // Database variables
    final Context context;
    MyDatabaseHelper DBHelper;
    SQLiteDatabase db;

    private static final String CREATE_CLIENT_TABLE = "create table Client (_id integer primary key autoincrement, " +
            "Client_Name  TEXT, " +
            "Phone  INTEGER, " +
            "Address  TEXT," +
            "Log_No    TEXT," +
            "Driver_No   TEXT," +
            "Dob  DATE," + // maybe change to DATE
            "No_Of_Lessons  INTEGER," +
            "Comments TEXT);";

    private static final String CREATE_LESSONS_TABLE = "create table Lessons(_id integer primary key autoincrement, " +
            "Lesson_Name  TEXT, " +
            "Date  DATE, " +
            "Time  TEXT," +
            "Location    TEXT," +
            "Comments   TEXT);";

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

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LESSON_TABLE);

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
                             String driver_no, String dob, String no_of_lessons, String comments) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_TASK_CLIENT_NAME, name);
        initialValues.put(KEY_TASK_PHONE, phone);
        initialValues.put(KEY_TASK_ADDRESS, address);
        initialValues.put(KEY_TASK_LOG_NO, log_no);
        initialValues.put(KEY_TASK_DRIVER_NO, driver_no);
        initialValues.put(KEY_TASK_DOB, dob);
        initialValues.put(KEY_TASK_NO_OF_LESSONS, no_of_lessons);
        initialValues.put(KEY_TASK_COMMENTS, comments);

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