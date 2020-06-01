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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;

public class ListWordsViewModel extends BaseViewModel {

    private MutableLiveData<List<Word>> words;
    private List<Word> wordsToDisplay;
    private MutableLiveData<OrderByEnum> orderByEnum;
    private MutableLiveData<Boolean> isFavoriteSelected;
    private MutableLiveData<String> searchedString;
    private MutableLiveData<List<TranslatedWord>> translatedWordResults;
    private Boolean isRecyclerViewOnTranslateResults = Boolean.FALSE;
    private MutableLiveData<LanguageEnum> currentSourceLanguage;
    private MutableLiveData<LanguageEnum> currentTargetLanguage;
    private MutableLiveData<Boolean> isNativeLanguageToTranslation;


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

    public MutableLiveData<Boolean> getIsNativeLanguageToTranslation() {
        return isNativeLanguageToTranslation;
    }

    public List<Word> getWordsToDisplay() {
        return wordsToDisplay;
    }

    public void setWordsToDisplay(List<Word> wordsToDisplay) {
        this.wordsToDisplay = wordsToDisplay;
    }

    public void init() {
        words = new MutableLiveData<>(new ArrayList<>());
        orderByEnum = new MutableLiveData<>(OrderByEnum.LAST_TRY);
        isFavoriteSelected = new MutableLiveData<>(Boolean.FALSE);
        searchedString = new MutableLiveData<>("");
        translatedWordResults = new MutableLiveData<>(new ArrayList<>());
        currentSourceLanguage = new MutableLiveData<>(this.getNativeLanguage());
        currentTargetLanguage = new MutableLiveData<>(this.getCurrentLanguage().getValue());
        isNativeLanguageToTranslation = new MutableLiveData<>(Boolean.TRUE);
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
            if(ponsResult.getHits() != null) {
                for(SearchWordResultList searchWordResultList : ponsResult.getHits()) {
                    if(searchWordResultList.getRoms() != null) {
                        for(SearchWordResult searchWordResult : searchWordResultList.getRoms()) {
                            translatedWords.add(createTranslatedWordForSection(searchWordResult, sectionNumber));
                            if(searchWordResult.getArabs() != null) {
                                for(WordMeaning wordMeaning : searchWordResult.getArabs()) {
                                    translatedWords.add(createTranslatedWordForSubSection(wordMeaning, sectionNumber, subSectionNumber));
                                    if(wordMeaning.getTranslations() != null) {
                                        for(Translation translation : wordMeaning.getTranslations()) {
                                            translatedWords.add(createTranslatedWordForRow(translation, sectionNumber, subSectionNumber));
                                        }
                                    }
                                    subSectionNumber++;
                                }
                            }
                            sectionNumber ++;
                        }
                    }
                }
            }
            translatedWordResults.setValue(translatedWords);
        } else {
            translatedWordResults.setValue(new ArrayList<>());
        }
    }

    public List<Word> filterWordsToDisplay() {
        List<Word> words = getWords().getValue();
        String searchWord = getSearchedString().getValue();
        Boolean isFavorite = getIsFavoriteSelected().getValue();
        OrderByEnum orderByEnum = getOrderByEnum().getValue();
        if(isFavorite != null && isFavorite) {
            words = words.stream().filter(Word::isFavorite).collect(Collectors.toList());
        }
        if(searchWord != null && !searchWord.isEmpty()) {
            searchWord = updateStringWithIgnoredCharacter(searchWord);
            String finalSearchWord = searchWord;
            if(getIsNativeLanguageToTranslation().getValue()) {
                words = words.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordFR())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            } else {
                words = words.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordDE())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            }

        }
        switch(orderByEnum) {
            case AZ:
                Collections.sort(words, (word, word2) -> updateStringWithIgnoredCharacter(word.getWordFR())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordFR())));
                break;
            case ZA:
                Collections.sort(words, (word, word2) -> updateStringWithIgnoredCharacter(word.getWordFR())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordFR())));
                Collections.reverse(words);
                break;
            case LAST_TRY:
                Collections.sort(words, Comparator.comparing(Word::getLastTry).reversed());
                break;
            case KNOWLEDGE_LEVEL:
                Collections.sort(words, Comparator.comparing(Word::getKnowledgeLevel));
                break;
            case KNOWLEDGE_LEVEL_DESC:
                Collections.sort(words, Comparator.comparing(Word::getKnowledgeLevel).reversed());
                break;
        }
        wordsToDisplay = words;
        return words;
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

    public Word getNewTranslatedWord(TranslatedWord translatedWord) {
        Optional<TranslatedWord> subsectionForTranslatedWord = getTranslatedWordResults().getValue().stream()
                .filter(w -> w.getSectionRowtype().equals(SectionRowEnum.SUBSECTION) && w.getSubSectionNumber().equals(w.getSubSectionNumber()))
                .findFirst();
        String contextNewWord = "";
        if(subsectionForTranslatedWord.isPresent()) {
            contextNewWord = subsectionForTranslatedWord.get().getSourceWord();
        }
        Word word;
        if(isNativeLanguageToTranslation.getValue()) {
            word = new Word(translatedWord.getSourceWord(), translatedWord.getTargetWord(),
                    new Date().getTime(), NO_LAST_SUCCESS, NO_LAST_TRY,
                    0, 0, contextNewWord, 0, false);
        } else {
            word = new Word(translatedWord.getTargetWord(), translatedWord.getSourceWord(),
                    new Date().getTime(), NO_LAST_SUCCESS, NO_LAST_TRY,
                    0, 0, contextNewWord, 0, false);
        }
        return word;
    }

    public void deleteWord(int pos) {
        getFirebaseDataHelper().deleteWord(wordsToDisplay.get(pos));
    }

    private String updateStringWithIgnoredCharacter(String string) {
        return string.replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .toLowerCase();
    }
}
