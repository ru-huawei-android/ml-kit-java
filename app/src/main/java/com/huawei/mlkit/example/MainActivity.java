package com.huawei.mlkit.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.huawei.mlkit.example.bankCard.BcrAnalyseActivity;
import com.huawei.mlkit.example.face.LiveFaceAnalyseActivity;
import com.huawei.mlkit.example.translate.TranslatorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.findViewById(R.id.btn_face_live).setOnClickListener(this);
        this.findViewById(R.id.btn_translate).setOnClickListener(this);
        this.findViewById(R.id.btn_bcr).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_face_live:
                this.startActivity(new Intent(MainActivity.this, LiveFaceAnalyseActivity.class));
                break;
            case R.id.btn_translate:
                this.startActivity(new Intent(MainActivity.this, TranslatorActivity.class));
                break;
            case R.id.btn_bcr:
                this.startActivity(new Intent(MainActivity.this, BcrAnalyseActivity.class));
                break;
            default:
                break;
        }
    }
}
