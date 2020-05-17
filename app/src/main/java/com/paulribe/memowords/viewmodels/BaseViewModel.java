package com.paulribe.memowords.viewmodels;

import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    private static MutableLiveData<LanguageEnum> currentLanguage;
    private static FirebaseUser currentUser;
    static FirebaseDataHelper firebaseDataHelper;


    public void init() {
        firebaseDataHelper.setReferenceUserConfig();
        currentLanguage = new MutableLiveData<>();
    }

    public void loadUserConfig() {
        firebaseDataHelper.loadUserConfig(userConfig -> {
            currentLanguage.setValue(LanguageEnum.valueOf(userConfig.getCurrentLanguage()));
        });
    }

    public void updateLanguage(LanguageEnum language) {
        FirebaseDataHelper firebaseDataHelper = getFirebaseDataHelper();
        firebaseDataHelper.setReferenceWords(language);
        firebaseDataHelper.updateCurrentLanguage(language);
    }

    public static FirebaseDataHelper getFirebaseDataHelper() {
        return firebaseDataHelper;
    }

    public static void setFirebaseDataHelper(FirebaseDataHelper firebaseDataHelper) {
        BaseViewModel.firebaseDataHelper = firebaseDataHelper;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        BaseViewModel.currentUser = currentUser;
    }

    public MutableLiveData<LanguageEnum> getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(MutableLiveData<LanguageEnum> currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
}
