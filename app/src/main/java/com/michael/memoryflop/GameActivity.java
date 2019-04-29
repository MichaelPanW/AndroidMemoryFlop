package com.michael.memoryflop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ImgBox_linear;
    TextView brewTimeLabel;
    int time,size,width,height;//runtime,card width and height size,card width,card height
    Button arrbut[][];//2d Button
    int arrsou[][];//2d card index
    Drawable backImg;//default close card resource
    CountDownTimer timer;//run time
    int finishtime=0;//record finish time
    int count=0;//count success card count
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
        brewTimeLabel=(TextView) findViewById(R.id.brewTimeLabel);
        arrbut = new Button[size][size];
        arrsou = new int[size][size];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width=metrics.widthPixels/size;
        //keep 250px to set time label
        height=(metrics.heightPixels-250)/size;
        timer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                brewTimeLabel.setText(time-(millisUntilFinished / 1000) + "秒");
                finishtime=time-(int)(millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                finishGame("失敗");

            }
        };
        timer.start();
    }
    void finishGame(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        builder.setMessage("您花了"+finishtime+"秒鐘，完成了"+count+"張牌")
                .setTitle(title);
        builder.setPositiveButton("結束", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GameActivity.this.finish();
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    void setViewData(){
        //random possible choice
        List<Integer> randbox = new ArrayList();//[0,0,1,1,2,2,3,3]
        for(int i=0;i<size*size/2;i++){
            for(int j=0;j<2;j++){
                randbox.add(i);
            }
        }

        LinearLayout.LayoutParams Linparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams Butparams = new LinearLayout.LayoutParams(width, height);
        Linparams.weight = 1;
        Butparams.weight = 1;
        backImg = getResources().getDrawable( R.drawable.back );
        Random ran = new Random();
        int newIndex=0;

        for(int i=0;i<size;i++){
            LinearLayout tv=new LinearLayout(this);
            tv.setLayoutParams(Linparams);
            ImgBox_linear.addView(tv);
            for(int j=0;j<size;j++){
                JSONObject holder =new JSONObject();
                Button newbut=new Button(this);
                arrbut[i][j]=newbut;
                arrbut[i][j].setBackground(backImg);
                arrbut[i][j].setLayoutParams(Butparams);
                newIndex=ran.nextInt(randbox.size());//from random box choice one index
                int getIndex=randbox.get(newIndex);
                arrbut[i][j].setOnClickListener(this);
                try {
                    //set button tag content
                    holder.put("x",i);
                    holder.put("y",j);
                    holder.put("index",getIndex);//image index
                    holder.put("img",imgbox[getIndex]);//image resource
                    holder.put("status",1);//image status 0:is open 1:is cover
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrbut[i][j].setTag(holder);//set image
                arrsou[i][j]=getIndex;
                randbox.remove(newIndex);

                tv.addView(arrbut[i][j]);

            }

        }
    }
    int coverX=-1,coverY=-1;
    boolean animateStop=false;
    //when user click the card
    @Override
    public void onClick(View v) {
        final JSONObject getdata= (JSONObject)(v.getTag());
        Drawable thisImg = null;
        if(animateStop)return;//animate delay
        try {
            if(getdata.getInt("status")==1){//is choice is not cover
                animateStop=true;
                thisImg = getResources().getDrawable(getdata.getInt("img"));
                v.setBackground(thisImg);
                if(coverX==-1){//first open card
                    coverX=getdata.getInt("x");
                    coverY=getdata.getInt("y");
                    animateStop=false;

                }else{//second open card
                    if(arrsou[coverX][coverY]==arrsou[getdata.getInt("x")][getdata.getInt("y")]){//check first and second time open card
                        JSONObject holder =new JSONObject();
                        holder.put("status",0);//set true card id opening
                        arrbut[coverX][coverY].setTag(holder);
                        arrbut[getdata.getInt("x")][getdata.getInt("y")].setTag(holder);
                        animateStop=false;
                        coverX=-1;
                        coverY=-1;
                        count++;
                        if(count==size*size/2){//if finish all card
                            finishGame("勝利");
                        }
                    }else{
                        Handler handler = new Handler();
                        //delay 1 sec to close card
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Drawable backImg = getResources().getDrawable( R.drawable.back );
                                arrbut[coverX][coverY].setBackground(backImg);
                                try {
                                    //set close card
                                    arrbut[getdata.getInt("x")][getdata.getInt("y")].setBackground(backImg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                coverX=-1;
                                coverY=-1;
                                animateStop=false;
                            }
                        }, 1000);

                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
