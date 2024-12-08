package com.tencent.nanodetncnn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.menu4, container, false);

        Button ToSet=(Button)view.findViewById(R.id.set);
        ToSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), Set.class);
                startActivity(intent);
            }
        });


        Button ToHelp=(Button)view.findViewById(R.id.help);
        ToHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), Help.class);
                startActivity(intent);
            }
        });


        Button ToMe=(Button)view.findViewById(R.id.me);
        ToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), About.class);
                startActivity(intent);
            }
        });


        Button Out=(Button)view.findViewById(R.id.back);
        Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });



        return view;
    }

}
