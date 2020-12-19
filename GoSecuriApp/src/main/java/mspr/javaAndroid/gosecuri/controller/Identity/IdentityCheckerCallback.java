package mspr.javaAndroid.gosecuri.controller.Identity;

import java.util.List;

import mspr.javaAndroid.gosecuri.model.Visitor;

public abstract class IdentityCheckerCallback implements IIDentityCheckerEventHandler {
    @Override
    public void onSuccess(boolean output) {

    }

    @Override
    public void onFailure(Exception output) {

    }

    @Override
    public void onListVisitorFound(List<Visitor> output) {

    }

    @Override
    public void onListVisitorNotFound() {

    }

    @Override
    public void onVisitorAdded() {

    }

    @Override
    public void onVisitorNotAdded() {
    }

    @Override
    public void onVisitorFound(Visitor output) {

    }

    @Override
    public void onVisitorNotFound() {

    }


}
