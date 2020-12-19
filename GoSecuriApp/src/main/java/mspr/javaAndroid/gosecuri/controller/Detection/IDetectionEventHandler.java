package mspr.javaAndroid.gosecuri.controller.Detection;

import java.util.List;

import mspr.javaAndroid.gosecuri.model.Visitor;

public interface IDetectionEventHandler {
    void processFinish(Object object);
    void onProcessFail();
    void onTextDetected(List<String> texts);
    void onFaceDetected(byte[] face);
    void onFaceCompared(boolean output);
}
