package mspr.javaAndroid.gosecuri.controller.Identity;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import mspr.javaAndroid.gosecuri.controller.Detection.IDetectionCallback;
import mspr.javaAndroid.gosecuri.controller.Detection.faceRecognition.FaceDetection;
import mspr.javaAndroid.gosecuri.controller.Detection.textRecognition.TextDetection;
import mspr.javaAndroid.gosecuri.model.Visitor;

public class CardIDExtractInfo extends AsyncTask<IdentityCheckerCallback,Void,Visitor>{

    private byte[] _idCardPicture;
    private List<String> listText;
    private IdentityCheckerCallback callback;

    public CardIDExtractInfo(byte[] idCardPicture) {
        this._idCardPicture = idCardPicture;
    }


    @Override
    protected Visitor doInBackground(IdentityCheckerCallback... callbacks) {
        callback = callbacks[0];

        //récupere tout le texte sur la carte et cherche les bonne infos en prennant en compte
        //le spécifisité des carte d'identité française
        final Visitor frenchVisitor = new Visitor();
        final List<byte[]> imageVisitor = new ArrayList<>();

        try {
            new TextDetection(_idCardPicture).Detect(new IDetectionCallback() {
                @Override
                public void onTextDetected(final List<String> texts) {
                    new FaceDetection(_idCardPicture).Detect(new IDetectionCallback() {
                        @Override
                        public void onFaceDetected(byte[] face) {
                            //récupere l'image
                            imageVisitor.add(face);
                            frenchVisitor.setImages(imageVisitor);

                            for (String text : texts) {

                                //si le texte contient "<" symbole caracteristique CNI
                                if (text.contains("<")) {
                                    //si le texte commence par "IDFRA": chaine avant le nom sur les CNI
                                    if (text.startsWith("IDFRA")) {
                                        //le nom est égale au texte - "IDFRA" et les "<"
                                        frenchVisitor.setName(text.split("IDFRA")[1].split("<")[0]);
                                    }
                                    //sinon si le texte commence par des chiffres
                                    else if (Character.isDigit(text.charAt(0))) {

                                        //pour chaque caractere dans la chaine
                                        for (int i = 0; i < text.length(); i++) {

                                            //si le caractere n'est pas un chiffre
                                            if (!Character.isDigit(text.charAt(i))) {

                                                //le prenom = le text entre les chiffres et les "<"
                                                frenchVisitor.setFirstname(text.substring(i).split("<")[0]);

                                                //le numeros de carte = du premier caractere au dernier chiffre
                                                frenchVisitor.setIdCard(text.substring(0, i));
                                                break;
                                            }

                                        }
                                    }
                                }

                            }

                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return frenchVisitor;
    }

    @Override
    protected void onPostExecute(Visitor visitor) {
        callback.onVisitorFound(visitor);
    }
}