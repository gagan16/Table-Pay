package com.gjs.opentable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.gjs.opentable.Adapter.DownloadImageTask;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView ivIcon=(ImageView)findViewById(R.id.imageView4);
        ivIcon.setImageResource(R.drawable.unnamed);
      /*  handler.sendEmptyMessageDelayed(101, 3000);



    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101) {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        }
    };*/

    preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);

    //String str = preferences.getString(Util.KEY_NAME,"");

    if(preferences.contains(Util.KEY_EMAIL)){
        handler.sendEmptyMessageDelayed(102, 3000);
    }else{
        handler.sendEmptyMessageDelayed(101, 3000);
    }
}

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101) {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };
}
