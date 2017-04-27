package com.example.kieranfitzsimons.groupproject;




import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kieranfitzsimons on 11/04/2017.
 * This method makes use of Shared preferences to create a record of using the app.
 * If we know this then we can use booleans to know whether the user had already logged in or not.
 * If they have logged in then we no longer need to go to the register or login pages.
 * This has not been fully implemented in this app due to time constraints as we would alos have to create
 * a method for the user to log out so we could demosntrate the registration and login pages fully to the
 * assessors.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myfirstapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean loggedin){//this method has not been called in this iteration of the app
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}
