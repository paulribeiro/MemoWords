package com.paulribe.memowords.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.enumeration.SectionRowEnum;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.model.pons.PonsResult;
import com.paulribe.memowords.recyclerViews.OnExpandSectionClickListener;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;
import com.paulribe.memowords.recyclerViews.OnWordTranslatedClickListener;
import com.paulribe.memowords.recyclerViews.translationResult.TranslationResultAdapter;
import com.paulribe.memowords.recyclerViews.word.WordAdapter;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.PonsService;
import com.paulribe.memowords.restclient.RetrofitClientInstance;
import com.paulribe.memowords.viewmodels.ListWordsViewModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WordAdapter adapter;
    private TranslationResultAdapter adapterTranslationResult;
    private ListWordsViewModel listWordsViewModel;
    private FrameLayout searchBarLayout;
    private TextView searchBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button deleteSearchWordButton;
    private TextView sourceLanguageTextView;
    private TextView targetLanguageTextView;
    private ImageButton swapLanguageButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataBinding();

        recyclerView = view.findViewById(R.id.recycler_view_words);
        searchBarLayout = view.findViewById(R.id.search_word_toolbar);
        searchBar = view.findViewById(R.id.search_word);
        toolbar = view.findViewById(R.id.toolbarList);
        deleteSearchWordButton = view.findViewById(R.id.delete_search_word);
        sourceLanguageTextView = view.findViewById(R.id.sourceLanguageTextView);
        targetLanguageTextView = view.findViewById(R.id.targetLanguageTextView);
        swapLanguageButton = view.findViewById(R.id.swapLanguageButton);
        createOptionMenuOrderBy();
        configureRecyclerView();

        swapLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWordsViewModel.exchangeSourceTargetLanguage();
            }
        });
    }

    private void configureRecyclerView() {
        listWordsViewModel.setRecyclerViewOnTranslateResults(Boolean.FALSE);
        if(this.adapter == null) {
            OnFavoriteClickListener favoriteClickListener = createFavoriteClickListener();
            View.OnClickListener ponsClickListener = createPonsClickListener();
            View.OnClickListener googleTranslateClickListener = createGoogleTranslateClickListener();
            this.adapter = new WordAdapter(listWordsViewModel.getWords().getValue(), favoriteClickListener, ponsClickListener, googleTranslateClickListener);
        }
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void configureRecyclerViewForTranslationResult() {
        listWordsViewModel.setRecyclerViewOnTranslateResults(Boolean.TRUE);
        OnWordTranslatedClickListener onTranslationClickListener = new OnWordTranslatedClickListener() {
            @Override
            public void onClick(View v) {
                Word newWord = createWordFromTranslatedWord(this.getTranslatedWord());
                ((MainActivity)getActivity()).displayNewWordFragment(newWord, Boolean.FALSE);
            }
        };

        OnExpandSectionClickListener onExpandSectionClickListener = new OnExpandSectionClickListener() {
            @Override
            public void onClick(View v) {
                updateRecyclerViewForTranslatedWord(this.getTranslatedSection());
            }
        };

        this.adapterTranslationResult = new TranslationResultAdapter(listWordsViewModel.getTranslatedWordResults().getValue(),
                onTranslationClickListener, onExpandSectionClickListener);
        this.recyclerView.setAdapter(this.adapterTranslationResult);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private Word createWordFromTranslatedWord(TranslatedWord translatedWord) {
        //TODO : manage which one is source and which one is target
        Optional<TranslatedWord> subsectionForTranslatedWord = listWordsViewModel.getTranslatedWordResults().getValue().stream()
                .filter(w -> w.getSectionRowtype().equals(SectionRowEnum.SUBSECTION) && w.getSubSectionNumber().equals(w.getSubSectionNumber()))
                .findFirst();
        String contextNewWord = "";
        if(subsectionForTranslatedWord.isPresent()) {
            contextNewWord = subsectionForTranslatedWord.get().getSourceWord();
        }
        Word word = new Word(translatedWord.getSourceWord(), translatedWord.getTargetWord(),
                new Date().getTime(), listWordsViewModel.NO_LAST_SUCCESS, listWordsViewModel.NO_LAST_TRY,
                0, 0, contextNewWord, 0, false);
        return word;
    }

    private OnFavoriteClickListener createFavoriteClickListener() {
        return new OnFavoriteClickListener(){
                @Override
                public void onClick(View v) {
                    listWordsViewModel.getFirebaseDataHelper().updateWord(this.getWord());
                }
            };
    }

    private View.OnClickListener createPonsClickListener() {
        return view -> {
                PonsService service = RetrofitClientInstance.getRetrofitInstance().create(PonsService.class);
                Call<List<PonsResult>> call = service.getAllTranslations(listWordsViewModel.getTranslationLanguagesPrefix(), listWordsViewModel.getSearchedString().getValue(),
                        listWordsViewModel.getCurrentSourceLanguage().getValue().getPrefixForPons(), listWordsViewModel.getxApiKeyPons());
                call.enqueue(new Callback<List<PonsResult>>() {
                    @Override
                    public void onResponse(Call<List<PonsResult>> call, Response<List<PonsResult>> response) {
                        listWordsViewModel.buildTranslation(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<PonsResult>> call, Throwable t) {
                        //progressDoalog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            };
    }

    private View.OnClickListener createGoogleTranslateClickListener() {
        return view -> {

        };
    }

    private void updateRecyclerView() {
        if(listWordsViewModel.getRecyclerViewOnTranslateResults()) {
           configureRecyclerView();
        }
        List<Word> words = listWordsViewModel.getWords().getValue();
        String searchWord = listWordsViewModel.getSearchedString().getValue();
        Boolean isFavorite = listWordsViewModel.getIsFavoriteSelected().getValue();
        OrderByEnum orderByEnum = listWordsViewModel.getOrderByEnum().getValue();
        if(isFavorite != null && isFavorite) {
            words = words.stream().filter(Word::isFavorite).collect(Collectors.toList());
        }
        if(searchWord != null && !searchWord.isEmpty()) {
            searchWord = updateStringWithIgnoredCharacter(searchWord);
            String finalSearchWord = searchWord;
            if(listWordsViewModel.getIsNativeLanguageToTranslation().getValue()) {
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
        adapter.setWords(words);
        adapter.notifyDataSetChanged();
    }

    private void updateRecyclerViewForTranslatedWord(TranslatedWord translatedSection) {
        List<TranslatedWord> translatedWords = listWordsViewModel.getTranslatedWordResults().getValue();
        List<TranslatedWord> subSectionsAndRows = translatedWords.stream()
                                            .filter(tw -> tw.getSectionNumber().equals(translatedSection.getSectionNumber()))
                                            .collect(Collectors.toList());
        for(TranslatedWord translatedWord : subSectionsAndRows) {
            translatedWord.setHidden(translatedSection.getHidden());
        }
        adapterTranslationResult.setPossibleTranslations(translatedWords);
        adapterTranslationResult.notifyDataSetChanged();
    }

    private void createOptionMenuOrderBy() {

        toolbar.inflateMenu(R.menu.menu_order_by);
        MenuItem menuItemOrderByLastTry = toolbar.getMenu().getItem(1).getSubMenu().getItem(0);
        MenuItem menuItemOrderByKnowledgeLevel = toolbar.getMenu().getItem(1).getSubMenu().getItem(1);
        MenuItem menuItemOrderByAZ = toolbar.getMenu().getItem(1).getSubMenu().getItem(2);
        MenuItem menuItemOrderByZA = toolbar.getMenu().getItem(1).getSubMenu().getItem(3);

        MenuItem.OnMenuItemClickListener menuItemClickListener = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_last_tried:
                    listWordsViewModel.getOrderByEnum().setValue(OrderByEnum.LAST_TRY);
                    break;
                case R.id.action_knowledge_level:
                    listWordsViewModel.getOrderByEnum().setValue(OrderByEnum.KNOWLEDGE_LEVEL);
                    break;
                case R.id.action_knowledge_level_desc:
                    listWordsViewModel.getOrderByEnum().setValue(OrderByEnum.KNOWLEDGE_LEVEL_DESC);
                    break;
                case R.id.action_AZ:
                    listWordsViewModel.getOrderByEnum().setValue(OrderByEnum.AZ);
                    break;
                case R.id.action_ZA:
                    listWordsViewModel.getOrderByEnum().setValue(OrderByEnum.ZA);
                    break;
                default:
                    break;
            }
            return true;
        };

        menuItemOrderByLastTry.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByKnowledgeLevel.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByAZ.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByZA.setOnMenuItemClickListener(menuItemClickListener);

        MenuItem menuItemFavorite = toolbar.getMenu().getItem(0);
        menuItemFavorite.setOnMenuItemClickListener(menuItem -> {
            listWordsViewModel.getIsFavoriteSelected().setValue(!listWordsViewModel.getIsFavoriteSelected().getValue());
            if(listWordsViewModel.getIsFavoriteSelected().getValue()) {
                menuItemFavorite.setIcon(R.drawable.star_filled);
            } else {
                menuItemFavorite.setIcon(R.drawable.star_empty_white);
            }
            return true;
        });

        deleteSearchWordButton.setVisibility(View.GONE);
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s != null && !s.toString().equals("")) {
                    deleteSearchWordButton.setVisibility(View.VISIBLE);
                } else {
                    deleteSearchWordButton.setVisibility(View.GONE);
                }
                listWordsViewModel.getSearchedString().setValue(s.toString());
            }
        });

        deleteSearchWordButton.setOnClickListener(view -> {
            listWordsViewModel.getSearchedString().setValue("");
            searchBar.setText("");
        });

    }

    private void initDataBinding() {
        listWordsViewModel = new ViewModelProvider(getActivity()).get(ListWordsViewModel.class);
        listWordsViewModel.init();
        setUpChangeValueListener();
        listWordsViewModel.readWords();
    }

    private void setUpChangeValueListener() {
        listWordsViewModel.getWords().observe(getViewLifecycleOwner(), this::onWordsChanged);
        listWordsViewModel.getOrderByEnum().observe(getViewLifecycleOwner(), this::onOrderByEnumChanged);
        listWordsViewModel.getIsFavoriteSelected().observe(getViewLifecycleOwner(), this::onIsFavoriteSelectedChanged);
        listWordsViewModel.getSearchedString().observe(getViewLifecycleOwner(), this::onSearchedStringChanged);
        listWordsViewModel.getTranslatedWordResults().observe(getViewLifecycleOwner(), this::onTranslatedWordResultsChanged);
        listWordsViewModel.getCurrentSourceLanguage().observe(getViewLifecycleOwner(), this::onCurrentSourceLanguageChanged);
        listWordsViewModel.getCurrentTargetLanguage().observe(getViewLifecycleOwner(), this::onCurrentTargetLanguageChanged);
        listWordsViewModel.getIsNativeLanguageToTranslation().observe(getViewLifecycleOwner(), this::onIsNativeLanguageToTranslation);
    }

    private void onIsNativeLanguageToTranslation(Boolean isNativeLanguageToTranslation) {
        adapter.setNativeLanguageToTranslation(isNativeLanguageToTranslation);
        adapter.notifyDataSetChanged();
    }

    private void onCurrentTargetLanguageChanged(LanguageEnum targetLanguage) {
        targetLanguageTextView.setText(targetLanguage.toString());
    }

    private void onCurrentSourceLanguageChanged(LanguageEnum sourceLanguage) {
        sourceLanguageTextView.setText(sourceLanguage.toString());
        if(listWordsViewModel.getCurrentSourceLanguage().getValue().equals(listWordsViewModel.getNativeLanguage())) {
            listWordsViewModel.getIsNativeLanguageToTranslation().setValue(Boolean.TRUE);
        } else {
            listWordsViewModel.getIsNativeLanguageToTranslation().setValue(Boolean.FALSE);
        }
    }

    private void onTranslatedWordResultsChanged(List<TranslatedWord> translatedWords) {
        configureRecyclerViewForTranslationResult();
    }

    private void onWordsChanged(List<Word> words) {
        updateRecyclerView();
    }

    private void onOrderByEnumChanged(OrderByEnum orderByEnum) {
        updateRecyclerView();
    }

    private void onIsFavoriteSelectedChanged(Boolean isFavoriteSelected) {
        updateRecyclerView();
    }

    private void onSearchedStringChanged(String searchedString) {
        updateRecyclerView();
    }

    private String updateStringWithIgnoredCharacter(String string) {
        return string.replace("é", "e")
                     .replace("è", "e")
                     .replace("ê", "e")
                     .toLowerCase();
    }
}
