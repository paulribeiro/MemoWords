package com.paulribe.memowords.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.OrderByEnum;
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

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataBinding();

        recyclerView = view.findViewById(R.id.recycler_view_words);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        configureRecyclerView();

    }

    private void configureRecyclerView(){
        this.adapter = new WordAdapter(listWordsViewModel.getWords().getValue());
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
