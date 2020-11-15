package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.enumeration.KnowledgeLevelEnum;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.enumeration.SectionRowEnum;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mymemory.Match;
import com.paulribe.memowords.model.mymemory.MyMemoryResult;
import com.paulribe.memowords.model.pons.SearchWordResult;
import com.paulribe.memowords.model.pons.SearchWordResultList;
import com.paulribe.memowords.model.pons.Translation;
import com.paulribe.memowords.model.pons.WordMeaning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class ListWordsViewModelTest {

    private ListWordsViewModel viewModelMock;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        ListWordsViewModel viewModel = new ListWordsViewModel();
        viewModelMock = Mockito.spy(viewModel);
        Mockito.doReturn(new MutableLiveData<>(LanguageEnum.FRENCH)).when(viewModelMock).getCurrentLanguage();
        viewModelMock.init();
    }

    @Test
    public void buildTranslationTest() {

        List<Translation> translations = Arrays.asList(TestData.createTranslation("word1", "wordTranslation1"),
                TestData.createTranslation("word2", "wordTranslation2"),
                TestData.createTranslation("word3", "wordTranslation3"));
        List<Translation> translations2 = Arrays.asList(TestData.createTranslation("word4", "wordTranslation4"),
                TestData.createTranslation("word5", "wordTranslation5"),
                TestData.createTranslation("word6", "wordTranslation6"));
        WordMeaning wordMeaning = TestData.createWordMeaning("wordSearchedVar1", translations);
        WordMeaning wordMeaning2 = TestData.createWordMeaning("wordSearchedVar2", translations2);

        SearchWordResult searchWordResult = TestData.createSearchWordResult("chercher <span class=\"phonetics\">[ʃɛʀʃe]</span> <span class=\"wordclass\"><acronym title=\"verb\">VB</acronym></span> <span class=\"verbclass\"><acronym title=\"transitive verb\">trans</acronym></span>", Arrays.asList(wordMeaning, wordMeaning2));
        SearchWordResultList searchWordResultList = TestData.createSearchWordResultList(Collections.singletonList(searchWordResult));

        viewModelMock.buildTranslation(Collections.singletonList(TestData.createPonsResult(Collections.singletonList(searchWordResultList))));

        final ArgumentCaptor<List<TranslatedWord>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(viewModelMock).setTranslatedWordResults(captor.capture());

        List<TranslatedWord> translatedWordResults = captor.getValue();
        Assert.assertNotNull(translatedWordResults);
        assertEquals(9, translatedWordResults.size());
        List<TranslatedWord> sections = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.SECTION))
                .collect(Collectors.toList());
        assertEquals(1, sections.size());
        assertEquals("chercher  [ʃɛʀʃe] VB trans", sections.get(0).getSourceWord());
        assertEquals("transitive verb", sections.get(0).getWordClass());

        List<TranslatedWord> subSections = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.SUBSECTION))
                .collect(Collectors.toList());
        assertEquals(2, subSections.size());
        Optional<TranslatedWord> subSection1 = subSections.stream().filter(s -> s.getSectionNumber().equals(1) && s.getSubSectionNumber().equals(1)).findFirst();
        assertTrue(subSection1.isPresent());
        assertEquals("wordSearchedVar1", subSection1.get().getSourceWord());
        Optional<TranslatedWord> subSection2 = subSections.stream().filter(s -> s.getSectionNumber().equals(1) && s.getSubSectionNumber().equals(2)).findFirst();
        assertTrue(subSection2.isPresent());
        assertEquals("wordSearchedVar2", subSection2.get().getSourceWord());

        List<TranslatedWord> rows = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.ROW))
                .collect(Collectors.toList());
        assertEquals(6, rows.size());
        List<TranslatedWord> rows1 = rows.stream().filter(r -> r.getSubSectionNumber().equals(1) && r.getSectionNumber().equals(1)).collect(Collectors.toList());
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word1") && r.getTargetWord().equals("wordTranslation1")));
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word2") && r.getTargetWord().equals("wordTranslation2")));
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word3") && r.getTargetWord().equals("wordTranslation3")));
        List<TranslatedWord> rows2 = rows.stream().filter(r -> r.getSubSectionNumber().equals(2) && r.getSectionNumber().equals(1)).collect(Collectors.toList());
        assertTrue(rows2.stream().anyMatch(r -> r.getSourceWord().equals("word4") && r.getTargetWord().equals("wordTranslation4")));
        assertTrue(rows2.stream().anyMatch(r -> r.getSourceWord().equals("word5") && r.getTargetWord().equals("wordTranslation5")));
        assertTrue(rows2.stream().anyMatch(r -> r.getSourceWord().equals("word6") && r.getTargetWord().equals("wordTranslation6")));
    }

    @Test
    public void buildTranslationForMemoryTest() {
        MyMemoryResult myMemoryResult = new MyMemoryResult();
        Match match = new Match(436210817, "word1", "translation1", "fr-FR", "pt-PT", 0, null, 1, "");
        Match match2 = new Match(436210818, "word2", "translation2", "fr-FR", "pt-PT", 0, null, 1, "");
        myMemoryResult.setMatches(Arrays.asList(match, match2));

        viewModelMock.buildTranslationForMyMemory(myMemoryResult);

        final ArgumentCaptor<List<TranslatedWord>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(viewModelMock).setTranslatedWordResults(captor.capture());
        List<TranslatedWord> translatedWordResults = captor.getValue();
        Assert.assertNotNull(translatedWordResults);

        List<TranslatedWord> rows = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.ROW))
                .collect(Collectors.toList());
        assertEquals(2, rows.size());
        List<TranslatedWord> rows1 = rows.stream().filter(r -> r.getSubSectionNumber().equals(1) && r.getSectionNumber().equals(1)).collect(Collectors.toList());
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word1") && r.getTargetWord().equals("translation1")));
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word2") && r.getTargetWord().equals("translation2")));
    }

    @Test
    public void filterWordsToDisplayFavoriteOrderByLastTryTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "", Boolean.TRUE, OrderByEnum.LAST_TRY);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        Assert.assertEquals(3, wordsFiltered.size());
        Assert.assertEquals("word1", wordsFiltered.get(0).getWordFR());
        Assert.assertEquals("word2", wordsFiltered.get(1).getWordFR());
        Assert.assertEquals("école", wordsFiltered.get(2).getWordFR());
    }

    @Test
    public void filterWordsToDisplayOrderByAzTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "wor", Boolean.FALSE, OrderByEnum.AZ);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        Assert.assertEquals(3, wordsFiltered.size());
        Assert.assertEquals("word1", wordsFiltered.get(0).getWordFR());
        Assert.assertEquals("word2", wordsFiltered.get(1).getWordFR());
        Assert.assertEquals("word3", wordsFiltered.get(2).getWordFR());
    }

    @Test
    public void filterWordsToDisplayOrderByKnowledgeLevelTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "ec", Boolean.FALSE, OrderByEnum.KNOWLEDGE_LEVEL);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        Assert.assertEquals(2, wordsFiltered.size());
        Assert.assertEquals("ècrit", wordsFiltered.get(0).getWordFR());
        Assert.assertEquals("école", wordsFiltered.get(1).getWordFR());
    }

    @Test
    public void filterWordsToDisplayOrderByKnowledgeLevelDescTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "ec", Boolean.FALSE, OrderByEnum.KNOWLEDGE_LEVEL_DESC);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        Assert.assertEquals(2, wordsFiltered.size());
        Assert.assertEquals("école", wordsFiltered.get(0).getWordFR());
        Assert.assertEquals("ècrit", wordsFiltered.get(1).getWordFR());
    }

    @Test
    public void filterWordsToDisplayOrderByZaTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        Mockito.doReturn(new MutableLiveData<Boolean>(Boolean.FALSE)).when(viewModelMock).getIsNativeLanguageToTranslation();
        prepareMockForFilterWordsToDIsplay(wordsToFilter, "test", Boolean.FALSE, OrderByEnum.ZA);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        Assert.assertEquals(2, wordsFiltered.size());
        Assert.assertEquals("word3", wordsFiltered.get(0).getWordFR());
        Assert.assertEquals("word2", wordsFiltered.get(1).getWordFR());
    }

    private List<Word> createWordsToFIlter() {
        long now = new Date().getTime();
        long yesterday = new Date(now - (1000 * 60 * 60 * 24)).getTime();
        long oneHourAgo = new Date(now - (1000 * 60 * 60)).getTime();

        Word word1 = new Word("word1", "translation1", now, now, now, 1, 1, "", 1, true);
        Word word2 = new Word("word2", "testa", oneHourAgo, oneHourAgo, oneHourAgo, 1, 1, "", 3, true);
        Word word3 = new Word("word3", "testé", yesterday, yesterday, yesterday, 1, 2, "", 1, false);
        Word word4 = new Word("ècrit", "translation4", now, now, now, 1, 1, "", 1, false);
        Word word5 = new Word("école", "translation5", yesterday, yesterday, yesterday, 1, 4, "", 4, true);
        return Arrays.asList(word1, word2, word3, word4, word5);
    }

    private void prepareMockForFilterWordsToDIsplay(List<Word> words, String searchString, Boolean isFavoriteSelected, OrderByEnum orderByEnum) {
        Mockito.doReturn(new ArrayList<Word>(words)).when(viewModelMock).getFilteredWords();
        Mockito.doReturn(new MutableLiveData<String>(searchString)).when(viewModelMock).getSearchedString();
        Mockito.doReturn(new MutableLiveData<Boolean>(isFavoriteSelected)).when(viewModelMock).getIsFavoriteSelected();
        Mockito.doReturn(new MutableLiveData<OrderByEnum>(orderByEnum)).when(viewModelMock).getOrderByEnum();
    }

    @Test
    public void convertToKnowledgeLevelEnumTest() {
        Assert.assertEquals(KnowledgeLevelEnum.NEW, viewModelMock.convertToKnowledgeLevelEnum(0));
        Assert.assertEquals(KnowledgeLevelEnum.SHORT_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(1));
        Assert.assertEquals(KnowledgeLevelEnum.SHORT_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(2));
        Assert.assertEquals(KnowledgeLevelEnum.MID_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(3));
        Assert.assertEquals(KnowledgeLevelEnum.MID_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(4));
        Assert.assertEquals(KnowledgeLevelEnum.LONG_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(5));
    }
}
