package com.tencent.nanodetncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Help extends Activity {
    /*帮助与反馈（静态界面）*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help);

        Button exit=this.findViewById(R.id.iv_back);
        exit.setOnClickListener(v -> {
            Intent intent=new Intent(Help.this,Main.class);
            startActivity(intent);
        });

    }


}