package mspr.javaAndroid.gosecuri.controller.Identity;

import java.util.List;

import mspr.javaAndroid.gosecuri.model.Visitor;

public interface IIDentityCheckerEventHandler {

    void onSuccess(boolean output);
    void onFailure(Exception output);
    void onListVisitorFound(List<Visitor> output);
    void onVisitorAdded();
    void onVisitorNotAdded();
    void onVisitorFound(Visitor output);
    void onVisitorNotFound();
    void onListVisitorNotFound();
}
