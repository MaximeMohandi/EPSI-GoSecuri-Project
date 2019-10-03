package mspr.javaAndroid.gosecuri;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mspr.javaAndroid.gosecuri.controller.FirebaseController.FirebaseAction;
import mspr.javaAndroid.gosecuri.controller.FirebaseController.FirebaseActionCallback;
import mspr.javaAndroid.gosecuri.controller.Identity.IdentityCheckerCallback;
import mspr.javaAndroid.gosecuri.model.Visitor;

@RunWith(AndroidJUnit4.class)
public class FirebaseTest {

    @Test
    public void Test_GetDocument(){
        FirebaseActionCallback callbackTest = new FirebaseActionCallback() {

            @Override
            public void onDocumentRetrieved(QuerySnapshot output) {
                Assert.assertTrue(output.isEmpty() == false);

                synchronized (this){
                    notify();
                }
            }
        };

        new FirebaseAction().GetAllDocuments("visiteur",callbackTest);

        synchronized (callbackTest) {
            try {
                callbackTest.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
