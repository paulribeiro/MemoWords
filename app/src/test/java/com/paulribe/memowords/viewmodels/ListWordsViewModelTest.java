package com.paulribe.memowords.viewmodels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;
import com.paulribe.memowords.common.enumeration.LanguageEnum;
import com.paulribe.memowords.common.enumeration.OrderByEnum;
import com.paulribe.memowords.common.enumeration.SectionRowEnum;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.model.mymemory.Match;
import com.paulribe.memowords.common.model.mymemory.MyMemoryResult;
import com.paulribe.memowords.common.model.pons.SearchWordResult;
import com.paulribe.memowords.common.model.pons.SearchWordResultList;
import com.paulribe.memowords.common.model.pons.Translation;
import com.paulribe.memowords.common.model.pons.WordMeaning;
import com.paulribe.memowords.list.ListWordsViewModel;

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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        SearchWordResult searchWordResult = TestData.createSearchWordResult("","chercher <span class=\"phonetics\">[ʃɛʀʃe]</span> <span class=\"wordclass\"><acronym title=\"verb\">VB</acronym></span> <span class=\"verbclass\"><acronym title=\"transitive verb\">trans</acronym></span>", Arrays.asList(wordMeaning, wordMeaning2));
        SearchWordResult searchWordResult2 = TestData.createSearchWordResult("chercher <span class=\"phonetics\">[ʃɛʀʃe]</span> <span class=\"wordclass\"><acronym title=\"verb\">VB</acronym></span> <span class=\"verbclass\"><acronym title=\"transitive verb\">trans</acronym></span>", "", Arrays.asList());

        SearchWordResultList searchWordResultList = TestData.createSearchWordResultList(Arrays.asList(searchWordResult, searchWordResult2));

        viewModelMock.buildTranslation(Collections.singletonList(TestData.createPonsResult(Collections.singletonList(searchWordResultList))));

        final ArgumentCaptor<List<TranslatedWord>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(viewModelMock).setTranslatedWordResults(captor.capture());

        List<TranslatedWord> translatedWordResults = captor.getValue();
        Assert.assertNotNull(translatedWordResults);
        assertEquals(10, translatedWordResults.size());
        List<TranslatedWord> sections = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.SECTION))
                .collect(Collectors.toList());
        assertEquals(2, sections.size());
        assertEquals("chercher [ʃɛʀʃe] VB trans", sections.get(0).getSourceWord());
        assertEquals("transitive verb", sections.get(0).getWordClass());
        assertEquals("chercher [ʃɛʀʃe] VB trans", sections.get(1).getSourceWord());
        assertEquals("transitive verb", sections.get(1).getWordClass());

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
    public void buildTranslationEmptyTest() {

        viewModelMock.buildTranslation(null);

        final ArgumentCaptor<List<TranslatedWord>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(viewModelMock).setTranslatedWordResults(captor.capture());

        List<TranslatedWord> translatedWordResults = captor.getValue();
        Assert.assertNotNull(translatedWordResults);
        assertEquals(0, translatedWordResults.size());
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

        assertEquals(2, translatedWordResults.size());
        List<TranslatedWord> rows = translatedWordResults.stream().filter(t -> t.getSectionRowtype().equals(SectionRowEnum.ROW))
                .collect(Collectors.toList());
        assertEquals(2, rows.size());
        List<TranslatedWord> rows1 = rows.stream().filter(r -> r.getSubSectionNumber().equals(1) && r.getSectionNumber().equals(1)).collect(Collectors.toList());
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word1") && r.getTargetWord().equals("translation1")));
        assertTrue(rows1.stream().anyMatch(r -> r.getSourceWord().equals("word2") && r.getTargetWord().equals("translation2")));
    }

    @Test
    public void buildTranslationForMemoryNullTest() {

        viewModelMock.buildTranslationForMyMemory(null);

        final ArgumentCaptor<List<TranslatedWord>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(viewModelMock).setTranslatedWordResults(captor.capture());
        List<TranslatedWord> translatedWordResults = captor.getValue();
        Assert.assertNotNull(translatedWordResults);
        assertEquals(0, translatedWordResults.size());
    }

    @Test
    public void filterWordsToDisplayFavoriteOrderByLastTryTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "", Boolean.TRUE, OrderByEnum.LAST_TRY);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        assertEquals(3, wordsFiltered.size());
        assertEquals("word1", wordsFiltered.get(0).getWordNative());
        assertEquals("word2", wordsFiltered.get(1).getWordNative());
        assertEquals("école", wordsFiltered.get(2).getWordNative());
    }

    @Test
    public void filterWordsToDisplayOrderByAzTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "wor", Boolean.FALSE, OrderByEnum.AZ);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        assertEquals(3, wordsFiltered.size());
        assertEquals("word1", wordsFiltered.get(0).getWordNative());
        assertEquals("word2", wordsFiltered.get(1).getWordNative());
        assertEquals("word3", wordsFiltered.get(2).getWordNative());
    }

    @Test
    public void filterWordsToDisplayOrderByKnowledgeLevelTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "ec", Boolean.FALSE, OrderByEnum.KNOWLEDGE_LEVEL);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        assertEquals(2, wordsFiltered.size());
        assertEquals("ècrit", wordsFiltered.get(0).getWordNative());
        assertEquals("école", wordsFiltered.get(1).getWordNative());
    }

    @Test
    public void filterWordsToDisplayOrderByKnowledgeLevelDescTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        prepareMockForFilterWordsToDIsplay(wordsToFilter, "ec", Boolean.FALSE, OrderByEnum.KNOWLEDGE_LEVEL_DESC);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        assertEquals(2, wordsFiltered.size());
        assertEquals("école", wordsFiltered.get(0).getWordNative());
        assertEquals("ècrit", wordsFiltered.get(1).getWordNative());
    }

    @Test
    public void filterWordsToDisplayOrderByZaTest() {

        List<Word> wordsToFilter = createWordsToFIlter();

        Mockito.doReturn(new MutableLiveData<>(Boolean.FALSE)).when(viewModelMock).getIsNativeLanguageToTranslation();
        prepareMockForFilterWordsToDIsplay(wordsToFilter, "test", Boolean.FALSE, OrderByEnum.ZA);

        List<Word> wordsFiltered = viewModelMock.filterWordsToDisplay();

        assertEquals(2, wordsFiltered.size());
        assertEquals("word3", wordsFiltered.get(0).getWordNative());
        assertEquals("word2", wordsFiltered.get(1).getWordNative());
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
        Mockito.doReturn(new ArrayList<>(words)).when(viewModelMock).getFilteredWordsByKnowledgeLevel();
        Mockito.doReturn(new MutableLiveData<>(searchString)).when(viewModelMock).getSearchedString();
        Mockito.doReturn(new MutableLiveData<>(isFavoriteSelected)).when(viewModelMock).getIsFavoriteSelected();
        Mockito.doReturn(new MutableLiveData<>(orderByEnum)).when(viewModelMock).getOrderByEnum();
    }

    @Test
    public void convertToKnowledgeLevelEnumTest() {
        assertEquals(KnowledgeLevelEnum.NEW, viewModelMock.convertToKnowledgeLevelEnum(0));
        assertEquals(KnowledgeLevelEnum.SHORT_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(1));
        assertEquals(KnowledgeLevelEnum.SHORT_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(2));
        assertEquals(KnowledgeLevelEnum.MID_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(3));
        assertEquals(KnowledgeLevelEnum.MID_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(4));
        assertEquals(KnowledgeLevelEnum.LONG_TERM_MEMORY, viewModelMock.convertToKnowledgeLevelEnum(5));
    }

    @Test
    public void getNewTranslatedWordTest() {

        TranslatedWord row = TestData.createTranslatedWord(SectionRowEnum.ROW, "source", "target", "noun", 1, 1, false);

        Mockito.doReturn(new MutableLiveData<>(Boolean.TRUE)).when(viewModelMock).getIsNativeLanguageToTranslation();

        Word newTranslatedWord = viewModelMock.getNewTranslatedWord(row);

        assertEquals("source", newTranslatedWord.getWordNative());
        assertEquals("target", newTranslatedWord.getWordTranslated());

        assertNull(newTranslatedWord.getLastSuccess());
        assertNull(newTranslatedWord.getLastTry());

        assertEquals(new Integer(0), newTranslatedWord.getNumberTry());
        assertEquals(new Integer(0), newTranslatedWord.getNumberSuccess());
        assertEquals("", newTranslatedWord.getContext());
        assertEquals(new Integer(0), newTranslatedWord.getNumberSuccess());
        assertFalse(newTranslatedWord.isFavorite());
    }

    @Test
    public void getNewTranslatedWordTestReverseTranslation() {

        TranslatedWord row = TestData.createTranslatedWord(SectionRowEnum.ROW, "source", "target", "noun", 1, 1, false);

        Mockito.doReturn(new MutableLiveData<>(Boolean.FALSE)).when(viewModelMock).getIsNativeLanguageToTranslation();

        Word newTranslatedWord = viewModelMock.getNewTranslatedWord(row);

        assertEquals("target", newTranslatedWord.getWordNative());
        assertEquals("source", newTranslatedWord.getWordTranslated());

        assertNull(newTranslatedWord.getLastSuccess());
        assertNull(newTranslatedWord.getLastTry());

        assertEquals(new Integer(0), newTranslatedWord.getNumberTry());
        assertEquals(new Integer(0), newTranslatedWord.getNumberSuccess());
        assertEquals("", newTranslatedWord.getContext());
        assertEquals(new Integer(0), newTranslatedWord.getNumberSuccess());
        assertFalse(newTranslatedWord.isFavorite());
    }

    @Test
    public void getFilteredWordsByKnowledgeLevelTest() {

        List<Word> words = Arrays.asList(
                TestData.createWord("word1", 0, 0, 0),
                TestData.createWord("word2", 0, 0, 0),
                TestData.createWord("word3", 0, 0, 1),
                TestData.createWord("word4", 0, 0, 1),
                TestData.createWord("word5", 0, 0, 2),
                TestData.createWord("word6", 0, 0, 2),
                TestData.createWord("word7", 0, 0, 3),
                TestData.createWord("word8", 0, 0, 4),
                TestData.createWord("word9", 0, 0, 5)
        );

        HashSet<KnowledgeLevelEnum> knowledgeLevels = new HashSet<>(Arrays.asList(KnowledgeLevelEnum.NEW, KnowledgeLevelEnum.LONG_TERM_MEMORY));

        Mockito.doReturn(new MutableLiveData<>(words)).when(viewModelMock).getWords();
        Mockito.doReturn(new MutableLiveData<>(knowledgeLevels)).when(viewModelMock).getKnowledgeFilterSelected();

        List<Word> filteredWordsByKnowledgeLevel = viewModelMock.getFilteredWordsByKnowledgeLevel();

        assertEquals(3, filteredWordsByKnowledgeLevel.size());
    }

    @Test
    public void getFilteredWordsByKnowledgeLevelEmptyFilterTest() {

        List<Word> words = Arrays.asList(
                TestData.createWord("word1", 0, 0, 0),
                TestData.createWord("word2", 0, 0, 0),
                TestData.createWord("word3", 0, 0, 1),
                TestData.createWord("word4", 0, 0, 1),
                TestData.createWord("word5", 0, 0, 2),
                TestData.createWord("word6", 0, 0, 2),
                TestData.createWord("word7", 0, 0, 3),
                TestData.createWord("word8", 0, 0, 4),
                TestData.createWord("word9", 0, 0, 5)
        );

        Mockito.doReturn(new MutableLiveData<>(words)).when(viewModelMock).getWords();
        Mockito.doReturn(new MutableLiveData<>(new ArrayList<>())).when(viewModelMock).getKnowledgeFilterSelected();

        List<Word> filteredWordsByKnowledgeLevel = viewModelMock.getFilteredWordsByKnowledgeLevel();

        assertEquals(9, filteredWordsByKnowledgeLevel.size());
    }

    @Test
    public void getFilteredWordsByKnowledgeLevelWordsNullTest() {

        List<KnowledgeLevelEnum> knowledgeLevels = Arrays.asList(KnowledgeLevelEnum.NEW, KnowledgeLevelEnum.LONG_TERM_MEMORY);

        Mockito.doReturn(new MutableLiveData<>()).when(viewModelMock).getWords();
        Mockito.doReturn(new MutableLiveData<>(knowledgeLevels)).when(viewModelMock).getKnowledgeFilterSelected();

        List<Word> filteredWordsByKnowledgeLevel = viewModelMock.getFilteredWordsByKnowledgeLevel();

        assertNotNull(filteredWordsByKnowledgeLevel);
        assertEquals(0, filteredWordsByKnowledgeLevel.size());
    }

    @Test
    public void getTranslationLanguagesPrefixTest() {

        Mockito.doReturn(new MutableLiveData<>(LanguageEnum.FRENCH)).when(viewModelMock).getCurrentSourceLanguage();
        Mockito.doReturn(new MutableLiveData<>(LanguageEnum.PORTUGUESE)).when(viewModelMock).getCurrentTargetLanguage();

        String translationLanguagesPrefix = viewModelMock.getTranslationLanguagesPrefix();

        assertEquals("frpt", translationLanguagesPrefix);
    }

    @Test
    public void getTranslationLanguagesPrefixReverseTest() {

        Mockito.doReturn(new MutableLiveData<>(LanguageEnum.SPANISH)).when(viewModelMock).getCurrentSourceLanguage();
        Mockito.doReturn(new MutableLiveData<>(LanguageEnum.GERMAN)).when(viewModelMock).getCurrentTargetLanguage();

        String translationLanguagesPrefix = viewModelMock.getTranslationLanguagesPrefix();

        assertEquals("dees", translationLanguagesPrefix);
    }

    @Test
    public void addKnowledgeLevelFilterTest() {

        viewModelMock.addKnowledgeLevelFilter(KnowledgeLevelEnum.NEW);
        viewModelMock.addKnowledgeLevelFilter(KnowledgeLevelEnum.MID_TERM_MEMORY);
        MutableLiveData<HashSet<KnowledgeLevelEnum>> knowledgeFilterSelected = viewModelMock.getKnowledgeFilterSelected();

        assertNotNull(knowledgeFilterSelected.getValue());
        assertEquals(2, knowledgeFilterSelected.getValue().size());
        assertTrue(knowledgeFilterSelected.getValue().contains(KnowledgeLevelEnum.NEW));
        assertTrue(knowledgeFilterSelected.getValue().contains(KnowledgeLevelEnum.MID_TERM_MEMORY));

        viewModelMock.deleteKnowledgeLevelFilter(KnowledgeLevelEnum.MID_TERM_MEMORY);

        knowledgeFilterSelected = viewModelMock.getKnowledgeFilterSelected();

        assertNotNull(knowledgeFilterSelected.getValue());
        assertEquals(1, knowledgeFilterSelected.getValue().size());
        assertTrue(knowledgeFilterSelected.getValue().contains(KnowledgeLevelEnum.NEW));
    }
}
