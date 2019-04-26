package com.michael.memoryflop;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ImgBox_linear;
    int time,size,width,height;
    Button arrbut[][];
    int arrsou[][];
    int[] imgbox={R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,R.drawable.p11,R.drawable.p12,R.drawable.p13};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        dealIntent();
        findViewById();
        setViewData();

    }
    void dealIntent(){
        Intent intent=getIntent();
        time=intent.getIntExtra("time",30);
        size=intent.getIntExtra("size",2);

    }
    void findViewById(){
        ImgBox_linear=(LinearLayout) findViewById(R.id.ImgBox_linear);
         arrbut = new Button[size][size];
         arrsou = new int[size][size];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width=metrics.widthPixels/size;
        height=metrics.heightPixels/size-30;
    }
    void setViewData(){
        List<Integer> randbox = new ArrayList();

        for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            randbox.add(i);
        }
        }

        LinearLayout.LayoutParams Linparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams Butparams = new LinearLayout.LayoutParams(width, height);
        Linparams.weight = 1;
        Butparams.weight = 1;
        Drawable backImg = getResources().getDrawable( R.drawable.back );
        Random ran = new Random();
        int newIndex=0;
        for(int i=0;i<size;i++){
            LinearLayout tv=new LinearLayout(this);
            tv.setLayoutParams(Linparams);
            ImgBox_linear.addView(tv);
            for(int j=0;j<size;j++){
                arrbut[i][j] = new Button(this);
                arrbut[i][j].setBackground(backImg);
                arrbut[i][j].setLayoutParams(Butparams);
                if(randbox.size()>0){

                     newIndex=ran.nextInt(randbox.size());
                }else{
                    newIndex=0;
                }
                arrbut[i][j].setOnClickListener(this);
                arrbut[i][j].setTag(imgbox[randbox.get(newIndex)]);
                arrsou[i][j]=imgbox[randbox.get(newIndex)];
                randbox.remove(newIndex);

                tv.addView(arrbut[i][j]);

            }

        }
        width=arrbut[0][0].getWidth();
        height=arrbut[0][0].getHeight();

    }

    @Override
    public void onClick(View v) {
        Drawable thisImg = getResources().getDrawable((Integer) v.getTag());
        v.setBackground(thisImg);
    }
}
