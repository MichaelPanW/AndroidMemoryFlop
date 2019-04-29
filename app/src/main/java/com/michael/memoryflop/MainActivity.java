package com.michael.memoryflop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner spinner_size,spinner_time;
    Button button_submit;
    int[] timebox={30,60,999};
    int[] sizebox={2,3,4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setViewData();
        setOnClick();
    }
    void findViewById(){
        spinner_size=(Spinner)findViewById(R.id.spinner_size);
        spinner_time=(Spinner)findViewById(R.id.spinner_time);
        button_submit=(Button)findViewById(R.id.button_submit);
    }
    void setViewData(){
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(sizeAdapter);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(timeAdapter);

    }

    void setOnClick(){
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                int time = spinner_time.getSelectedItemPosition();
                int size = spinner_size.getSelectedItemPosition();
                Log.e("findData time:", String.valueOf(time));
                Log.e("findData size:", String.valueOf(size));
                intent.putExtra("time",timebox[time]);
                intent.putExtra("size",sizebox[size]);
                startActivity(intent);
            }
        });

    }
}
