package mspr.javaAndroid.gosecuri.controller.Detection;

import java.util.List;

public abstract class IDetectionCallback implements IDetectionEventHandler {

    @Override
    public void processFinish(Object object) {

    }

    @Override
    public void onTextDetected(List<String> texts) {

    }

    @Override
    public void onFaceDetected( byte[] face) {

    }

    @Override
    public void onFaceCompared(boolean output) {

    }

    @Override
    public void onProcessFail() {

    }
}
