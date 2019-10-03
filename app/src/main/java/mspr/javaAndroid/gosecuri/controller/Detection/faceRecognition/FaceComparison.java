package mspr.javaAndroid.gosecuri.controller.Detection.faceRecognition;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.InvalidParameterException;

import java.nio.ByteBuffer;
import java.util.List;
import mspr.javaAndroid.gosecuri.controller.Detection.IDetectionCallback;

public class FaceComparison {

    private List<byte[]> _toCompare;
    private List<byte[]>  _comparedTo;

    private IDetectionCallback callback;

    private String accessKey = "";
    private String secretKey = "";


    private AmazonRekognition awsClient = new AmazonRekognitionClient(new BasicAWSCredentials(accessKey,secretKey));

    public FaceComparison(){}

    public FaceComparison(List<byte[]> toCompare, List<byte[]> comparedTo){
        this._toCompare = toCompare;
        this._comparedTo = comparedTo;
    }

    public void Compare(IDetectionCallback callback){

        Float similarityThreshold = 70F;

        if(_toCompare == null){
            callback.onProcessFail();
        }

        //pour chaque image à comparer
        for (byte[] visageToCompare:_toCompare) {

            //pour chaque images comparé a l'image qu'on compare
            for (byte[] img : _comparedTo) {
                try {

                    CompareFacesRequest request = new CompareFacesRequest()
                            .withSourceImage(new Image().withBytes(ByteBuffer.wrap(visageToCompare)))
                            .withTargetImage(new Image().withBytes(ByteBuffer.wrap(img)))
                            .withSimilarityThreshold(similarityThreshold);

                    // Call operation
                    CompareFacesResult compareFacesResult = awsClient.compareFaces(request);


                    // récupère les visages qui correspondent
                    List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();

                    //pour chaque image ayant une correspondance
                    for (CompareFacesMatch match : faceDetails) {
                        ComparedFace face = match.getFace();

                        //on donne un résultat positif si le taux de confiance est supérieur a 90%
                        if (face.getConfidence() > 85) {
                            callback.onFaceCompared(true);
                            return;
                        }
                    }
                }catch (InvalidParameterException ex){
                    ex.printStackTrace();
                    continue;
                }
            }
        }

        callback.onFaceCompared(false);

    }

}
