package com.cm0573.w16041611.k5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class confirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Button back = (Button)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(confirm.this, MainActivity.class);
                i.putExtra("Email", getIntent().getExtras().getString("Email"));
                i.putExtra("Guest", "No");
                startActivity(i);
                finish();
            }
        });
    }
}
