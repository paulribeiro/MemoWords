package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.model.Word;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

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

}
