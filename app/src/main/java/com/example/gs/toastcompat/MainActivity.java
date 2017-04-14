package com.example.gs.toastcompat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.gs.toast.CustomToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
/*         new CustomToast.Builder(this).setIsSingle(true).setText("1").setDuration(2000).build()
.show();
        new CustomToast.Builder(this).setIsSingle(true).setText("2").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("3").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("4").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("5").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("6").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("7").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("8").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("9").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("10").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("11").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("12").setDuration(2000).build()
        .show();
        new CustomToast.Builder(this).setIsSingle(true).setText("13").setDuration(2000).build()
        .show();*/

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        new CustomToast.Builder(this).setIsSingle(true).setView(imageView).setDuration(2000).build()
                .show();
        new CustomToast.Builder(this).setIsSingle(true).setView(imageView).setDuration(2000).build()
                .show();
        new CustomToast.Builder(this).setIsSingle(true).setView(imageView).setDuration(2000).build()
                .show();

       /* new CustomToast.Builder(this).setText("你好").setDuration(2000).build()
                .show();
        new CustomToast.Builder(this).setText("你好l").setDuration(2000).build()
                .show();
        new CustomToast.Builder(this).setText("你好123").setDuration(2000).build
                ().show();*/

    /*    ToastUtil.setToast(this,"你好");
        ToastUtil.setToast(this,"你好1");
        ToastUtil.setToast(this,"你好2");*/
    }

}
