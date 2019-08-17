package com.cm0573.w16041611.k5;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

public class history_booking extends AppCompatActivity {
    ListView listView;
    List<history> historyList;
    DatabaseReference databaseReference;
    private static final String TAG = "history_booking";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking);
        listView = findViewById(R.id.history);
        historyList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("booking");
    }
    @Override
    protected void onStart() {
        super.onStart();
        historyList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot historySnapshot : dataSnapshot.getChildren()) {
                    history booking = historySnapshot.getValue(history.class);
                    historyList.add(booking);
                    listView.setOnItemClickListener((parent , view, position, id) -> {
                        if(isServicesOK()) {
                            history data1 = (history) parent.getAdapter().getItem(position);
                            Intent i = new Intent(history_booking.this, booking_detail.class);
                            i.putExtra("Image", data1.getImage());
                            i.putExtra("Name", data1.getRestaurant());
                            i.putExtra("Time", data1.getTime());
                            i.putExtra("Date", data1.getDate());
                            i.putExtra("Address", data1.getAddress());
                            startActivity(i);
                        }
                    });
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(history_booking.this, historyList);
                listView.setAdapter(historyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(history_booking.this, "Load data failed!", Toast.LENGTH_LONG).show();
            }
        });

    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(history_booking.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occurred");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(history_booking.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(history_booking.this, "Cant show the map", Toast.LENGTH_SHORT).show();
        }
        return false;

    }
}
