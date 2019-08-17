package com.cm0573.w16041611.k5;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private TextToSpeech TTS;
    private SpeechRecognizer mySpeechRecognizer;
    DatabaseReference databaseReference;
    ListView listView;
    ProgressBar pr;
    private ProgressDialog progressDialog;

    List<data>restaurantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listview);

        pr = findViewById(R.id.progressBar2);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("restaurant");
        restaurantList = new ArrayList<>();

        FloatingActionButton fab1 = findViewById(R.id.fab1);
        View b1 = findViewById(R.id.fab1);
        if(getIntent().getExtras().getString("Guest").equals("Yes")){
            b1.setVisibility(View.GONE);
        }
        else{
            initializeTextToSpeech();
            initializeSpeechRecognizer();
            fab1.setOnClickListener(v -> {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(i);
            });
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        View b = findViewById(R.id.fab);
        if(getIntent().getExtras().getString("Guest").equals("Yes")){
            b.setVisibility(View.GONE);
        }
        else{
            fab.setOnClickListener(view -> {
                Intent i = new Intent(MainActivity.this, Chatbot.class);
                startActivity(i);
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading!");
        progressDialog.show();
        restaurantList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    data restaurant = restaurantSnapshot.getValue(data.class);
                    restaurantList.add(restaurant);
                    listView.setOnItemClickListener((parent , view, position, id) -> {
                        if(isServicesOK()) {
                            try{
                                Bundle b = getIntent().getExtras();
                                if(b.getString("Guest").equals("Yes")){
                                    Toast.makeText(MainActivity.this, "Please login to take this action", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    data data1 = (data) parent.getAdapter().getItem(position);
                                    Intent i = new Intent(MainActivity.this, restaurant_detail.class);
                                    i.putExtra("Image", data1.getImage());
                                    i.putExtra("Name", data1.getName1());
                                    i.putExtra("Number", data1.getNumber());
                                    i.putExtra("Address", data1.getAddress());
                                    i.putExtra("Email", b.getString("Email"));
                                    startActivity(i);
                                }
                            }
                            catch(Exception e){
                            Toast.makeText(MainActivity.this, "Restaurant unavailable! Please try again!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                RestaurantInfoAdapter restaurantInfoAdapter = new RestaurantInfoAdapter(MainActivity.this, restaurantList);
                listView.setAdapter(restaurantInfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Load data failed!", Toast.LENGTH_LONG).show();
            }
        });

    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occurred");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(MainActivity.this, "Cant show the map", Toast.LENGTH_SHORT).show();
        }
        return false;

    }
    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> r = results.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION);
                    process(r.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void process(String command) {
        command = command.toLowerCase();

        if(command.indexOf("1") != -1) {
                listView.performItemClick(listView, 0,listView.getItemIdAtPosition(0));
        }
        if(command.indexOf("2") != -1) {
            listView.performItemClick(listView, 1,listView.getItemIdAtPosition(1));

        }
        if(command.indexOf("3") != -1) {
            listView.performItemClick(listView, 2,listView.getItemIdAtPosition(2));
        }
        if(command.indexOf("4") != -1) {
            listView.performItemClick(listView, 3,listView.getItemIdAtPosition(3));
        }
        if(command.indexOf("5") != -1) {
            listView.performItemClick(listView, 4,listView.getItemIdAtPosition(4));
        }
        if(command.indexOf("6") != -1) {
            listView.performItemClick(listView, 5,listView.getItemIdAtPosition(5));
        }
        if(command.indexOf("7") != -1) {
            listView.performItemClick(listView, 6,listView.getItemIdAtPosition(5));
        }
    }

    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this, "No TSS", Toast.LENGTH_LONG).show();
                    finish();
                } else{
                    TTS.setLanguage(Locale.US);
                    if(getIntent().getExtras().getString("Guest").equals("Yes")){
                        speak("Please login to system to use all features");
                    }
                    else {
                        speak("Hello! What option you want to choose?");
                    }
                }
            }


        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >=21){
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(getIntent().getExtras().getString("Guest").equals("Yes")){
                Toast.makeText(MainActivity.this, "Please login to take this action", Toast.LENGTH_LONG).show();
            }
            else{
                Intent i = new Intent(MainActivity.this, history_booking.class);
                i.putExtra("Email",getIntent().getExtras().getString("Email"));
                startActivity(i);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if(getIntent().getExtras().getString("Guest").equals("Yes")){
                Toast.makeText(MainActivity.this, "Please login to take this action", Toast.LENGTH_LONG).show();
            }
            else{
                Intent i = new Intent(MainActivity.this, Chatbot.class);
                startActivity(i);
            }

        } else if (id == R.id.nav_gallery) {
            if(getIntent().getExtras().getString("Guest").equals("Yes")){
                Toast.makeText(MainActivity.this, "Please login to take this action", Toast.LENGTH_LONG).show();
            }
            else{
                Intent i = new Intent(MainActivity.this, history_booking.class);
                i.putExtra("Email",getIntent().getExtras().getString("Email"));
                startActivity(i);
            }
        }  else if (id == R.id.nav_send) {

                Intent i = new Intent(MainActivity.this, Home_page.class);
                finish();
                startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    protected void onPause(){
//        super.onPause();
//        TTS.shutdown();
//    }
}
