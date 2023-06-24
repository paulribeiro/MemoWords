package com.paulribe.memowords.newword;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.recyclerviews.DividerItemDecoration;
import com.paulribe.memowords.common.recyclerviews.findword.FindWordAdapter;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;

public class NewWordFragment extends Fragment {


    @Getter
    private NewWordViewModel newWordViewModel;
    @Getter
    private Button addButton;
    private Button deleteButton;
    private Button exitEditWordButton;
    @Getter
    private EditText inputWordNative;
    @Getter
    private EditText inputWordTranslation;
    private EditText inputWordContext;
    @Getter
    private RecyclerView suggestionWordNativeRecyclerView;
    @Getter
    private RecyclerView suggestionWordTranslationRecyclerView;
    private TextView popupCurrentWordEditedTextView;
    private TextView popupAddTitleTextView;
    private TextView popupEditTitleTextView;
    @Getter
    private FindWordAdapter adapterWordNative;
    @Getter
    private FindWordAdapter adapterWordTranslation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_word, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataBinding();
        defineViews(view);

        addButton.setOnClickListener(view1 -> addWord());
        exitEditWordButton.setOnClickListener(view1 -> switchToAddWordMode());
        deleteButton.setOnClickListener(view1 -> deleteWord());

        hideKeyboardWhenTouchingOutside(view);

        Drawable drawable = getActivity().getDrawable(R.drawable.divider);
        suggestionWordTranslationRecyclerView.addItemDecoration(
                new DividerItemDecoration(drawable,
                        false, false));
        suggestionWordNativeRecyclerView.addItemDecoration(
                new DividerItemDecoration(drawable,
                        false, false));

        configureRecyclerView();
        configureInputWordEditTexts();
    }

    private void configureInputWordEditTexts() {
        inputWordNative.addTextChangedListener(
                new InputWordTextWatcher(this, true));
        inputWordNative.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionWordNativeRecyclerView.setVisibility(View.GONE);
            }
        });
        inputWordTranslation.addTextChangedListener(
                new InputWordTextWatcher(this, false));
        inputWordTranslation.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionWordTranslationRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void initDataBinding() {
        newWordViewModel = new ViewModelProvider(getActivity()).get(NewWordViewModel.class);
        newWordViewModel.init();
    }

    private void defineViews(@NonNull View view) {
        addButton = view.findViewById(R.id.popupButton);
        addButton.setEnabled(false);
        deleteButton = view.findViewById(R.id.popupDeleteButton);
        deleteButton.setVisibility(View.GONE);
        inputWordNative = view.findViewById(R.id.inputWordNative);
        inputWordTranslation = view.findViewById(R.id.inputWordTranslation);
        inputWordContext = view.findViewById(R.id.inputWordContext);
        suggestionWordNativeRecyclerView = view.findViewById(R.id.suggestionWordFR);
        suggestionWordTranslationRecyclerView = view.findViewById(R.id.suggestionWordDE);
        popupEditTitleTextView = view.findViewById(R.id.popupEditTitle);
        exitEditWordButton = view.findViewById(R.id.exitEditWordButton);
        popupCurrentWordEditedTextView = view.findViewById(R.id.popupCurrentWordEdited);
        popupAddTitleTextView = view.findViewById(R.id.popupAddTitle);
        TextView textViewWordTranslation = view.findViewById(R.id.textViewWordDE);
        TextView textViewWordNative = view.findViewById(R.id.textview_word_fr);
        if(newWordViewModel.getCurrentLanguage().getValue() != null) {
            textViewWordTranslation.setText(getString(R.string.word_in, newWordViewModel.getCurrentLanguage().getValue().toString(getContext())));
        }
        textViewWordNative.setText(getString(R.string.word_in, newWordViewModel.getNativeLanguage().toString(getContext())));
        suggestionWordNativeRecyclerView.setHasFixedSize(true);
        suggestionWordTranslationRecyclerView.setHasFixedSize(true);
        popupCurrentWordEditedTextView.setVisibility(View.GONE);
        popupEditTitleTextView.setVisibility(View.GONE);
        exitEditWordButton.setVisibility(View.GONE);
        suggestionWordNativeRecyclerView.setVisibility(View.GONE);
        suggestionWordTranslationRecyclerView.setVisibility(View.GONE);
    }

    private void hideKeyboardWhenTouchingOutside(@NonNull View view) {
        view.findViewById(R.id.outPopup).setOnTouchListener((v, event) -> {
            hideKeyboard();
            return true;
        });

        view.findViewById(R.id.scrollView).setOnTouchListener((v, event) -> {
            hideKeyboard();
            return true;
        });
    }

    private void addWord() {
        if(newWordViewModel.getNewWord() == null) {
            Word word = new Word(inputWordNative.getText().toString(), inputWordTranslation.getText().toString(),
                    new Date().getTime(), null, null,
                    0,0, inputWordContext.getText().toString(), 0, false);
            newWordViewModel.addWord(word);
        } else {
            newWordViewModel.updateWord(inputWordTranslation.getText().toString(),
                    inputWordNative.getText().toString(), inputWordContext.getText().toString());
            switchToAddWordMode();
        }
        emptyInputFields();
        deleteButton.setVisibility(View.GONE);
    }

    private void configureRecyclerView(){
        this.adapterWordNative = new FindWordAdapter(new ArrayList<>(), true, this::switchToEditWordMode);
        this.suggestionWordNativeRecyclerView.setAdapter(this.adapterWordNative);
        this.suggestionWordNativeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordNativeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.adapterWordTranslation = new FindWordAdapter(new ArrayList<>(), false, this::switchToEditWordMode);
        this.suggestionWordTranslationRecyclerView.setAdapter(this.adapterWordTranslation);
        this.suggestionWordTranslationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordTranslationRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void switchToEditWordMode(Word word) {
        updateWithWord(word);
        addButton.setText(R.string.update_word);
        suggestionWordTranslationRecyclerView.setVisibility(View.GONE);
        suggestionWordNativeRecyclerView.setVisibility(View.GONE);
        popupCurrentWordEditedTextView.setVisibility(View.VISIBLE);
        popupEditTitleTextView.setVisibility(View.VISIBLE);
        exitEditWordButton.setVisibility(View.VISIBLE);
        popupAddTitleTextView.setVisibility(View.GONE);
        popupCurrentWordEditedTextView.setText(word.getWordNative() + " - " + word.getWordTranslated());
        deleteButton.setVisibility(View.VISIBLE);
    }

    public void updateWithWord(Word word) {
        newWordViewModel.setNewWord(word);
        updateInputFields(word);
    }

    public void updateInputFields(Word word) {
        inputWordTranslation.setText(word.getWordTranslated());
        inputWordNative.setText(word.getWordNative());
        inputWordContext.setText(word.getContext());
    }

    private void switchToAddWordMode() {
        newWordViewModel.setNewWord(null);
        popupCurrentWordEditedTextView.setVisibility(View.GONE);
        popupEditTitleTextView.setVisibility(View.GONE);
        exitEditWordButton.setVisibility(View.GONE);
        popupAddTitleTextView.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        addButton.setText(R.string.add_word);
    }

    private void deleteWord() {
        newWordViewModel.deleteWord();
        switchToAddWordMode();
        emptyInputFields();
    }

    private void emptyInputFields() {
        inputWordNative.setText("");
        inputWordTranslation.setText("");
        inputWordContext.setText("");
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


}
