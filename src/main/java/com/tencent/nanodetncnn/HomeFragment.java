package com.tencent.nanodetncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu2, container, false);

        Button func1=(Button)view.findViewById(R.id.func1);
        func1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), Detect.class);
                startActivity(intent);
            }
        });

        Button func2=(Button)view.findViewById(R.id.func2);
        func2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), TextRec.class);
                startActivity(intent);
            }
        });

        Button func3=(Button)view.findViewById(R.id.func3);
        func3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Fragment 的上下文创建 Intent
                Intent intent = new Intent(getActivity(), Scene.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
