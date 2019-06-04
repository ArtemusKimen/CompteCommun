package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Tuto04Activity extends AppCompatActivity {

    private ImageButton next04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto04);

        next04 = findViewById(R.id.next04);

        next04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextTuto = new Intent();
                setResult(RESULT_OK, nextTuto);
                finish();
            }
        });



    }

}
