package mspr.javaAndroid.gosecuri.model;

import android.graphics.Bitmap;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mspr.javaAndroid.gosecuri.controller.Identity.IdentityCheckerCallback;

import static org.junit.Assert.*;

public class VisitorTest {

    @Test
    public void getAllVisitor() {
        IdentityCheckerCallback callbackTest = new IdentityCheckerCallback() {
            @Override
            public void onListVisitorFound(List<Visitor> output) {
                Assert.assertTrue(true);
                synchronized (this){
                    notify();
                }
            }

            @Override
            public void onListVisitorNotFound() {
                Assert.assertTrue(false);

                synchronized (this){
                    notify();
                }
            }
        };

        Visitor test =  new Visitor("Test_nom","Test_prenom","123456789", Calendar.getInstance().getTime(),MakeFakeImage());
        test.GetAllVisitor(callbackTest );

        synchronized (callbackTest) {
            try {
                callbackTest.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void Test_SaveVisitor()
    {
        IdentityCheckerCallback callbackTest = new IdentityCheckerCallback() {

            @Override
            public void onSuccess(boolean output) {
                Assert.assertTrue(output);
                synchronized (this){
                    notify();
                }
            }
        };

        Visitor test =  new Visitor("Test_nom","Test_prenom","123456789", Calendar.getInstance().getTime(),MakeFakeImage());
        test.SaveVisitor(callbackTest );

        synchronized (callbackTest) {
            try {
                callbackTest.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //genere des bitmap blanc pour test l'insertion de fichier
    private List<byte[]> MakeFakeImage(){
        int w = 360, h = 400;
        List<byte[]> listOfByte = new ArrayList<>();
        for(int i = 0; i<2;i++){
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            listOfByte.add(stream.toByteArray());

            bmp.recycle();
        }

        return listOfByte;
    }
}