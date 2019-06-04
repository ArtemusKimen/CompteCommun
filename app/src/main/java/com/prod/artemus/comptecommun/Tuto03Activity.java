package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Tuto03Activity extends AppCompatActivity {

    private ImageButton next03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto03);

        next03 = findViewById(R.id.next03);

        next03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextTuto = new Intent();
                setResult(RESULT_OK, nextTuto);
                finish();
            }
        });



    }

}
