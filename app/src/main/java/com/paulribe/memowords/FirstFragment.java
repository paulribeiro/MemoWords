package com.paulribe.memowords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    private int cpt;
    private List<Word> words;
    private TextView textViewWord;
    private TextView textViewTranslation;
    private boolean answerShown = false;
    private Button showAnswerButton;
    private Button easyButton;
    private Button difficultButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseDataHelper firebaseDataHelper = new FirebaseDataHelper();
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words.addAll(w);
                textViewWord.setText(words.get(cpt).getWordDE());
                textViewTranslation.setText(words.get(cpt).getWordFR());
            }

            @Override
            public void dataIsInserted() {

            }

            @Override
            public void dataIsUpdated() {

            }

            @Override
            public void dataIsDeleted() {

            }
        });

        words = new ArrayList<>();
        cpt = 0;
        textViewWord = view.findViewById(R.id.textview_word);
        textViewTranslation = view.findViewById(R.id.textview_translation);
        showAnswerButton = view.findViewById(R.id.button);
        easyButton = view.findViewById(R.id.button2);
        difficultButton = view.findViewById(R.id.button3);
        easyButton.setVisibility(View.GONE);
        difficultButton.setVisibility(View.GONE);
        textViewTranslation.setVisibility(View.GONE);

        /*
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        });
*/
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerButton.setVisibility(View.GONE);
                easyButton.setVisibility(View.VISIBLE);
                difficultButton.setVisibility(View.VISIBLE);
                textViewTranslation.setVisibility(View.VISIBLE);

            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerButton.setVisibility(View.VISIBLE);
                easyButton.setVisibility(View.GONE);
                difficultButton.setVisibility(View.GONE);
                textViewWord.setText(words.get(cpt).getWordDE());
                textViewTranslation.setText(words.get(cpt).getWordFR());
                textViewTranslation.setVisibility(View.GONE);
                firebaseDataHelper.updateWord(words.get(cpt));
                cpt++;

            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerButton.setVisibility(View.VISIBLE);
                easyButton.setVisibility(View.GONE);
                difficultButton.setVisibility(View.GONE);
                textViewWord.setText(words.get(cpt).getWordDE());
                textViewTranslation.setText(words.get(cpt).getWordFR());
                textViewTranslation.setVisibility(View.GONE);
                cpt++;
            }
        });

    }
}
