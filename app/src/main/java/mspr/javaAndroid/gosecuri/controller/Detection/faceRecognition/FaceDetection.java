package mspr.javaAndroid.gosecuri.controller.Detection.faceRecognition;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import mspr.javaAndroid.gosecuri.controller.Detection.Detection;
import mspr.javaAndroid.gosecuri.controller.Detection.IDetectionCallback;

public class FaceDetection extends Detection {


    private DetectFacesResult detectFacesResult;
    private byte[] faceId;

    public FaceDetection(byte[] imageSource) {
        super(imageSource);
    }
    public void Detect(IDetectionCallback callback){
        try
        {
            //demande a aws de trouver le visage
            DetectFacesRequest request = new DetectFacesRequest()
                    .withAttributes(Attribute.ALL.toString())
                    .withImage(super.image);

            //retourne la position des visages trouvé
            detectFacesResult = super.awsClient.detectFaces(request);

            //converti la photo original en bitmap pour la traiter
            Bitmap originalImage = BitmapFactory.decodeByteArray(super.sourceImage,0,sourceImage.length);

            //pour chaque visage detecter par aws
            for (com.amazonaws.services.rekognition.model.FaceDetail face : detectFacesResult.getFaceDetails()) {

                try
                {
                    //on rogne l'image d'origine pour garder seulement le visage
                    callback.onFaceDetected(CropFaceInImage(originalImage,face));

                } catch (FileNotFoundException e) {
                    Log.e("FaceDetection error : ",e.getMessage());
                    callback.onProcessFail();
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("FaceDetection error : ",ex.getMessage());
            callback.onProcessFail();
        }
    }

    //rogne l'image pour récupérer juste la tête
    private byte[] CropFaceInImage(Bitmap originalImage,com.amazonaws.services.rekognition.model.FaceDetail face) throws FileNotFoundException {

        //position de la tête
        float left =  (originalImage.getWidth()*face.getBoundingBox().getLeft());
        float top = (originalImage.getHeight() * face.getBoundingBox().getTop());
        float width = (originalImage.getWidth()*face.getBoundingBox().getWidth());
        float height = (originalImage.getHeight()*face.getBoundingBox().getHeight());

        if(detectFacesResult.getOrientationCorrection() != null)
        {
            //calcul la position de la tête selon la rotation de l'image
            switch (detectFacesResult.getOrientationCorrection()) {
                case "ROTATE_0":
                    left =  (originalImage.getWidth()*face.getBoundingBox().getLeft());
                    top = (originalImage.getHeight() * face.getBoundingBox().getTop());
                    break;
                case "ROTATE_90":
                    left =(originalImage.getHeight() * (1 - (face.getBoundingBox().getTop() + face.getBoundingBox().getHeight())));
                    top = (originalImage.getWidth() * face.getBoundingBox().getLeft());
                    break;
                case "ROTATE_180":
                    left = (originalImage.getWidth() - (originalImage.getWidth() * (face.getBoundingBox().getLeft() + face.getBoundingBox().getWidth())));
                    top = (originalImage.getHeight() * (1 - (face.getBoundingBox().getTop() + face.getBoundingBox().getHeight())));
                    break;
                case "ROTATE_270":
                    left = (originalImage.getHeight() * face.getBoundingBox().getTop());
                    top = (originalImage.getWidth() * (1 - face.getBoundingBox().getLeft() - face.getBoundingBox().getWidth()));
                    break;
                default:
                    System.out.println("No estimated orientation information. Check Exif data.");
                    break;
            }
    }
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        //créer une nouvelle image en rognant l'original au dimension du viasge x un padding
        Bitmap.createBitmap(originalImage,(int)(top*1),(int)left,(int)(width*1.75),(int)(height*1.75)).compress(Bitmap.CompressFormat.JPEG,100,byteArray);

        //retourne un jpeg
        return byteArray.toByteArray();
    }



}
