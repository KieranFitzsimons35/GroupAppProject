package com.example.kieranfitzsimons.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kieranfitzsimons on 11/04/2017.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button bRegister;
    private EditText etName, etLastname, etEmail, etUsername, etPassword;
    private TextView tvLogin;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
        etName = (EditText) findViewById(R.id.etName);
        etLastname = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        //register onClickListeners for the login TextView and Register Button
        bRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    /**
     * onClick method using switch statement for either Register Button or the Login TextView
     * @param v
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bRegister:
                register();//calls the register method
                break;

            case R.id.tvLogin:
                startActivity(new Intent(Register.this, Login.class));//starts the activity for the Login class
                finish();//ends the current activity
                break;
            default:
        }
    }

    /**
     * Method that that takes the EditText objects and stores their value as strings.
     * Then checks if the USERNAME and PASSWORD fields have been filled in.
     * If they have then adds all stored strings to the database table
     */
    private void register(){

        String name = etName.getText().toString();
        String lastname = etLastname.getText().toString();
        String email = etEmail.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(username.isEmpty() && password.isEmpty()){//if these strings are empty
            displayToast("Username/Password Field Empty");//user sees Toast message to tell them fields are required
        }
        else {
            db.addUser(name, lastname, email, username,password);//call DBHelper class method and pass strings as parmeters
            displayToast("User Registered");//confirm user details have been added to database table with Toast message


        }
    }

    /**
     * Method to quickly add Toast message to different areas of the code.
     * Cuts down on typing full syntax for Toast message every time.
     * @param message - the string entered
     */
    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
