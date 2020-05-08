package com.paulribe.memowords.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.recyclerViews.WordAdapter;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WordAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_words);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        configureRecyclerView();

    }

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){
        // 3.2 - Create adapter passing the list of users
        this.adapter = new WordAdapter(mContext.getWords());
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void updateRecyclerView(OrderByEnum orderByEnum, String searchWord, boolean isOnlyFavorite) {
        List<Word> words = mContext.getWords();
        if(isOnlyFavorite) {
            words = words.stream().filter(Word::isFavorite).collect(Collectors.toList());
        }
        if(searchWord != null && !searchWord.isEmpty()) {
            searchWord = searchWord.replace("é", "e");
            searchWord = searchWord.replace("è", "e");
            String finalSearchWord = searchWord;
            words = words.stream().filter(w -> w.getWordFR().toLowerCase()
                                                            .replace("é", "e")
                                                            .replace("è", "e")
                                                            .contains(finalSearchWord))
                                        .collect(Collectors.toList());
        }
        switch(orderByEnum) {
            case AZ:
                Collections.sort(words, (word, word2) -> word.getWordFR().replace("é", "e")
                                                                        .replace("è","e")
                                                                        .replace("ê", "e")
                                                                        .compareTo(word2.getWordFR()));
                break;
            case ZA:
                Collections.sort(words, (word, word2) -> -1 * word.getWordFR().replace("é", "e")
                                                                                .replace("è","e")
                                                                                .replace("ê", "e")
                                                        .compareTo(word2.getWordFR()));
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
}
