package com.example.kieranfitzsimons.groupproject;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.kieranfitzsimons.groupproject.R.id.chrono;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    boolean sittingFlat=false;

    Button  moreInfo, mapButton;
    Switch pauseTimer;
    ImageView  standingMan;
    long startTime;
    long countUp;
    String asText;
    TextView standingMessage;

    private SensorManager sensorManager = null;

    Uri alarmTone;
    Ringtone ringtoneAlarm;
    Vibrator v;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set ringtone alarm to be played when sat down for certain amount of time
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);


        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        // Output yes if can vibrate, no otherwise
        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
        } else {
            Log.v("Can Vibrate", "NO");
        }

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        //create stopwatch
        final Chronometer stopWatch = (Chronometer) findViewById(chrono);


        // initialize image views
        standingMan = (ImageView) findViewById(R.id.manStandingImage);

        //initialize TextViews
        standingMessage = (TextView) findViewById(R.id.standingUpMessage);



        //initialize buttons
        moreInfo = (Button) findViewById(R.id.moreInfo);
        mapButton = (Button) findViewById(R.id.map_button);

        pauseTimer = (Switch) findViewById(R.id.switchPauseTimer);

        //more info listener
        moreInfo.setOnClickListener(new handleButton());

        //map button listener
        mapButton.setOnClickListener(new handleOtherButton());




        //on click listener for the switch for pausing or resuming the sitting timer
        pauseTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //the toggle is enabled
                    onStop();//stops accelerometer from tracking changes in movement

                    Toast.makeText(MainActivity.this, "You have stopped the sensors and paused the timer",Toast.LENGTH_SHORT).show();

                    stopWatch.stop();//stops the timer , but does not reset- if you press restart button the timer has been
                    //counting the sitting time in the background anyway.
                } else {
                    // The toggle is disabled
                    Toast.makeText(MainActivity.this, "You have restarted the sensors",Toast.LENGTH_SHORT).show();

                    onResume();//restarts the accelerometer to track the movement of the phone from flat to vertical

                    Toast.makeText(MainActivity.this, "If you wish to reset the timer to zero again please stand up or move the phone to vertical",Toast.LENGTH_LONG).show();
                   stopWatch.start();

                }
            }
        });


    }
    //the method for starting the new Display Message Activity activity on the button click
   private class handleButton implements View.OnClickListener {
        public void onClick(View v)
        {
            Intent intent = new Intent (MainActivity.this, DisplayMessageActivity.class);
            startActivity(intent);

        }
    }
    //the method for starting the new Map activity on the button click
    private class handleOtherButton implements View.OnClickListener {
        public void onClick(View v)
        {
            Intent intent = new Intent (MainActivity.this, GoogleMaps.class);
            startActivity(intent);

        }
    }




    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


                //CODE BELOW FOR FLAT OR NOT FUNCTIONALITY
                // get the x y z values as a float array
                float[] accelVals = new float[3];
                //assign the values by cloning them
                accelVals = sensorEvent.values.clone();
                //normalize the values with the equations- casted to float rather than double
                float norm_of_accelVals = (float) Math.sqrt(accelVals[0] * accelVals[0] + accelVals[1] * accelVals[1] + accelVals[2] * accelVals[2]);

                //normalize the accelerometer value
                accelVals[0] = accelVals[0] / norm_of_accelVals; //x plane
                accelVals[1] = accelVals[1] / norm_of_accelVals; // y plane
                accelVals[2] = accelVals[2] / norm_of_accelVals;//surface of device (or z plane)

                //calculate the angle between the surface of the device (or z) and the xy plane
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(accelVals[2])));

                //create stopwatch
                final Chronometer stopWatch = (Chronometer) findViewById(chrono);

                startTime = SystemClock.elapsedRealtime();

                stopWatch.setFormat(" Time Spent Sitting  - %s"); // set the format for a chronometer

                stopWatch.setVisibility(View.INVISIBLE);//DEFAULT IS NOT TO SHOW IT UNTIL SITTING!

                stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
                    @Override
                    public void onChronometerTick(Chronometer arg0) {

                        countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;

                        asText = (countUp / 60) + ":" + (countUp % 60);

                      //  All you have to do is divide it.

                       //         duration_in_milliseconds / 1000 = Seconds

                       //           duration_in_milliseconds / 60000 = Minutes

                          //      (duration_in_milliseconds / 60000) / 60 = Hours


                    }
                });




                // Start without a delay
                // Vibrate for 100 milliseconds
                // Sleep for 1000 milliseconds
                long[] pattern = {0, 100, 1000};


                // the angle of the device is less than 25 degrees or greater than 155 when sitting flat
                if ((inclination < 25 || inclination > 155)) {

                    sittingFlat=true;


                }else{
                    sittingFlat=false;

                    //if it is playing when standing up then turn it off
                    if(ringtoneAlarm.isPlaying()){

                         ringtoneAlarm.stop();

                        Log.d("Ringtone playing?","stopped");
                    }


                }


                if(sittingFlat) {

                    // initialize image views
                    standingMan = (ImageView) findViewById(R.id.manStandingImage);

                    //change standing image in image view to sitting image
                    standingMan.setImageResource(R.drawable.sitting);

                    //show timer
                    stopWatch.setVisibility(View.VISIBLE);

                    //turn the standing upmessage to invisible now
                    standingMessage.setVisibility(View.INVISIBLE);



                    //start the stopwatch when phone is sitting flat i.e. sitting down
                    stopWatch.start();



                    if ((countUp/60)==1 && (countUp%60==0)) {//if you have sat down for 1 minute

                        //phone is flat- you must be sitting down!!
                        Toast.makeText(this, "You are sitting down too much get up!!!", Toast.LENGTH_SHORT).show();

                        // The '0' here means to repeat indefinitely
                        // '0' is actually the index at which the pattern keeps repeating from (the start)
                        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
                        v.vibrate(pattern, 0);

                        //set ringtone alarm to play
                        ringtoneAlarm.play();
                        Log.d("Ringtone playing?","Playing");


                    } else if ((countUp/60)==2 && (countUp%60==0)) {//if you have sat down for
                        //phone is flat- you must be sitting down!!
                        Toast.makeText(this, "Please get up!!!", Toast.LENGTH_SHORT).show();


                        // The '0' here means to repeat indefinitely
                        // '0' is actually the index at which the pattern keeps repeating from (the start)
                        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
                        v.vibrate(pattern, 0);

                    } else if ((countUp/60)==3 && (countUp%60==0)) {
                        //phone is flat- you must be sitting down!!
                       Toast.makeText(this, "You're slowly killing yourself!!!", Toast.LENGTH_SHORT).show();

                        // The '0' here means to repeat indefinitely
                        // '0' is actually the index at which the pattern keeps repeating from (the start)
                        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
                       v.vibrate(pattern, 0);


                    }else if((countUp/60)==45&&(countUp%60==0)){

                        //another alert -noise, vibration or Toast message here

                    }
                }else{
                    
                    //if standing set the imageview back to the original
                    standingMan.setImageResource(R.drawable.man_standing_up);
                    standingMessage.setVisibility(View.VISIBLE);


                    //stop the stopwatch when phone is vertical i.e. standing up
                    stopWatch.stop();

                    //reset stopwatch to zero - start again from 0 when sat down.
                    stopWatch.setBase(startTime);


                    v.cancel();



                }


            }



        }
    }




    // I've chosen not to implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }



    @Override
    protected void onResume() {
        super.onResume();

        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);




    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unregister the listener
        sensorManager.unregisterListener(this);

        ringtoneAlarm.stop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ringtoneAlarm.stop();
    }
}
