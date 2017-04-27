package com.example.kieranfitzsimons.groupproject;

/**
 * Created by kieranfitzsimons on 16/03/2017.
 */


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

    public class DisplayMessageActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_message);

            Button moreinfo = (Button) findViewById(R.id.button2);
            Button hearthealth = (Button) findViewById(R.id.button3);
            Button movemore = (Button) findViewById(R.id.button4);

            moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nhs.uk/Livewell/fitness/Pages/sitting-and-sedentary-behaviour-are-bad-for-your-health.aspx"));
                    startActivity(intent);
                }
            });

            hearthealth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.heartfoundation.org.au/active-living/sit-less"));
                    startActivity(intent);
                }
            });

            movemore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.movemoresitless.org.au/sedentary-dangers/"));
                    startActivity(intent);
                }
            });

        }
    }


