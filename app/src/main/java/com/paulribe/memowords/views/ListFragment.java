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
import android.widget.TextView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;
import com.paulribe.memowords.recyclerViews.WordAdapter;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.viewmodels.ListWordsViewModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WordAdapter adapter;

    private ListWordsViewModel listWordsViewModel;
    private FrameLayout searchBarLayout;
    private TextView searchBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button deleteSearchWordButton;

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

        createOptionMenuOrderBy();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        configureRecyclerView();

    }

    private void configureRecyclerView(){
        OnFavoriteClickListener favoriteClickListener = new OnFavoriteClickListener(){
            @Override
            public void onClick(View v) {
                listWordsViewModel.getFirebaseDataHelper().updateWord(this.getWord());
            }
        };
        this.adapter = new WordAdapter(listWordsViewModel.getWords().getValue(), favoriteClickListener);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void updateRecyclerView() {
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
            words = words.stream().filter(w -> updateStringWithIgnoredCharacter(w.getWordFR())
                                                            .contains(finalSearchWord))
                                                            .collect(Collectors.toList());
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
                Collections.sort(words, Comparator.comparing(Word::getKnowledgeLevel).reversed());
                break;
        }
        adapter.setWords(words);
        adapter.notifyDataSetChanged();
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
