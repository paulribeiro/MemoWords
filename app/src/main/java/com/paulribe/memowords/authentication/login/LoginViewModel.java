package com.paulribe.memowords.authentication.login;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.BaseViewModel;

public class LoginViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> isLoginSuccessful = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;

    @Override
    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        isLoading = new MutableLiveData<>(Boolean.FALSE);
    }

    public void loginByEmailAndPassword(String email, String password) {
        isLoading.setValue(Boolean.TRUE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(Boolean.FALSE);
                    if(task.isSuccessful()){
                        setCurrentUser(task.getResult().getUser());
                        getFirebaseDataHelper().setReferenceUserConfig(getCurrentUser());
                        isLoginSuccessful.setValue(Boolean.TRUE);
                    } else {
                        isLoginSuccessful.setValue(Boolean.FALSE);
                    }
                });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsLoginSuccessful() {
        return isLoginSuccessful;
    }
}
