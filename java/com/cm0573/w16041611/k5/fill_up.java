package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.BitSet;

public class fill_up extends AppCompatActivity {
    private DatabaseReference databaseReference;
    EditText amount,time,date,number, note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("booking");
        amount = findViewById(R.id.amount);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        number = findViewById(R.id.phone);
        note = findViewById(R.id.date);
        Button confirm = (Button)findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTime = time.getText().toString();
                String getDate = date.getText().toString();
                String getContact = number.getText().toString();
                String getCustomer = amount.getText().toString();
                String getNote = note.getText().toString();
                if(getTime.isEmpty() || getDate.isEmpty() || getContact.isEmpty() || getCustomer.isEmpty()){
                    Toast.makeText(fill_up.this, "Please fill in the form!", Toast.LENGTH_SHORT).show();
                }
                else {

                    Bundle b = getIntent().getExtras();
                    String id = databaseReference.push().getKey();
                    booking db = new booking(id, b.getString("Email"), b.getString("Name"),
                            getTime, getDate, getContact, getCustomer, getNote,
                            b.getString("Address"), b.getString("Image"));
                    databaseReference.child(id).setValue(db);
                    Intent i = new Intent(fill_up.this, confirm.class);
                    i.putExtra("Email", b.getString("Email"));
                    startActivity(i);
                    finish();

                }
            }
        });
    }
}
