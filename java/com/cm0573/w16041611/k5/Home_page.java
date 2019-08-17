package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_page extends AppCompatActivity {
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        login = findViewById(R.id.login);

        TextView guest = (TextView) findViewById(R.id.guest);
        TextView register = (TextView) findViewById(R.id.register);
        guest.setMovementMethod(LinkMovementMethod.getInstance());
        guest.setOnClickListener(v -> {
            Intent intent = new Intent(Home_page.this, MainActivity.class);
            intent.putExtra("Guest", "Yes");
            startActivity(intent);
        });
        register.setOnClickListener(v -> {
            Intent intent = new Intent(Home_page.this, register.class);
            startActivity(intent);
        });
    }
    public void onClick(View v){
        Intent i = new Intent(Home_page.this, login.class);
        startActivity(i);
        finish();
    }
}
