package com.paulribe.memowords.viewmodels;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.enumeration.KnowledgeLevelEnum;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.enumeration.SectionRowEnum;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mymemory.Match;
import com.paulribe.memowords.model.mymemory.MyMemoryResult;
import com.paulribe.memowords.model.pons.PonsResult;
import com.paulribe.memowords.model.pons.SearchWordResult;
import com.paulribe.memowords.model.pons.SearchWordResultList;
import com.paulribe.memowords.model.pons.Translation;
import com.paulribe.memowords.model.pons.WordMeaning;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import lombok.Getter;

public class ListWordsViewModel extends BaseViewModel {

    @Getter
    private MutableLiveData<List<Word>> words;

    @Getter
    private List<Word> wordsToDisplay;

    @Getter
    private MutableLiveData<OrderByEnum> orderByEnum;

    @Getter
    private MutableLiveData<Boolean> isFavoriteSelected;

    @Getter
    private MutableLiveData<String> searchedString;

    @Getter
    private MutableLiveData<List<TranslatedWord>> translatedWordResults;

    @Getter
    private Boolean isRecyclerViewOnTranslateResults = Boolean.FALSE;

    @Getter
    private MutableLiveData<LanguageEnum> currentSourceLanguage;

    @Getter
    private MutableLiveData<LanguageEnum> currentTargetLanguage;

    @Getter
    private MutableLiveData<Boolean> isNativeLanguageToTranslation;

    @Getter
    private Word lastWordDeleted;

    @Getter
    private MutableLiveData<HashSet<KnowledgeLevelEnum>> knowledgeFilterSelected;

    @Getter
    private List<String> possiblePonsTranslations;

    public void setRecyclerViewOnTranslateResults(Boolean recyclerViewOnTranslateResults) {
        isRecyclerViewOnTranslateResults = recyclerViewOnTranslateResults;
    }

    public void setTranslatedWordResults(List<TranslatedWord> translatedWordResults) {
        this.translatedWordResults.setValue(translatedWordResults);
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
        knowledgeFilterSelected = new MutableLiveData<>(new HashSet<>());

        possiblePonsTranslations = Arrays.asList("enfr", "deen", "enes", "enpt", "defr", "esfr", "dept", "dees", "espt");
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
            setTranslatedWordResults(translatedWords);
        } else {
            setTranslatedWordResults(new ArrayList<>());
        }
    }

    public void buildTranslationForMyMemory(MyMemoryResult myMemoryResult) {
        Integer sectionNumber = 1;
        Integer subSectionNumber = 1;
        List<TranslatedWord> translatedWords = new ArrayList<>();
        if(myMemoryResult != null) {
            if(myMemoryResult.getMatches() != null) {
                for(Match match : myMemoryResult.getMatches()) {
                     translatedWords.add(createTranslatedWordForRow(new Translation(match.getSegment(), match.getTranslation()), sectionNumber, subSectionNumber));
                }
            }
            setTranslatedWordResults(translatedWords);
        } else {
            setTranslatedWordResults(new ArrayList<>());
        }
    }

    public List<Word> getFilteredWordsByKnowledgeLevel(){
        List<Word> allWords = getWords().getValue();
        if(allWords != null) {
            if(!CollectionUtils.isEmpty(getKnowledgeFilterSelected().getValue())) {
                return allWords.stream().filter(w -> getKnowledgeFilterSelected().getValue().contains(convertToKnowledgeLevelEnum(w.getKnowledgeLevel()))).collect(Collectors.toList());
            } else {
                return allWords;
            }
        }
        return new ArrayList<>();
    }

    public KnowledgeLevelEnum convertToKnowledgeLevelEnum (Integer knowledgeLevel) {
        switch(knowledgeLevel) {
            case 0:
                return KnowledgeLevelEnum.NEW;
            case 1:
            case 2:
                return KnowledgeLevelEnum.SHORT_TERM_MEMORY;
            case 3:
            case 4:
                return KnowledgeLevelEnum.MID_TERM_MEMORY;
            default:
                return KnowledgeLevelEnum.LONG_TERM_MEMORY;
        }
    }

    public List<Word> filterWordsToDisplay() {
        List<Word> words = getFilteredWordsByKnowledgeLevel();
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
                words = words.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordNative())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            } else {
                words = words.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordTranslated())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            }

        }
        switch(orderByEnum) {
            case AZ:
                words.sort((word, word2) -> updateStringWithIgnoredCharacter(word.getWordNative())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordNative())));
                break;
            case ZA:
                words.sort((word, word2) -> updateStringWithIgnoredCharacter(word.getWordNative())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordNative())));
                Collections.reverse(words);
                break;
            case LAST_TRY:
                words.sort(Comparator.comparing(Word::getLastTry).reversed());
                break;
            case KNOWLEDGE_LEVEL:
                words.sort(Comparator.comparing(Word::getKnowledgeLevel));
                break;
            case KNOWLEDGE_LEVEL_DESC:
                words.sort(Comparator.comparing(Word::getKnowledgeLevel).reversed());
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
        if(!searchWordResult.getHeadwordfull().isEmpty()) {
            translatedWord = new TranslatedWord(SectionRowEnum.SECTION, html2text(searchWordResult.getHeadwordfull()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        } else {
            translatedWord = new TranslatedWord(SectionRowEnum.SECTION, html2text(searchWordResult.getHeadword()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        }
        return translatedWord;
    }

    public static String html2text(String html) {
        return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ").trim();
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
        if(getIsNativeLanguageToTranslation().getValue()) {
            return new Word(translatedWord.getSourceWord(), translatedWord.getTargetWord(),
                    new Date().getTime(), NO_LAST_SUCCESS, NO_LAST_TRY,
                    0, 0, "", 0, false);
        } else {
            return new Word(translatedWord.getTargetWord(), translatedWord.getSourceWord(),
                    new Date().getTime(), NO_LAST_SUCCESS, NO_LAST_TRY,
                    0, 0, "", 0, false);
        }
    }

    public void deleteWord(int pos) {
        lastWordDeleted = wordsToDisplay.get(pos);
        getFirebaseDataHelper().deleteWord(lastWordDeleted);
    }

    public void restoreLastWordDeleted(){
        if(lastWordDeleted != null) {
            getFirebaseDataHelper().updateWord(lastWordDeleted);
        }
    }

    private String updateStringWithIgnoredCharacter(String string) {
        return string.replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .toLowerCase();
    }

    public void addKnowledgeLevelFilter (KnowledgeLevelEnum knowledgeLevelEnum) {
        HashSet<KnowledgeLevelEnum> knowledgeFilterSelectedValue = getKnowledgeFilterSelected().getValue();
        if(knowledgeFilterSelectedValue != null) {
            knowledgeFilterSelectedValue.add(knowledgeLevelEnum);
            knowledgeFilterSelected.setValue(knowledgeFilterSelectedValue);
        }
    }

    public void deleteKnowledgeLevelFilter (KnowledgeLevelEnum knowledgeLevelEnum) {
        HashSet<KnowledgeLevelEnum> knowledgeFilterSelectedValue = getKnowledgeFilterSelected().getValue();
        if(knowledgeFilterSelectedValue != null) {
            knowledgeFilterSelectedValue.remove(knowledgeLevelEnum);
            knowledgeFilterSelected.setValue(knowledgeFilterSelectedValue);
        }
    }
}
