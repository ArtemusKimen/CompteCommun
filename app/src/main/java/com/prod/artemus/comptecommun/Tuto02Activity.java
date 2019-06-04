package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Tuto02Activity extends AppCompatActivity {

    private ImageButton next02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto02);

        next02 = findViewById(R.id.next02);

        next02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextTuto = new Intent();
                setResult(RESULT_OK, nextTuto);
                finish();
            }
        });



    }

}
