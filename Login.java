package com.example.kieranfitzsimons.groupproject;

/**
 * Created by kieranfitzsimons on 11/04/2017.
 */


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

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button bLogin;
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink;
    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        session = new Session(this);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        //register onClickListeners for the login button and Register TextView
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        //Use session with loggedin method to send user straight to main activity if the
        //user had previously logged in and closed the app, though we would have to create a logoff button
        //and method or the user would never log off. The benefit of this means the user only ever has to log in once.
        if(session.loggedin()){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    /**
     * onClick method using switch statement for either login button or register TextView
     * @param v - view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                login();//call login method below
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(Login.this, Register.class));//intent to start Register class activity
                finish();
                break;
            default:
        }
    }

    /**
     *login method takes the username and password strings from the EditText objects and checks if they exist on the database.
     * If they do sends an intent to start the MainActivity class
     */
    private void login(){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (db.getUser(username,password)){//calls method from DBHelper class

            //Have not allowed this to be set because also would have to create logoff button and method
           // session.setLoggedin(true);

            startActivity(new Intent(Login.this, MainActivity.class));
            finish();

        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong Username and Password",Toast.LENGTH_SHORT).show();
        }
    }
}
