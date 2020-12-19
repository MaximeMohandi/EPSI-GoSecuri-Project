package mspr.javaAndroid.gosecuri.controller.Detection.textRecognition;



import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.ImageTooLargeException;

import java.util.ArrayList;
import java.util.List;

import mspr.javaAndroid.gosecuri.controller.Detection.Detection;
import mspr.javaAndroid.gosecuri.controller.Detection.IDetectionCallback;

public class TextDetection extends Detection  {

    private DetectTextResult detectTextResult;
    private List<String> result = new ArrayList<>();

    public TextDetection(byte[] imageSource) {
        super(imageSource);
    }

    public void Detect(IDetectionCallback callback){
        try
        {
            DetectTextRequest request = new DetectTextRequest().withImage(super.image);
            detectTextResult = super.awsClient.detectText(request);
            //liste de tous les textes trouvés par aws
            List<com.amazonaws.services.rekognition.model.TextDetection> textDetections = detectTextResult.getTextDetections();

            //pour chaque texte détecté
            for (com.amazonaws.services.rekognition.model.TextDetection textDetected : textDetections) {

                result.add(textDetected.getDetectedText());
            }
            callback.onTextDetected(result);
        }
        catch (ImageTooLargeException ex)
        {
            ex.printStackTrace();
        }
    }
}

