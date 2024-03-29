package com.paulribe.memowords.list;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.BaseViewModel;
import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;
import com.paulribe.memowords.common.enumeration.LanguageEnum;
import com.paulribe.memowords.common.enumeration.OrderByEnum;
import com.paulribe.memowords.common.enumeration.SectionRowEnum;
import com.paulribe.memowords.common.firebase.FirebaseDataHelper;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.model.mymemory.Match;
import com.paulribe.memowords.common.model.mymemory.MyMemoryResult;
import com.paulribe.memowords.common.model.pons.PonsResult;
import com.paulribe.memowords.common.model.pons.SearchWordResult;
import com.paulribe.memowords.common.model.pons.SearchWordResultList;
import com.paulribe.memowords.common.model.pons.Translation;
import com.paulribe.memowords.common.model.pons.WordMeaning;
import com.paulribe.memowords.common.restclient.HtmlHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @Override
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
            public void dataIsUpdated(List<Word> w) {
                words.setValue(w);
            }
        });
    }

    public void buildTranslationForPons(List<PonsResult> ponsResults) {
        if(!CollectionUtils.isEmpty(ponsResults)) {
            List<TranslatedWord> translatedWords = convertPonsResultToTranslatedWords(ponsResults.get(0));
            setTranslatedWordResults(translatedWords);
        } else {
            setTranslatedWordResults(new ArrayList<>());
        }
    }

    private List<TranslatedWord> convertPonsResultToTranslatedWords(PonsResult ponsResult) {
        List<TranslatedWord> translatedWords = new ArrayList<>();
        if(ponsResult.getHits() != null) {
            AtomicInteger sectionNumber = new AtomicInteger(1);
            ponsResult.getHits().stream()
                    .map(SearchWordResultList::getRoms)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .forEach(searchWordResult ->
                            createTranslatedWordForSectionAndChildren(sectionNumber.getAndIncrement(),
                                    translatedWords, searchWordResult));
        }
        return translatedWords;
    }

    private void createTranslatedWordForSectionAndChildren(Integer sectionNumber, List<TranslatedWord> translatedWords, SearchWordResult searchWordResult) {
        TranslatedWord translatedWordForSection = createTranslatedWordForSection(searchWordResult, sectionNumber);
        if(translatedWordForSection != null) {
            translatedWords.add(translatedWordForSection);
        }
        if(searchWordResult.getArabs() != null) {
            AtomicInteger subSectionNumber = new AtomicInteger(1);
            searchWordResult.getArabs().stream()
                    .filter(Objects::nonNull)
                    .forEach(wordMeaning -> createTranslatedWordForSubSectionAndChildren(sectionNumber, translatedWords, subSectionNumber.getAndIncrement(), wordMeaning));
        }
    }

    private TranslatedWord createTranslatedWordForSection(SearchWordResult searchWordResult, Integer sectionNumber) {
        if(!searchWordResult.getHeadwordfull().isEmpty()) {
            return new TranslatedWord(SectionRowEnum.SECTION, HtmlHelper.html2text(searchWordResult.getHeadwordfull()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        } else if(searchWordResult.getHeadword() != null && !searchWordResult.getHeadword().isEmpty()){
            return new TranslatedWord(SectionRowEnum.SECTION, HtmlHelper.html2text(searchWordResult.getHeadword()),
                    searchWordResult.getWordclass(), sectionNumber, Boolean.FALSE);
        } else {
            return null;
        }
    }

    private void createTranslatedWordForSubSectionAndChildren(Integer sectionNumber, List<TranslatedWord> translatedWords, int subSectionNumber, WordMeaning wordMeaning) {
        TranslatedWord translatedWordForSubSection = createTranslatedWordForSubSection(wordMeaning, sectionNumber, subSectionNumber);
        if(translatedWordForSubSection != null) {
            translatedWords.add(translatedWordForSubSection);
        }
        if(wordMeaning.getTranslations() != null) {
            wordMeaning.getTranslations().forEach(
                translation -> translatedWords.add(
                        createTranslatedWordForRow(translation, sectionNumber, subSectionNumber)));
        }
    }

    private TranslatedWord createTranslatedWordForSubSection(WordMeaning wordMeaning, Integer sectionNumber, Integer subSectionNumber) {
        if(wordMeaning.getHeader() != null && !wordMeaning.getHeader().isEmpty()) {
            return new TranslatedWord(SectionRowEnum.SUBSECTION, HtmlHelper.html2text(wordMeaning.getHeader()),
                    sectionNumber, subSectionNumber, Boolean.FALSE);
        } else {
            return null;
        }
    }

    private TranslatedWord createTranslatedWordForRow(Translation translation, Integer sectionNumber, Integer subSectionNumber) {
        return new TranslatedWord(SectionRowEnum.ROW, HtmlHelper.html2text(translation.getSource()), HtmlHelper.html2text(translation.getTarget()),
                sectionNumber, subSectionNumber, Boolean.FALSE);
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

    public List<Word> getFilteredWordsByKnowledgeLevel(Set<KnowledgeLevelEnum> knowledgeLevelSelected){
        List<Word> allWords = getWords().getValue();
        if(allWords != null) {
            if(!CollectionUtils.isEmpty(knowledgeLevelSelected)) {
                return allWords.stream().filter(w -> knowledgeLevelSelected.contains(convertToKnowledgeLevelEnum(w.getKnowledgeLevel()))).collect(Collectors.toList());
            } else {
                return allWords;
            }
        }
        return new ArrayList<>();
    }

    public long getFilteredWordsCountByKnowledgeLevel(List<KnowledgeLevelEnum> knowledgeLevelSelected){
        List<Word> wordsForKnowledgeLevel = getFilteredWordsByKnowledgeLevel(new HashSet<>(knowledgeLevelSelected));
        return wordsForKnowledgeLevel.size();
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
        List<Word> wordsToFilter = getFilteredWordsByKnowledgeLevel(getKnowledgeFilterSelected().getValue());
        String searchWord = getSearchedString().getValue();
        Boolean isFavorite = getIsFavoriteSelected().getValue();
        if(isFavorite != null && isFavorite) {
            wordsToFilter = wordsToFilter.stream().filter(Word::isFavorite).collect(Collectors.toList());
        }
        if(searchWord != null && !searchWord.isEmpty()) {
            searchWord = updateStringWithIgnoredCharacter(searchWord);
            String finalSearchWord = searchWord;
            if(getIsNativeLanguageToTranslation().getValue()) {
                wordsToFilter = wordsToFilter.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordNative())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            } else {
                wordsToFilter = wordsToFilter.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordTranslated())
                        .contains(finalSearchWord))
                        .collect(Collectors.toList());
            }

        }
        switch(getOrderByEnum().getValue()) {
            case AZ:
                wordsToFilter.sort((word, word2) -> updateStringWithIgnoredCharacter(word.getWordNative())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordNative())));
                break;
            case ZA:
                wordsToFilter.sort((word, word2) -> updateStringWithIgnoredCharacter(word.getWordNative())
                        .compareTo(updateStringWithIgnoredCharacter(word2.getWordNative())));
                Collections.reverse(wordsToFilter);
                break;
            case LAST_TRY:
                wordsToFilter.sort(Comparator.comparing(Word::getLastTry,
                        Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed());
                break;
            case KNOWLEDGE_LEVEL:
                wordsToFilter.sort(Comparator.comparing(Word::getKnowledgeLevel));
                break;
            case KNOWLEDGE_LEVEL_DESC:
                wordsToFilter.sort(Comparator.comparing(Word::getKnowledgeLevel).reversed());
                break;
        }
        wordsToDisplay = wordsToFilter;
        return wordsToFilter;
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
                    new Date().getTime(), "");
        } else {
            return new Word(translatedWord.getTargetWord(), translatedWord.getSourceWord(),
                    new Date().getTime(), "");
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
