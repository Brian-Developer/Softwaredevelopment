package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class register extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText username, pass, repass, number;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        number = findViewById(R.id.number);
        TextView registered = findViewById(R.id.registered);
        registered.setOnClickListener(v -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
        });
    }

    public void onClick(View v){
        String getuser = username.getText().toString().trim();
        String getpass = pass.getText().toString().trim();
        String getrepass = repass.getText().toString().trim();
        String getnumber = number.getText().toString().trim();

        if (getuser.isEmpty() || getpass.isEmpty() || getrepass.isEmpty()){
            Toast.makeText(register.this, "Please enter username or password!", Toast.LENGTH_LONG).show();
        }
        else if(!getpass.equals(getrepass)){
            Toast.makeText(register.this, "Password does not match!", Toast.LENGTH_LONG).show();
        }
        else if(getpass.length() < 5){
            Toast.makeText(register.this, "Password too short!", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(getuser, getpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = databaseReference.push().getKey();
                        userdata db = new userdata(id, getuser, md5(getpass) ,getnumber);
                        databaseReference.child(id).setValue(db);
                        Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
