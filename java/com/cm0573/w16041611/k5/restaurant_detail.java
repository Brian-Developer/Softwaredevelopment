package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.Manifest;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class restaurant_detail extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(restaurant_detail.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(getIntent().getExtras().getString("Address"), 1);
        }
        catch(IOException e){
            Log.e(TAG, "geoLocate: IOException");
        }

        if(list.size() > 0){
            Address address1 = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + getIntent().getExtras().getString("Address").toString());
            LatLng lat = new LatLng(address1.getLatitude(), address1.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15f));
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(address1.getLatitude(), address1.getLongitude()))
                    .title(address1.getAddressLine(0));
            mMap.addMarker(options);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(restaurant_detail.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private TextToSpeech TTS;
    private SpeechRecognizer mySpeechRecognizer;
    private static final String TAG = "restaurant_detail";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        getLocationPermission();
        ImageView im = (ImageView)findViewById(R.id.imageView);
        TextView name = (TextView)findViewById(R.id.Name);
        TextView number = (TextView)findViewById(R.id.number);
        TextView address = (TextView)findViewById(R.id.address);
        Button book = (Button)findViewById(R.id.book);
        Bundle b = getIntent().getExtras();
        initializeTextToSpeech(b.getString("Name"));
        initializeSpeechRecognizer();

        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            mySpeechRecognizer.startListening(i);
        });
            Picasso.get().load(b.getString("Image")).into(im);
            name.setText(b.getString("Name"));
            number.setText(b.getString("Number"));
            address.setText(b.getString("Address"));

        book.setOnClickListener(v -> {
            Intent intent = new Intent(restaurant_detail.this, fill_up.class);
            intent.putExtra("Email", b.getString("Email"));
            intent.putExtra("Name", b.getString("Name"));
            intent.putExtra("Address", b.getString("Address"));
            intent.putExtra("Image", b.getString("Image"));
            startActivity(intent);
            finish();
        });
    }
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(restaurant_detail.this);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
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

        if(command.indexOf("ok") != -1) {
            Intent i = new Intent(restaurant_detail.this, voice_reservation.class);
            i.putExtra("Email", getIntent().getExtras().getString("Email"));
            i.putExtra("Name", getIntent().getExtras().getString("Name"));
            i.putExtra("Address", getIntent().getExtras().getString("Address"));
            i.putExtra("Image", getIntent().getExtras().getString("Image"));
            startActivity(i);
            finish();
        }
    }

    private void initializeTextToSpeech(String a) {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0){
                    Toast.makeText(restaurant_detail.this, "No TSS", Toast.LENGTH_LONG).show();
                    finish();
                } else{
                    TTS.setLanguage(Locale.US);
                    speak("You chose the" + a.toLowerCase() + "Please say ok to confirm");
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


}
