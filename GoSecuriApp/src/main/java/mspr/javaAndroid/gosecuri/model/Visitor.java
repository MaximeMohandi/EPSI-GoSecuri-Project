package mspr.javaAndroid.gosecuri.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;

import mspr.javaAndroid.gosecuri.controller.FirebaseController.FirebaseAction;
import mspr.javaAndroid.gosecuri.controller.FirebaseController.FirebaseActionCallback;
import mspr.javaAndroid.gosecuri.controller.Identity.IdentityCheckerCallback;

public class Visitor{

    private String name;
    private String firstname;
    private String idCard;
    private Date lastVisit;
    private List<byte[]> images;

    public Visitor(){}
    public Visitor(String name, String firstname, String IDCard,Date lastVisite, List<byte[]> image) {
        this();
        this.name = name;
        this.firstname = firstname;
        this.idCard = IDCard;
        this.images = image;
        this.lastVisit = lastVisite;
    }


    //===============================Getter Setter======================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }


    //====================================Function=====================================
    //sauvegarde un visiteur
    public void SaveVisitor(final IdentityCheckerCallback callback)
    {

        final FirebaseAction action = new FirebaseAction();

        //Prepare l'objet a entrer en base de données
        final Map<String,Object> visitor = new HashMap<>();
        visitor.put("name",name);
        visitor.put("firstname",firstname);
        visitor.put("idCard",idCard);
        visitor.put("lastVisite",new Date());

        final String subCollectionID = UUID.randomUUID().toString();

        //enregistre les images dans le storage firebase
        new Thread(new Runnable() {
            @Override
            public void run() {
                action.AddMultipleImagesToDocument(images, "visiteur", new FirebaseActionCallback() {

                    @Override
                    public void onFileAdded(Object output) {
                        ArrayList imagesList = (ArrayList<String>)output;
                        //ajoute le nom de la souscollection
                        for (int i = 0; i < imagesList.size() ; i++) {
                            visitor.put("image"+i,imagesList.get(i));
                        }

                        //enregistre le visiteur
                        action.AddDocument(visitor, "visiteur", new FirebaseActionCallback() {
                            @Override
                            public void IsSuccessful(Boolean output) {
                                if(output){
                                    callback.onVisitorAdded();
                                    Thread.currentThread().interrupt();
                                }else{
                                    callback.onVisitorNotAdded();
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
                    }
                });
            }
        }).start();



    }

    public void UpdateVisitorVisite(final IdentityCheckerCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new FirebaseAction().GetDocument("visiteur", "idCard", idCard, new FirebaseActionCallback() {
                    @Override
                    public void onDocumentRetrieved(QuerySnapshot output) {

                        DocumentSnapshot doc = (DocumentSnapshot) output.getDocuments().get(0);

                        new FirebaseAction().UpdateDocument("visiteur",doc.getId(), "lastVisite",new Date() , new FirebaseActionCallback() {
                            @Override
                            public void IsSuccessful(Boolean output) {
                                callback.onSuccess(output);
                                Thread.currentThread().interrupt();
                            }
                        });
                    }
                });
            }
        }).start();


    }

    public void GetAllVisitors(final IdentityCheckerCallback callback){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new FirebaseAction().GetAllDocuments("visiteur", new FirebaseActionCallback() {

                    @Override
                    public void onDocumentRetrieved(QuerySnapshot output){


                        final List<Visitor> visitorsList = new ArrayList<>();

                        for(final DocumentSnapshot doc : output){


                            new FirebaseAction().GetByteFiles(Arrays.asList(doc.get("image0").toString(),doc.get("image1").toString()), new FirebaseActionCallback() {
                                @Override
                                public void onFileRetrieved(Object output) {

                                    Timestamp date = (Timestamp) doc.get("lastVisite");

                                    visitorsList.add(new Visitor(
                                            (String) doc.get("firstname"),
                                            (String) doc.get("name"),
                                            (String) doc.get("idCard"),
                                            date.toDate(),
                                            (List<byte[]>)output

                                    ));

                                    callback.onListVisitorFound(visitorsList);
                                    Thread.currentThread().interrupt();
                                }
                            });
                        }
                    }

                    @Override
                    public void IsSuccessful(Boolean output) {
                        callback.onListVisitorNotFound();
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }).start();

    }

}
