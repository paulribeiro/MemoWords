package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.model.Word;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

@RunWith(JUnit4.class)
public class LearningViewModelTest {

    private static Integer HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 = 48;
    private static Integer HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 = 24*7 + 24;
    private static Integer HOURS_TO_WAIT_KNOWLEDGELEVEL_4 = 21*24*7 + 24;

    private LearningViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        viewModel = new LearningViewModel();
        viewModel.init();
    }

    @Test
    public void prepareWordsForRevisionTest() {

        List<Word> words = Arrays.asList(
                //Revise after 1 day
                TestData.createWord("new word", 0, 0,  0),
                TestData.createWord("knowledgeLevel 0, recently tried", 1, 4, 0,  0),
                TestData.createWord("knowledgeLevel 0, long time tried", HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 4, 0,  0),
                TestData.createWord("knowledgeLevel 1, recently tried",1, 4, 2,  1),
                TestData.createWord("knowledgeLevel 1, long time tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 4, 2,  1),
                //Revise after 1 week
                TestData.createWord("knowledgeLevel 2, recently tried",1, 5, 3,  2),
                TestData.createWord("knowledgeLevel 2, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                TestData.createWord("knowledgeLevel 2, long time tried", HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 + 1, 6, 3,  2),
                TestData.createWord("knowledgeLevel 3, recently tried",1, 7, 4,  3),
                TestData.createWord("knowledgeLevel 3, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                TestData.createWord("knowledgeLevel 3, long time tried", HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 + 1, 7, 5,  3),
                //Revise after 1 month
                TestData.createWord("knowledgeLevel 4, recently tried",1, 8, 5,  4),
                TestData.createWord("knowledgeLevel 4, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 + 1, 8, 5,  4),
                TestData.createWord("knowledgeLevel 4, long time tried", HOURS_TO_WAIT_KNOWLEDGELEVEL_4 + 1, 10, 5,  4),
                //Revise never
                TestData.createWord("knowledgeLevel 5, recently tried",1, 12, 6,  5),
                TestData.createWord("knowledgeLevel 5, long time tried", HOURS_TO_WAIT_KNOWLEDGELEVEL_4 + 1, 12, 6,  5)
        );

        viewModel.setWords(words);
        viewModel.prepareWords();

        List<Word> wordsToDisplay = viewModel.getWordsToDisplay();
        Assert.assertEquals(5, wordsToDisplay.size());
    }


    @Test
    public void prepareWordsForLearningRevisionNotFinishedTest() {

        List<Word> words = Arrays.asList(
                //Revise after 1 day
                TestData.createWord("new word", 0, 0,  0),
                TestData.createWord("new word 2", 0, 0,  0),
                TestData.createWord("knowledgeLevel 0, recently tried", 1, 4, 0,  0),
                TestData.createWord("knowledgeLevel 1, recently tried",1, 4, 2,  1),
                //Revise after 1 week
                TestData.createWord("knowledgeLevel 2, recently tried",1, 5, 3,  2),
                TestData.createWord("knowledgeLevel 2, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                TestData.createWord("knowledgeLevel 3, recently tried",1, 7, 4,  3),
                TestData.createWord("knowledgeLevel 3, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                //Revise after 1 month
                TestData.createWord("knowledgeLevel 4, recently tried",1, 8, 5,  4),
                TestData.createWord("knowledgeLevel 4, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 + 1, 8, 5,  4),
                //Revise never
                TestData.createWord("knowledgeLevel 5, recently tried",1, 12, 6,  5)
        );

        viewModel.setWords(words);
        viewModel.prepareWords();

        List<Word> wordsToDisplay = viewModel.getWordsToDisplay();
        Assert.assertEquals(0, wordsToDisplay.size());
    }

    @Test
    public void prepareWordsForLearningRevisionFinishedTest() {

        List<Word> words = Arrays.asList(
                //Revise after 1 day
                TestData.createWord("new word", 0, 0,  0),
                TestData.createWord("new word 2", 0, 0,  0),
                TestData.createWord("knowledgeLevel 0, recently tried", 1, 4, 0,  0),
                TestData.createWord("knowledgeLevel 1, recently tried",1, 4, 2,  1),
                //Revise after 1 week
                TestData.createWord("knowledgeLevel 2, recently tried",1, 5, 3,  2),
                TestData.createWord("knowledgeLevel 2, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                TestData.createWord("knowledgeLevel 3, recently tried",1, 7, 4,  3),
                TestData.createWord("knowledgeLevel 3, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_0_1 + 1, 7, 5,  3),
                //Revise after 1 month
                TestData.createWord("knowledgeLevel 4, recently tried",1, 8, 5,  4),
                TestData.createWord("knowledgeLevel 4, tried",HOURS_TO_WAIT_KNOWLEDGELEVEL_2_3 + 1, 8, 5,  4),
                //Revise never
                TestData.createWord("knowledgeLevel 5, recently tried",1, 12, 6,  5)
        );

        viewModel.setWords(words);
        viewModel.getIsRevisionFinished().setValue(Boolean.TRUE);
        viewModel.prepareWords();

        List<Word> wordsToDisplay = viewModel.getWordsToDisplay();
        Assert.assertEquals(2, wordsToDisplay.size());
    }

    @Test
    public void updateLearningStateTest() {

    }
}
