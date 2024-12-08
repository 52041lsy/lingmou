package com.tencent.nanodetncnn;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Main extends FragmentActivity {
/*患者端主页面
 * Home：识别功能目录界面
 * Call：紧急呼叫界面
 * Set：用户资料界面
 */
    private Fragment HomePage = new HomeFragment();
    private Fragment CallPage = new CallFragment();
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
            //监听底部导航栏按钮，注入响应Fragment
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if(i==R.id.rbHome){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, HomePage)
                                .commit();
                    }
                    if (i==R.id.rbCall) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, CallPage)
                                .commit();
                    }
                    if(i==R.id.rbMe) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, SetPage)
                                .commit();
                    }

                }
            }
        );
    }
}


