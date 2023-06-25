package com.paulribe.memowords.authentication.passwordforgotten;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.BaseViewModel;

public class PasswordForgottenViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPasswordResetSuccessful = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sendResetPasswordEmail(String email) {
        isLoading.setValue(Boolean.TRUE);
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(Boolean.FALSE);
                    if (task.isSuccessful()) {
                        isPasswordResetSuccessful.setValue(Boolean.TRUE);
                    } else {
                        isPasswordResetSuccessful.setValue(Boolean.FALSE);
                    }
                });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsPasswordResetSuccessful() {
        return isPasswordResetSuccessful;
    }

}
