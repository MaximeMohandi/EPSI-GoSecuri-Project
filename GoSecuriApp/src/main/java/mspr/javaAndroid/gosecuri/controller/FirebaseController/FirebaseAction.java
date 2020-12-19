package mspr.javaAndroid.gosecuri.controller.FirebaseController;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FirebaseAction  {

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private List<String> filesUrl;
    public FirebaseAction()
    {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    //ajoute un document dans une collection donnnée
    public void AddDocument(final Map document, final String collection, final FirebaseActionCallback callback)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection(collection).add(document)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                        //lorsque le processus est terminé on signal le resultat du processus
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            callback.IsSuccessful(task.isSuccessful());
                            Thread.currentThread().interrupt();
                        }
                    });
            }
        }).start();

    }

    //ajoute plusieur image et le lié a un document
    public void AddMultipleImagesToDocument(final List<byte[]> files, final String collection, final FirebaseActionCallback callback) {
        final StorageReference storageRef = storage.getReference();

        final List<String> imgLinks = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //pour chaque imaages à enregistrer
                for (byte[] image : files) {
                    //donne un nom aléatoire
                    final StorageReference fileRef = storageRef.child(collection+"/"+UUID.randomUUID()+".jpg");
                    imgLinks.add(fileRef.getPath());

                    //upload l'image
                    fileRef.putBytes(image).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.IsSuccessful(false);
                            Thread.currentThread().interrupt();
                        }
                    });
                }
                //on signal que ça a réussi
                callback.onFileAdded(imgLinks);
                Thread.currentThread().interrupt();
            }
        }).start();



    }

    //récupere un ou plusieur document selon filtre
    public void GetDocument(final String collection, final String filterBy, final Object value, final FirebaseActionCallback callback){

        new Thread(new Runnable() {
            @Override
            public void run() {
                CollectionReference citiesRef = db.collection(collection);

                // Create a query against the collection.
                db.collection(collection).whereEqualTo(filterBy, value).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if(!documentSnapshots.isEmpty()){
                                    callback.onDocumentRetrieved(documentSnapshots);
                                    Thread.currentThread().interrupt();
                                }else{
                                    callback.IsSuccessful(false);
                                    Thread.currentThread().interrupt();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                       e.printStackTrace();
                    }
                });
            }
        }).start();



    }

    //récupère les documents dans la collection souhaité
    public void GetAllDocuments(final String collection, final FirebaseActionCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection(collection).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if(!documentSnapshots.isEmpty()){
                                    callback.onDocumentRetrieved(documentSnapshots);
                                    Thread.currentThread().interrupt();
                                }else{
                                    callback.IsSuccessful(false);
                                    Thread.currentThread().interrupt();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }).start();

    }

    //récupère les blobs des fichiers souhaité
    public void GetByteFiles(final List<String> paths, final FirebaseActionCallback callback){

        if(paths.isEmpty()) {
            callback.IsSuccessful(false);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StorageReference storageRef = storage.getReference();
                final int[] index = {0};

                final List<byte[]> listBytesFiles = new ArrayList<>();
                for(final String path : paths){
                    StorageReference pathReference = storageRef.child(path);

                    final long ONE_MEGABYTE = 1024 * 1024 *5;
                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            index[0]++;
                            listBytesFiles.add(bytes);
                            if(paths.size() == index[0]){
                                callback.onFileRetrieved(listBytesFiles);
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                }
            }

        }).start();
    }

    public void UpdateDocument(final String collection, final String document, final String field, final Object value, final FirebaseActionCallback callback){

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection(collection).document(document).update(field, value)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                callback.IsSuccessful(task.isSuccessful());
                                Thread.currentThread().interrupt();
                            }
                        });
            }
        }).start();


    }
}
