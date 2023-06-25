package com.paulribe.memowords;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.common.enumeration.LanguageEnum;
import com.paulribe.memowords.common.firebase.FirebaseDataHelper;

public class BaseViewModel extends ViewModel {

    private static LanguageEnum nativeLanguage;
    private static MutableLiveData<LanguageEnum> currentLanguage = new MutableLiveData<>();
    private static String xApiKeyPons;
    private static FirebaseUser currentUser;
    private static MutableLiveData<Boolean> normalState;
    protected static FirebaseDataHelper firebaseDataHelper;

    public void init() {
        if(firebaseDataHelper != null) {
            firebaseDataHelper.setReferenceUserConfig(getCurrentUser());
            normalState = new MutableLiveData<>(Boolean.TRUE);
        } else {
            normalState = new MutableLiveData<>(Boolean.FALSE);
        }
    }

    public void loadUserConfig() {
        firebaseDataHelper.loadUserConfig(userConfig -> {
            currentLanguage.setValue(LanguageEnum.valueOf(userConfig.getCurrentLanguage()));
            nativeLanguage = LanguageEnum.valueOf(userConfig.getNativeLanguage());
            xApiKeyPons = userConfig.getXApiKeyPons();
        });
    }

    public void updateLanguage(LanguageEnum language) {
        firebaseDataHelper.setReferenceWords(language, getCurrentUser());
        firebaseDataHelper.updateCurrentLanguage(language);
    }

    public FirebaseDataHelper getFirebaseDataHelper() {
        return firebaseDataHelper;
    }

    public void setFirebaseDataHelper(FirebaseDataHelper firebaseDataHelper) {
        BaseViewModel.firebaseDataHelper = firebaseDataHelper;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        BaseViewModel.currentUser = currentUser;
    }

    public MutableLiveData<LanguageEnum> getCurrentLanguage() {
        return currentLanguage;
    }

    public String getxApiKeyPons() {
        return xApiKeyPons;
    }

    public LanguageEnum getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(LanguageEnum nativeLanguage) {
        BaseViewModel.nativeLanguage = nativeLanguage;
    }

    public static MutableLiveData<Boolean> getNormalState() {
        return normalState;
    }
}
