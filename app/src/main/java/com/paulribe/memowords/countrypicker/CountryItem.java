package com.paulribe.memowords.countrypicker;

import com.paulribe.memowords.enumeration.LanguageEnum;

public class CountryItem {

    private LanguageEnum mCountryName;
    private int mFlagImage;

    public CountryItem(LanguageEnum countryName, int flagImage) {
        mCountryName = countryName;
        mFlagImage = flagImage;
    }

    public LanguageEnum getCountryName() {
        return mCountryName;
    }

    public int getFlagImage() {
        return mFlagImage;
    }
}
