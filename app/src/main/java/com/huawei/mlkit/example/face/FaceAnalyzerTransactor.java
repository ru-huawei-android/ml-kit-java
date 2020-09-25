package com.huawei.mlkit.example.face;

import android.util.SparseArray;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.mlkit.example.camera.GraphicOverlay;

public class FaceAnalyzerTransactor implements MLAnalyzer.MLTransactor<MLFace> {
    private GraphicOverlay mGraphicOverlay;

    FaceAnalyzerTransactor(GraphicOverlay ocrGraphicOverlay) {
        this.mGraphicOverlay = ocrGraphicOverlay;
    }

    @Override
    public void transactResult(MLAnalyzer.Result<MLFace> result) {
        this.mGraphicOverlay.clear();
        SparseArray<MLFace> faceSparseArray = result.getAnalyseList();
        for (int i = 0; i < faceSparseArray.size(); i++) {
            MLFaceGraphic graphic = new MLFaceGraphic(this.mGraphicOverlay, faceSparseArray.valueAt(i));
            this.mGraphicOverlay.add(graphic);
        }
    }

    @Override
    public void destroy() {
        this.mGraphicOverlay.clear();
    }

}