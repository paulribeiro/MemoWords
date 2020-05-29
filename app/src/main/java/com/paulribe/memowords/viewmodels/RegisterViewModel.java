package com.paulribe.memowords.viewmodels;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.R;
import com.paulribe.memowords.countrypicker.CountryItem;
import com.paulribe.memowords.enumeration.LanguageEnum;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class RegisterViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRegisterSuccessful = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;
    private LanguageEnum nativeLanguage;
    private Exception taskRegistrationResultException;
    private ArrayList<CountryItem> mCountryList;


    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        initPossibleLanguages();
    }

    public void registerUser(String email, String password) {
        isLoading.setValue(Boolean.TRUE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        isLoading.setValue(Boolean.FALSE);
                        if(task.isSuccessful()){
                            setCurrentUser(task.getResult().getUser());
                            getFirebaseDataHelper().setReferenceUserConfig(getCurrentUser());
                            getFirebaseDataHelper().addUser(nativeLanguage);
                            isRegisterSuccessful.setValue(Boolean.TRUE);
                        } else {
                            taskRegistrationResultException = task.getException();
                            isRegisterSuccessful.setValue(Boolean.FALSE);
                        }
                    }
                });
    }

    private void initPossibleLanguages() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem(LanguageEnum.NONE, 0));
        mCountryList.add(new CountryItem(LanguageEnum.GERMAN, R.mipmap.de_flag));
        mCountryList.add(new CountryItem(LanguageEnum.SPANISH, R.mipmap.es_flag));
        mCountryList.add(new CountryItem(LanguageEnum.FRENCH, R.mipmap.fr_flag));
        mCountryList.add(new CountryItem(LanguageEnum.ENGLISH, R.mipmap.gb_flag));
        mCountryList.add(new CountryItem(LanguageEnum.PORTUGUESE, R.mipmap.pt_flag));
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsRegisterSuccessful() {
        return isRegisterSuccessful;
    }

    public Exception getTaskRegistrationResultException() {
        return taskRegistrationResultException;
    }

    public LanguageEnum getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(LanguageEnum nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public ArrayList<CountryItem> getmCountryList() {
        return mCountryList;
    }
}