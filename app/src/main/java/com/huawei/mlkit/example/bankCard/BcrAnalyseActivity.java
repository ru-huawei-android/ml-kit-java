package com.huawei.mlkit.example.bankCard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCapture;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureConfig;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureFactory;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureResult;
import com.huawei.mlkit.example.R;


public class BcrAnalyseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;

    private ImageView previewImage;

    private String cardResultFront = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_image_bcr_analyse);
        this.mTextView = this.findViewById(R.id.text_result);
        this.previewImage = this.findViewById(R.id.Bank_Card_image);
        this.previewImage.setScaleType(ImageView.ScaleType.FIT_XY);
        this.findViewById(R.id.detect).setOnClickListener(this);
        this.previewImage.setOnClickListener(this);
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            this.requestCameraPermission();
        }
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            this.requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            int CAMERA_PERMISSION_CODE = 100;
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            int READ_EXTERNAL_STORAGE_CODE = 200;
            ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        mTextView.setText("");
        this.startCaptureActivity(this.banCallback);
    }

    private String formatIdCardResult(MLBcrCaptureResult bankCardResult) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(bankCardResult.getNumber());
        resultBuilder.append("\r\n");
        resultBuilder.append(bankCardResult.getOrganization());
        resultBuilder.append("\r\n");
        resultBuilder.append(bankCardResult.getExpire());
        return resultBuilder.toString();
    }

    private void displayFailure() {
        mTextView.setText(getString(R.string.onfail));
    }

    private MLBcrCapture.Callback banCallback = new MLBcrCapture.Callback() {
        @Override
        public void onSuccess(MLBcrCaptureResult bankCardResult) {
            if (bankCardResult == null) {
                return;
            }
            Bitmap bitmap = bankCardResult.getOriginalBitmap();
            BcrAnalyseActivity.this.previewImage.setImageBitmap(bitmap);
            BcrAnalyseActivity.this.cardResultFront = BcrAnalyseActivity.this.formatIdCardResult(bankCardResult);
            BcrAnalyseActivity.this.mTextView.setText(BcrAnalyseActivity.this.cardResultFront);
        }

        @Override
        public void onCanceled() {

        }

        @Override
        public void onFailure(int recCode, Bitmap bitmap) {
            BcrAnalyseActivity.this.displayFailure();
        }

        @Override
        public void onDenied() {
            BcrAnalyseActivity.this.displayFailure();
        }
    };

    private void startCaptureActivity(MLBcrCapture.Callback Callback) {
        MLBcrCaptureConfig config = new MLBcrCaptureConfig.Factory().setResultType(MLBcrCaptureConfig.RESULT_ALL)
                .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
                .create();
        MLBcrCapture bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config);
        bcrCapture.captureFrame(this, Callback);
    }
}