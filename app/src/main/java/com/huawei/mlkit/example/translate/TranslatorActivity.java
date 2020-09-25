package com.huawei.mlkit.example.translate;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.langdetect.MLDetectedLang;
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory;
import com.huawei.hms.mlsdk.translate.MLTranslateLanguage;
import com.huawei.hms.mlsdk.translate.local.*;
import com.huawei.hms.mlsdk.langdetect.local.*;
import com.huawei.hms.mlsdk.model.download.*;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.mlkit.example.R;

import java.util.List;
import java.util.Set;

public class TranslatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;

    private EditText mEditText;

    public MLLocalTranslator translator;

    private MLLocalLangDetector langDetector;

    public String sourceText;

    public Object[] langs;

    public int i;

    MLModelDownloadStrategy downloadStrategy = new MLModelDownloadStrategy.Factory().needWifi().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_translator);
        this.mTextView = this.findViewById(R.id.tv_output);
        this.mEditText = this.findViewById(R.id.et_input);
        this.findViewById(R.id.btn_translator).setOnClickListener(this);
        MLApplication.getInstance().setApiKey(getString(R.string.apikey));
        sourceText = this.mEditText.getText().toString();
        MLTranslateLanguage.getCloudAllLanguages().addOnSuccessListener(
                new OnSuccessListener<Set<String>>() {
                    @Override
                    public void onSuccess(Set<String> result) {
                        langs = result.toArray();
                        i = 0;
                        Download d = new Download();
                        d.start();
                    }
                });
        MLLocalLangDetectorSetting setting = new MLLocalLangDetectorSetting.Factory().create();
        this.langDetector = MLLangDetectorFactory.getInstance().getLocalLangDetector(setting);
    }

    private void localTranslator() {
        sourceText = this.mEditText.getText().toString();
        Task<List<MLDetectedLang>> task = this.langDetector.probabilityDetect(sourceText);
        task.addOnSuccessListener(new OnSuccessListener<List<MLDetectedLang>>() {
            @Override
            public void onSuccess(List<MLDetectedLang> result) {
                MLLocalTranslateSetting setting =
                        new MLLocalTranslateSetting.Factory().setSourceLangCode(result.get(0).getLangCode()).setTargetLangCode("ru").create();
                translator = MLTranslatorFactory.getInstance().getLocalTranslator(setting);
                Task<String> task1 = translator.asyncTranslate(sourceText);
                task1.addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String text) {
                        // Recognition success.
                        mTextView.setText(text);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_translator:
                this.localTranslator();
                break;
            default:
                break;
        }
    }

    class Download extends Thread {
        String langCode;

        public void run() {
            langCode = langs[i].toString();
            MLLocalModelManager manager = MLLocalModelManager.getInstance();
            MLLocalTranslatorModel model = new MLLocalTranslatorModel.Factory(langCode).create();
            manager.downloadModel(model, downloadStrategy).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mTextView.setText(mTextView.getText() + " " + langCode + getString(R.string.modelsuccess));
                    i++;
                    if (i < langs.length) {
                        Download d = new Download();
                        d.start();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    mTextView.setText(mTextView.getText() + " " + langCode + getString(R.string.nomodel));
                    i++;
                    if (i < langs.length) {
                        Download d = new Download();
                        d.start();
                    }
                }
            });
        }
    }
}


