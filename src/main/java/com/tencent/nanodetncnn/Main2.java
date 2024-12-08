package com.tencent.nanodetncnn;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Main2 extends FragmentActivity {
    /*志愿者端主页面
     * Home：资讯界面
     * Call：用户动态界面
     * Set：用户资料界面
     */
    private Fragment HomePage = new HomeFragment2();
    private Fragment CallPage = new CallFragment2();
    private Fragment SetPage = new UserFragment();
    private RadioButton ToHome;
    private RadioButton ToCall;
    private RadioButton ToSet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        RadioGroup rg=this.findViewById(R.id.radioGroup);
        ToHome=this.findViewById(R.id.rbHome);
        ToCall=this.findViewById(R.id.rbCall);
        ToSet=this.findViewById(R.id.rbMe);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomePage)
                .commit();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rbHome){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, HomePage)
                            .commit();
                } else if (i==R.id.rbCall) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, CallPage)
                            .commit();
                }else if(i==R.id.rbMe) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, SetPage)
                            .commit();
                }

            }
        }
        );
    }}