package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.pons.PonsResult;
import com.paulribe.memowords.model.pons.SearchWordResult;
import com.paulribe.memowords.model.pons.SearchWordResultList;
import com.paulribe.memowords.model.pons.Translation;
import com.paulribe.memowords.model.pons.WordMeaning;

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

        return new Word(wordName, "DE1", dateAdded, lastTryTimeStamp, lastTryTimeStamp, numberTry, numberSuccess, "context", knowledgeLevel, false);
    }

    public static Word createWord(String wordName, Integer numberTry, Integer numberSuccess, Integer knowledgeLevel) {
        long dateAdded = Timestamp.valueOf("2020-01-01 00:00:00.000000000").getTime();
        return new Word(wordName, "DE1", dateAdded, BASE_LAST_SUCCESS_TIMESTAMP, BASE_LAST_TRY_TIMESTAMP, numberTry, numberSuccess, "context", knowledgeLevel, false);
    }

    public static PonsResult createPonsResult (List<SearchWordResultList> hits) {
        return new PonsResult("language", hits);
    }

    public static SearchWordResultList createSearchWordResultList(List<SearchWordResult> roms) {
        return new SearchWordResultList("entry", roms);
    }

    public static SearchWordResult createSearchWordResult(String headWordFull, List<WordMeaning> wordMeanings) {
        return new SearchWordResult("not important", headWordFull, "transitive verb", wordMeanings);
    }

    public static WordMeaning createWordMeaning(String header, List<Translation> translations) {
        return new WordMeaning(header, translations);
    }

    public static Translation createTranslation(String source, String target) {
        return new Translation(source, target);
    }
}
