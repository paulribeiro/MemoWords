package com.paulribe.memowords;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.common.firebase.FirebaseDataHelper;

public class SplashViewModel extends BaseViewModel {

    @Override
    public void init() {
        if(getFirebaseDataHelper() == null) {
            setFirebaseDataHelper(new FirebaseDataHelper());
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            setCurrentUser(currentUser);
            getFirebaseDataHelper().setReferenceUserConfig(getCurrentUser());
        }
    }
}
