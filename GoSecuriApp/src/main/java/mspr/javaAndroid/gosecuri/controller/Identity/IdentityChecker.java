package mspr.javaAndroid.gosecuri.controller.Identity;


import android.os.AsyncTask;

import java.util.List;

import mspr.javaAndroid.gosecuri.controller.Detection.IDetectionCallback;
import mspr.javaAndroid.gosecuri.controller.Detection.faceRecognition.FaceComparison;
import mspr.javaAndroid.gosecuri.model.Visitor;


public class IdentityChecker extends AsyncTask<IdentityCheckerCallback,Void,Void> {

    private Visitor _toCheckVisitor;

    private IdentityCheckerCallback callback;

    public IdentityChecker(Visitor toCheck){
        this._toCheckVisitor = toCheck;

    }

    @Override
    protected Void doInBackground(final IdentityCheckerCallback... callbacks) {
        callback = callbacks[0];
        final Thread getVisitorThread;


        try {
            getVisitorThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    final Thread[] compareThread = new Thread[1];
                    //récupère tous les visiteurs connus
                    new Visitor().GetAllVisitors(new IdentityCheckerCallback() {

                        @Override
                        public void onListVisitorFound(final List<Visitor> output) {

                            compareThread[0] = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        final int[] index = {0};
                                        for (final Visitor visitor : output) {
                                            new FaceComparison(_toCheckVisitor.getImages(), visitor.getImages()).Compare(new IDetectionCallback() {
                                                @Override
                                                public void onFaceCompared(boolean output) {
                                                    if (output) {
                                                        callback.onVisitorFound(visitor);
                                                        Thread.currentThread().interrupt();
                                                    } else {
                                                        index[0]++;
                                                    }
                                                }

                                                @Override
                                                public void onProcessFail() {
                                                    callback.onVisitorNotFound();
                                                    Thread.currentThread().interrupt();
                                                }
                                            });
                                            if (index[0] == output.size()) {
                                                callback.onVisitorNotFound();
                                                Thread.currentThread().interrupt();
                                            }
                                        }
                                    } catch (
                                            Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            if(!compareThread[0].isAlive()){
                                compareThread[0].start();
                            }
                        }

                        @Override
                        public void onListVisitorNotFound() {
                            callback.onListVisitorNotFound();
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            });
            if(!getVisitorThread.isAlive()){
                getVisitorThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
