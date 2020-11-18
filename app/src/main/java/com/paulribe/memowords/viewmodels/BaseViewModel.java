package com.paulribe.memowords.viewmodels;

import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    public final long NO_LAST_SUCCESS = 946684800;
    public final long NO_LAST_TRY = 915148800;
    private static LanguageEnum nativeLanguage;
    private static MutableLiveData<LanguageEnum> currentLanguage;
    private static String xApiKeyPons;
    private static FirebaseUser currentUser;
    private static MutableLiveData<Boolean> normalState;
    static FirebaseDataHelper firebaseDataHelper;

    public void init() {
        if(firebaseDataHelper != null) {
            firebaseDataHelper.setReferenceUserConfig(getCurrentUser());
            currentLanguage = new MutableLiveData<>();
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
        FirebaseDataHelper firebaseDataHelper = getFirebaseDataHelper();
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
