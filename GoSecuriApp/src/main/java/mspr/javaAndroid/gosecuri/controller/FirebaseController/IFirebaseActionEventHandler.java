package mspr.javaAndroid.gosecuri.controller.FirebaseController;


import com.google.firebase.firestore.QuerySnapshot;


public interface IFirebaseActionEventHandler {
    void onFileRetrieved(Object output);
    void onFileAdded(Object output);
    void onDocumentRetrieved(QuerySnapshot output);
    void IsSuccessful(Boolean output);

}
