package com.paulribe.memowords.authentication.register;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.BaseViewModel;
import com.paulribe.memowords.R;
import com.paulribe.memowords.common.countrypicker.CountryItem;
import com.paulribe.memowords.common.enumeration.LanguageEnum;

import java.util.ArrayList;
import java.util.List;

public class RegisterViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRegisterSuccessful = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;
    private Exception taskRegistrationResultException;
    private ArrayList<CountryItem> mCountryList;


    @Override
    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        initPossibleLanguages();
    }

    public void registerUser(String email, String password) {
        isLoading.setValue(Boolean.TRUE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    isLoading.setValue(Boolean.FALSE);
                    if(task.isSuccessful()){
                        setCurrentUser(task.getResult().getUser());
                        getFirebaseDataHelper().setReferenceUserConfig(getCurrentUser());
                        getFirebaseDataHelper().addUser(getNativeLanguage());
                        isRegisterSuccessful.setValue(Boolean.TRUE);
                    } else {
                        taskRegistrationResultException = task.getException();
                        isRegisterSuccessful.setValue(Boolean.FALSE);
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
        mCountryList.add(new CountryItem(LanguageEnum.BULGARIAN, R.mipmap.bg_flag));
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

    public List<CountryItem> getCountryList() {
        return mCountryList;
    }
}
