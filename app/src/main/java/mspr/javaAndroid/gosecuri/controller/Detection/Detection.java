package mspr.javaAndroid.gosecuri.controller.Detection;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Image;

import java.nio.ByteBuffer;

abstract public class Detection{

    protected IDetectionCallback callback;
    //Credentials
    private String accessKey = "";
    private String secretKey = "";

    //variable commune partagé par les enfants
    protected AmazonRekognition awsClient;
    protected byte[] sourceImage;
    protected Image image;

    //constructeur inialise l'image a traiter et le clien aws
    public Detection(byte[] imageSource)
    {
        this.sourceImage = imageSource;
        image = new Image().withBytes(ByteBuffer.wrap(sourceImage));
        this.awsClient = new AmazonRekognitionClient(new BasicAWSCredentials(accessKey,secretKey));

    }

}
