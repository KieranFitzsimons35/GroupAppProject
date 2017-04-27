package com.example.kieranfitzsimons.groupproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kieranfitzsimons on 11/04/2017.
 */

class DBHelper extends SQLiteOpenHelper{
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DB_NAME = "myfirstapp.db";
    public static final int DB_VERSION = 1;

    //Database table
    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    //Database creation SQL statement
    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_LASTNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT);";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);//executes the database creation SQL statement
    }

    /**
     * Method is called during an upgrade of the database
     * e.g. if you increase the database version
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + USER_TABLE);//dropt table if it exists
        onCreate(db);// call the onCreate method above to create another table
    }

    /**
     * Method that takes care of the SQL insert statements to add values to the database table
     * @param name
     * @param lastname
     * @param email
     * @param username
     * @param password
     */
    public void addUser(String name, String lastname, String email, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();//

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LASTNAME, lastname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long id = db.insert(USER_TABLE, null, values);//inserts values into the table
        db.close();

        Log.d(TAG, "User Added" + id);
    }

    /**
     * Method that returns a boolean true or false if the username and password strings it receives
     * as parameters match those values already in the database table
     * @param username
     * @param password
     * @return true or false
     */
    public boolean getUser(String username, String password){
        //uses SQL select statement to find username and password in database table
        String selectQuery = "select * from " + USER_TABLE + " where " +
                COLUMN_USERNAME + " = " + "'"+username+"'" + " and " + COLUMN_PASSWORD + " = " + "'"+password+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//uses Cursor object to return result
        cursor.moveToFirst();
        if (cursor.getCount() > 0){//if cursor finds a result it is greater than 0
            return true;
        }
        cursor.close();//close resources
        db.close();//close resources

        return false;
    }


}
