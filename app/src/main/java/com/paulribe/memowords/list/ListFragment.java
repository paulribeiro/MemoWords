package com.paulribe.memowords.list;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.paulribe.memowords.LoadingDialog;
import com.paulribe.memowords.MainActivity;
import com.paulribe.memowords.R;
import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;
import com.paulribe.memowords.common.enumeration.LanguageEnum;
import com.paulribe.memowords.common.enumeration.OrderByEnum;
import com.paulribe.memowords.common.model.KnowledgeLevelFilter;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.model.mymemory.MyMemoryResult;
import com.paulribe.memowords.common.model.pons.PonsResult;
import com.paulribe.memowords.common.recyclerviews.OnExpandSectionClickListener;
import com.paulribe.memowords.common.recyclerviews.OnFavoriteClickListener;
import com.paulribe.memowords.common.recyclerviews.OnWordTranslatedClickListener;
import com.paulribe.memowords.common.recyclerviews.SwipeHelper;
import com.paulribe.memowords.common.recyclerviews.UnderlayButton;
import com.paulribe.memowords.common.recyclerviews.knowledgelevelfilter.KnowledgeLevelFilterAdapter;
import com.paulribe.memowords.common.recyclerviews.translationresult.TranslationResultAdapter;
import com.paulribe.memowords.common.recyclerviews.word.WordAdapter;
import com.paulribe.memowords.common.restclient.MyMemoryService;
import com.paulribe.memowords.common.restclient.PonsService;
import com.paulribe.memowords.common.restclient.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFilter;
    private WordAdapter adapter;
    private KnowledgeLevelFilterAdapter knowledgeLevelFilterAdapter;
    private TranslationResultAdapter adapterTranslationResult;
    private ListWordsViewModel listWordsViewModel;
    private TextView searchBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button deleteSearchWordButton;
    private TextView sourceLanguageTextView;
    private TextView targetLanguageTextView;
    private View listFragmentView;
    private SwipeHelper swipeHelper;
    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataBinding();

        recyclerView = view.findViewById(R.id.recycler_view_words);
        recyclerViewFilter = view.findViewById(R.id.recyclerViewFilter);
        searchBar = view.findViewById(R.id.search_word);
        toolbar = view.findViewById(R.id.toolbarList);
        deleteSearchWordButton = view.findViewById(R.id.delete_search_word);
        sourceLanguageTextView = view.findViewById(R.id.sourceLanguageTextView);
        targetLanguageTextView = view.findViewById(R.id.targetLanguageTextView);
        ImageButton swapLanguageButton = view.findViewById(R.id.swapLanguageButton);
        listFragmentView = view;

        createOptionMenuOrderBy();
        configureRecyclerView();
        configureFilterRecyclerView();

        swapLanguageButton.setOnClickListener(view1 -> {
            listWordsViewModel.exchangeSourceTargetLanguage();
            updateRecyclerView();
        });
    }

    private void showDeleteWordSnackBarWithUndoOption() {
        Snackbar snackbar = Snackbar
                .make(listFragmentView, String.format("%s removed from the list", listWordsViewModel.getLastWordDeleted().getWordNative()),
                        BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setAction("UNDO", view -> listWordsViewModel.restoreLastWordDeleted());
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void configureFilterRecyclerView() {
        if(this.knowledgeLevelFilterAdapter == null) {
            KnowledgeLevelFilterAdapter.OnViewClickListener onViewClickListener = knowledgeLevelFilter -> {
                knowledgeLevelFilter.setSelected(!knowledgeLevelFilter.getSelected());
                if(knowledgeLevelFilter.getSelected()) {
                    listWordsViewModel.addKnowledgeLevelFilter(knowledgeLevelFilter.getKnowledgeLevelEnum());
                } else {
                    listWordsViewModel.deleteKnowledgeLevelFilter(knowledgeLevelFilter.getKnowledgeLevelEnum());
                }
            };
            this.knowledgeLevelFilterAdapter = new KnowledgeLevelFilterAdapter(createKnowledgeLevelFilterList(), onViewClickListener);
        }
        this.recyclerViewFilter.setAdapter(this.knowledgeLevelFilterAdapter);
        this.recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());
    }

    private List<KnowledgeLevelFilter> createKnowledgeLevelFilterList() {
        List<KnowledgeLevelFilter> knowledgeLevelFilterList = new ArrayList<>();
        for(KnowledgeLevelEnum knowledgeLevelEnum : KnowledgeLevelEnum.values()) {
            knowledgeLevelFilterList.add(new KnowledgeLevelFilter(knowledgeLevelEnum, false));
        }
        return knowledgeLevelFilterList;
    }

    private void configureRecyclerView() {
        listWordsViewModel.setRecyclerViewOnTranslateResults(Boolean.FALSE);
        if(this.adapter == null) {
            OnFavoriteClickListener favoriteClickListener = createFavoriteClickListener();
            View.OnClickListener ponsClickListener = null;
            if(listWordsViewModel.getPossiblePonsTranslations().contains(listWordsViewModel.getTranslationLanguagesPrefix())) {
                ponsClickListener = createPonsClickListener();
            }
            View.OnClickListener myMemoryClickListener = createMyMemoryClickListener();
            List<Word> wordList = listWordsViewModel.getFilteredWordsByKnowledgeLevel();
            this.adapter = new WordAdapter(wordList, favoriteClickListener, ponsClickListener, myMemoryClickListener,
                    listWordsViewModel.getSearchedString().getValue());
        }
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeHelper = new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        BitmapFactory.decodeResource(getContext().getResources(),
                                R.drawable.bin_icon_48),
                        Color.parseColor("#FF3C30"),
                        pos -> {
                            listWordsViewModel.deleteWord(pos);
                            showDeleteWordSnackBarWithUndoOption();
                        }
                ));

                underlayButtons.add(new UnderlayButton(
                        "Edit",
                        BitmapFactory.decodeResource(getContext().getResources(),
                                R.drawable.edit_icon_40),
                        Color.parseColor("#3BDC1F"),
                        pos -> {
                            ((MainActivity)getActivity()).changeBottomMenuItemSelected(R.id.newWordFragment);
                            ((MainActivity)getActivity()).displayNewWordFragment(listWordsViewModel.getWordsToDisplay().get(pos), true);
                        }
                ));
            }
        };
    }

    private void configureRecyclerViewForTranslationResult() {
        listWordsViewModel.setRecyclerViewOnTranslateResults(Boolean.TRUE);
        OnWordTranslatedClickListener onTranslationClickListener = new OnWordTranslatedClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeBottomMenuItemSelected(R.id.newWordFragment);
                Word newWord = listWordsViewModel.getNewTranslatedWord(this.getTranslatedWord());
                ((MainActivity)getActivity()).displayNewWordFragment(newWord, false);
            }
        };

        OnExpandSectionClickListener onExpandSectionClickListener = new OnExpandSectionClickListener() {
            @Override
            public void onClick(View v) {
                updateRecyclerViewForTranslatedWord(this.getTranslatedSection());
            }
        };

        this.adapterTranslationResult = new TranslationResultAdapter(listWordsViewModel.getTranslatedWordResults().getValue(),
                onTranslationClickListener, onExpandSectionClickListener, listWordsViewModel.getSearchedString().getValue());
        this.recyclerView.setAdapter(this.adapterTranslationResult);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeHelper.stop();
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
                startLoader();
                hideKeyboard();
                PonsService service = RetrofitClientInstance.getRetrofitInstance().create(PonsService.class);
                Call<List<PonsResult>> call = service.getAllTranslations(listWordsViewModel.getTranslationLanguagesPrefix(), listWordsViewModel.getSearchedString().getValue(),
                        listWordsViewModel.getCurrentSourceLanguage().getValue().getPrefixForPons(), listWordsViewModel.getxApiKeyPons());
                call.enqueue(new Callback<List<PonsResult>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<PonsResult>> call, @NonNull Response<List<PonsResult>> response) {
                        stopLoader();
                        if(response.isSuccessful()) {
                            listWordsViewModel.buildTranslationForPons(response.body());
                            //TODO : is result is empty, display no result
                        } else {
                            //TODO : display no result
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<PonsResult>> call, @NonNull Throwable t) {
                        stopLoader();
                        Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            };
    }

    private View.OnClickListener createMyMemoryClickListener() {
        return view -> {
            startLoader();
            hideKeyboard();
            MyMemoryService service = RetrofitClientInstance.getRetrofitMyMemoryInstance().create(MyMemoryService.class);
            Call<MyMemoryResult> call = service.getTranslations(listWordsViewModel.getSearchedString().getValue(),
                    listWordsViewModel.getCurrentSourceLanguage().getValue().getPrefixForPons() + "|" + listWordsViewModel.getCurrentTargetLanguage().getValue().getPrefixForPons());
            call.enqueue(new Callback<MyMemoryResult>() {
                @Override
                public void onResponse(@NonNull Call<MyMemoryResult> call, @NonNull Response<MyMemoryResult> response) {
                    stopLoader();
                    if(response.isSuccessful()) {
                        listWordsViewModel.buildTranslationForMyMemory(response.body());
                        //TODO : is result is empty, display no result
                    } else {
                        //TODO : display no result
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyMemoryResult> call, @NonNull Throwable t) {
                    stopLoader();
                    Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        };
    }

    private void updateRecyclerView() {
        if(Boolean.TRUE.equals(listWordsViewModel.getIsRecyclerViewOnTranslateResults())) {
           configureRecyclerView();
        }
        List<Word> words = listWordsViewModel.filterWordsToDisplay();
        adapter.setSearchedWord(listWordsViewModel.getSearchedString().getValue());
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
        adapterTranslationResult.setSearchedWord(listWordsViewModel.getSearchedString().getValue());
        adapterTranslationResult.setPossibleTranslations(translatedWords);
        adapterTranslationResult.notifyDataSetChanged();
    }

    private void createOptionMenuOrderBy() {

        toolbar.inflateMenu(R.menu.menu_order_by);
        MenuItem menuItemOrderByLastTry = toolbar.getMenu().getItem(1).getSubMenu().getItem(0);
        MenuItem menuItemOrderByKnowledgeLevel = toolbar.getMenu().getItem(1).getSubMenu().getItem(1);
        MenuItem menuItemOrderByKnowledgeLevelDesc = toolbar.getMenu().getItem(1).getSubMenu().getItem(2);
        MenuItem menuItemOrderByAZ = toolbar.getMenu().getItem(1).getSubMenu().getItem(3);
        MenuItem menuItemOrderByZA = toolbar.getMenu().getItem(1).getSubMenu().getItem(4);

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
        menuItemOrderByKnowledgeLevelDesc.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByAZ.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByZA.setOnMenuItemClickListener(menuItemClickListener);

        MenuItem menuItemFavorite = toolbar.getMenu().getItem(0);
        menuItemFavorite.setOnMenuItemClickListener(menuItem -> {
            listWordsViewModel.getIsFavoriteSelected().setValue(!listWordsViewModel.getIsFavoriteSelected().getValue());
            if(Boolean.TRUE.equals(listWordsViewModel.getIsFavoriteSelected().getValue())) {
                menuItemFavorite.setIcon(R.drawable.star_filled);
            } else {
                menuItemFavorite.setIcon(R.drawable.star_empty_white);
            }
            return true;
        });

        deleteSearchWordButton.setVisibility(View.GONE);
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action to perform.
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s != null && !s.toString().equals("")) {
                    deleteSearchWordButton.setVisibility(View.VISIBLE);
                } else {
                    deleteSearchWordButton.setVisibility(View.GONE);
                }
                listWordsViewModel.getSearchedString().setValue(Objects.toString(s, ""));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action to perform.
            }
        });

        deleteSearchWordButton.setOnClickListener(view -> {
            listWordsViewModel.getSearchedString().setValue("");
            searchBar.setText("");
            hideKeyboard();
        });
    }

    public void startLoader() {
        if(loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.startLoadingDialog(getResources().getString(R.string.loading_translation));
    }

    public void stopLoader() {
        if(loadingDialog != null) {
            loadingDialog.dismissDialog();
        }
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
        listWordsViewModel.getKnowledgeFilterSelected().observe(getViewLifecycleOwner(), this::onKnowledgeFilterSelected);
    }

    private void onKnowledgeFilterSelected(HashSet<KnowledgeLevelEnum> knowledgeLevelEnums) {
        knowledgeLevelFilterAdapter.notifyDataSetChanged();
        updateRecyclerView();
    }

    private void onIsNativeLanguageToTranslation(Boolean isNativeLanguageToTranslation) {
        adapter.setNativeLanguageToTranslation(isNativeLanguageToTranslation);
        adapter.notifyDataSetChanged();
    }

    private void onCurrentTargetLanguageChanged(LanguageEnum targetLanguage) {
        targetLanguageTextView.setText(targetLanguage.toString(getContext()));
    }

    private void onCurrentSourceLanguageChanged(LanguageEnum sourceLanguage) {
        sourceLanguageTextView.setText(sourceLanguage.toString(getContext()));
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

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
