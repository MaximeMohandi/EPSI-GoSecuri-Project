package mspr.javaAndroid.gosecuri.controller.FirebaseController;

import com.google.firebase.firestore.QuerySnapshot;

public abstract class FirebaseActionCallback implements IFirebaseActionEventHandler {

    @Override
    public void onFileAdded(Object output) {
    }

    @Override
    public void onDocumentRetrieved(QuerySnapshot output) {

    }

    @Override
    public void IsSuccessful(Boolean output) {
    }

    @Override
    public void onFileRetrieved(Object output) {

    }
}
