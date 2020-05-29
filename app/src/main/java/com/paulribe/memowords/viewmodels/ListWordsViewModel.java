package com.paulribe.memowords.viewmodels;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.enumeration.SectionRowEnum;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.pons.PonsResult;
import com.paulribe.memowords.model.pons.SearchWordResult;
import com.paulribe.memowords.model.pons.SearchWordResultList;
import com.paulribe.memowords.model.pons.Translation;
import com.paulribe.memowords.model.pons.WordMeaning;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.MutableLiveData;

public class ListWordsViewModel extends BaseViewModel {

    private MutableLiveData<List<Word>> words;
    private MutableLiveData<OrderByEnum> orderByEnum;
    private MutableLiveData<Boolean> isFavoriteSelected;
    private MutableLiveData<String> searchedString;
    private MutableLiveData<List<TranslatedWord>> translatedWordResults;
    private Boolean isRecyclerViewOnTranslateResults = Boolean.FALSE;
    private MutableLiveData<LanguageEnum> currentSourceLanguage;
    private MutableLiveData<LanguageEnum> currentTargetLanguage;

    public MutableLiveData<List<Word>> getWords() {
        return words;
    }

    public MutableLiveData<OrderByEnum> getOrderByEnum() {
        return orderByEnum;
    }

    public MutableLiveData<Boolean> getIsFavoriteSelected() {
        return isFavoriteSelected;
    }

    public MutableLiveData<String> getSearchedString() {
        return searchedString;
    }

    public MutableLiveData<List<TranslatedWord>> getTranslatedWordResults() { return translatedWordResults; }

    public MutableLiveData<LanguageEnum> getCurrentSourceLanguage() { return currentSourceLanguage; }

    public void setCurrentSourceLanguage(MutableLiveData<LanguageEnum> currentSourceLanguage) {
        this.currentSourceLanguage = currentSourceLanguage;
    }

    public MutableLiveData<LanguageEnum> getCurrentTargetLanguage() { return currentTargetLanguage; }

    public void setCurrentTargetLanguage(MutableLiveData<LanguageEnum> currentTargetLanguage) {
        this.currentTargetLanguage = currentTargetLanguage;
    }

    public void init() {
        words = new MutableLiveData<>(new ArrayList<>());
        orderByEnum = new MutableLiveData<>(OrderByEnum.LAST_TRY);
        isFavoriteSelected = new MutableLiveData<>(Boolean.FALSE);
        searchedString = new MutableLiveData<>("");
        translatedWordResults = new MutableLiveData<>(new ArrayList<>());
        currentSourceLanguage = new MutableLiveData<>(this.getNativeLanguage());
        currentTargetLanguage = new MutableLiveData<>(this.getCurrentLanguage().getValue());
    }

    public void readWords() {

        firebaseDataHelper.setReferenceWords(getCurrentLanguage().getValue(), getCurrentUser());
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words.setValue(w);
            }

            @Override
            public void dataIsInserted() {}

            @Override
            public void dataIsUpdated(List<Word> w) {
                words.setValue(w);
            }

            @Override
            public void dataIsDeleted() {}
        });
    }

    public void buildTranslation(List<PonsResult> ponsResults) {
        Integer sectionNumber = 1;
        Integer subSectionNumber = 1;
        List<TranslatedWord> translatedWords = new ArrayList<>();
            if(!CollectionUtils.isEmpty(ponsResults)) {
            PonsResult ponsResult = ponsResults.get(0);
            for(SearchWordResultList searchWordResultList : ponsResult.getHits()) {
                for(SearchWordResult searchWordResult : searchWordResultList.getRoms()) {
                    translatedWords.add(createTranslatedWordForSection(searchWordResult, sectionNumber));
                    for(WordMeaning wordMeaning : searchWordResult.getArabs()) {
                        translatedWords.add(createTranslatedWordForSubSection(wordMeaning, sectionNumber, subSectionNumber));
                        for(Translation translation : wordMeaning.getTranslations()) {
                            translatedWords.add(createTranslatedWordForRow(translation, sectionNumber, subSectionNumber));
                        }
                        subSectionNumber++;
                    }
                    sectionNumber ++;
                }
            }
            translatedWordResults.setValue(translatedWords);
        } else {
            translatedWordResults.setValue(new ArrayList<>());
        }
    }

    private TranslatedWord createTranslatedWordForRow(Translation translation, Integer sectionNumber, Integer subSectionNumber) {
        return new TranslatedWord(SectionRowEnum.ROW, html2text(translation.getSource()), html2text(translation.getTarget()),
                sectionNumber, subSectionNumber, Boolean.FALSE);
    }

    private TranslatedWord createTranslatedWordForSubSection(WordMeaning wordMeaning, Integer sectionNumber, Integer subSectionNumber) {
        return new TranslatedWord(SectionRowEnum.SUBSECTION, html2text(wordMeaning.getHeader()),
                                            sectionNumber, subSectionNumber, Boolean.FALSE);
    }

    private TranslatedWord createTranslatedWordForSection(SearchWordResult searchWordResult, Integer sectionNumber) {
        TranslatedWord translatedWord;
        if(searchWordResult.getHeadwordfull() != null) {
            translatedWord = new TranslatedWord(SectionRowEnum.SECTION, html2text(searchWordResult.getHeadwordfull()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        } else {
            translatedWord = new TranslatedWord(SectionRowEnum.SECTION, html2text(searchWordResult.getHeadword()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        }
        return translatedWord;
    }

    public Boolean getRecyclerViewOnTranslateResults() {
        return isRecyclerViewOnTranslateResults;
    }

    public void setRecyclerViewOnTranslateResults(Boolean recyclerViewOnTranslateResults) {
        isRecyclerViewOnTranslateResults = recyclerViewOnTranslateResults;
    }

    public static String html2text(String html) {
        return android.text.Html.fromHtml(html).toString();
    }

    public void exchangeSourceTargetLanguage() {
        LanguageEnum temp;
        temp = currentSourceLanguage.getValue();
        currentSourceLanguage.setValue(currentTargetLanguage.getValue());
        currentTargetLanguage.setValue(temp);
    }

    public String getTranslationLanguagesPrefix() {
        if(getCurrentSourceLanguage().getValue().getPrefixForPons().compareTo(getCurrentTargetLanguage().getValue().getPrefixForPons()) < 0) {
            return getCurrentSourceLanguage().getValue().getPrefixForPons() + getCurrentTargetLanguage().getValue().getPrefixForPons();
        } else {
            return getCurrentTargetLanguage().getValue().getPrefixForPons() + getCurrentSourceLanguage().getValue().getPrefixForPons();
        }
    }
}
