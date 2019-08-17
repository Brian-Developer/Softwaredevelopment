package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class voice_reservation extends AppCompatActivity {
    private TextToSpeech TTS;
    private SpeechRecognizer mySpeechRecognizer;
    private DatabaseReference databaseReference;
    private String n, t, d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_reservation);
        databaseReference = FirebaseDatabase.getInstance().getReference("booking");
        Button voice = (Button)findViewById(R.id.voice);


        voice.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            mySpeechRecognizer.startListening(i);
        });
        initializeTextToSpeech();
        initializeSpeechRecognizer();
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
        TextView number = (TextView)findViewById(R.id.number);
        TextView time = (TextView)findViewById(R.id.time);
        TextView date = (TextView)findViewById(R.id.date);
        String getNumber = number.getText().toString();
        String getTime = time.getText().toString();
        String getDate = date.getText().toString();
        Bundle b = getIntent().getExtras();
        if(command.indexOf("cancel") != -1) {
            finish();
        }
        else{
            if(getNumber.trim().isEmpty()){
                if(!command.matches("\\d+(?:\\.\\d+)?") || command.length() > 2){
                    number.setText("");
                    speak("Invalid number of guest. Please speak again!");
                }
                else {
                    number.setText("Number of guest: " + command);
                    speak("You booked " + command + "guests. Please tell me the time of reservation");
                    n = command;
                }
            }

            else if (getTime.isEmpty()){
                time.setText("Time: "+ command);
                speak("You booked at "+ command + ". Please tell me the date of reservation");
                t = command;
            }
            else if (getDate.isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");
                Date today = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(today);
                if(command.indexOf("tomorrow") != -1) {
                    cal.add(Calendar.DATE, 1);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next week") != -1){
                    cal.add(Calendar.DATE, 7);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("today") != -1){
                    cal.add(Calendar.DATE, 0);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next 2") != -1){
                    cal.add(Calendar.DATE, 2);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next 3") != -1){
                    cal.add(Calendar.DATE, 3);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next 4") != -1){
                    cal.add(Calendar.DATE, 4);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next 5") != -1){
                    cal.add(Calendar.DATE, 5);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else if(command.indexOf("next 6") != -1){
                    cal.add(Calendar.DATE, 6);
                    String tomorrow = (String)(simpleDateFormat.format(cal.getTime()));
                    date.setText("Date: " + tomorrow);
                    d = tomorrow;
                    speak("You booked at " + tomorrow + ". Please say ok to confirm");
                }
                else{
                    speak("You only able to book within 7 days. Please say again!");
                }
            }
            else if (command.indexOf("ok") != -1) {

                String id = databaseReference.push().getKey();
                booking db = new booking(id, b.getString("Email"),b.getString("Name"),t, d,"",n,"",b.getString("Address"),b.getString("Image") );
                databaseReference.child(id).setValue(db);
                Intent i = new Intent(voice_reservation.this, confirm.class);
                i.putExtra("Email", b.getString("Email"));
                startActivity(i);
                finish();
            }
        }


    }

    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0){
                    Toast.makeText(voice_reservation.this, "No TSS", Toast.LENGTH_LONG).show();
                    finish();
                } else{
                    TTS.setLanguage(Locale.US);
                    speak("You selected option one, How many guests will come?");
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
