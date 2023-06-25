package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.common.enumeration.SectionRowEnum;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.model.pons.PonsResult;
import com.paulribe.memowords.common.model.pons.SearchWordResult;
import com.paulribe.memowords.common.model.pons.SearchWordResultList;
import com.paulribe.memowords.common.model.pons.Translation;
import com.paulribe.memowords.common.model.pons.WordMeaning;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.List;

public class TestData {

    public static long BASE_LAST_SUCCESS_TIMESTAMP = Timestamp.valueOf("2000-01-01 00:00:00.000000000").getTime();
    public static long BASE_LAST_TRY_TIMESTAMP = Timestamp.valueOf("1999-01-01 00:00:00.000000000").getTime();

    public static Word createWord(String wordName, Integer hoursSinceLastTry, Integer numberTry, Integer numberSuccess, Integer knowledgeLevel) {
        long dateAdded = Timestamp.valueOf("2020-01-01 00:00:00.000000000").getTime();

        LocalDateTime lastTry = LocalDateTime.now().minusHours(hoursSinceLastTry);
        long lastTryTimeStamp = lastTry.atZone(ZoneId.systemDefault()).getLong(ChronoField.INSTANT_SECONDS) * 1000;

        Word word = new Word(wordName, "DE1", dateAdded, "context");

        word.setNumberTry(numberTry);
        word.setNumberSuccess(numberSuccess);
        word.setKnowledgeLevel(knowledgeLevel);
        word.setFavorite(false);
        word.setLastSuccess(lastTryTimeStamp);
        word.setLastTry(lastTryTimeStamp);

        return word;
    }

    public static Word createWord(String wordName, Integer numberTry, Integer numberSuccess, Integer knowledgeLevel) {
        long dateAdded = Timestamp.valueOf("2020-01-01 00:00:00.000000000").getTime();
        Word word = new Word(wordName, "DE1", dateAdded, "context");
        word.setNumberTry(numberTry);
        word.setNumberSuccess(numberSuccess);
        word.setKnowledgeLevel(knowledgeLevel);
        word.setFavorite(false);
        word.setLastSuccess(BASE_LAST_SUCCESS_TIMESTAMP);
        word.setLastTry(BASE_LAST_TRY_TIMESTAMP);
        return word;
    }

    public static Word createWord(String wordName, String wordTranslated, long date,
                                  Integer numberTry, Integer numberSuccess, Integer knowledgeLevel, boolean isFavorite) {
        Word word = new Word(wordName, wordTranslated, date, "");
        word.setNumberTry(numberTry);
        word.setNumberSuccess(numberSuccess);
        word.setKnowledgeLevel(knowledgeLevel);
        word.setFavorite(isFavorite);
        word.setLastSuccess(date);
        word.setLastTry(date);
        return word;
    }

    public static PonsResult createPonsResult (List<SearchWordResultList> hits) {
        return new PonsResult("language", hits);
    }

    public static SearchWordResultList createSearchWordResultList(List<SearchWordResult> roms) {
        return new SearchWordResultList("entry", roms);
    }

    public static SearchWordResult createSearchWordResult(String headWord, String headWordFull, List<WordMeaning> wordMeanings) {
        return new SearchWordResult(headWord, headWordFull, "transitive verb", wordMeanings);
    }

    public static WordMeaning createWordMeaning(String header, List<Translation> translations) {
        return new WordMeaning(header, translations);
    }

    public static Translation createTranslation(String source, String target) {
        return new Translation(source, target);
    }

    public static TranslatedWord createTranslatedWord(SectionRowEnum sectionRowtype, String sourceWord,
                      String targetWord, String wordClass, Integer sectionNumber, Integer subSectionNumber, Boolean isHidden) {
        return new TranslatedWord(sectionRowtype, sourceWord, targetWord, wordClass, sectionNumber, subSectionNumber, isHidden);
    }

}
