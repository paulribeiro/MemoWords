package com.paulribe.memowords.newword;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.recyclerviews.DividerItemDecoration;
import com.paulribe.memowords.common.recyclerviews.findword.FindWordAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class NewWordFragment extends Fragment {

    private Button addButton;
    private Button deleteButton;
    private EditText inputWordFR;
    private EditText inputWordDE;
    private EditText inputWordContext;
    private RecyclerView suggestionWordFRRecyclerView;
    private RecyclerView suggestionWordDERecyclerView;
    private Button exitEditWordButton;
    private TextView popupCurrentWordEditedTextView;
    private TextView popupAddTitleTextView;
    private TextView popupEditTitleTextView;

    private TextView textViewWordTranslation;
    private TextView textViewWordNative;

    private FindWordAdapter adapterFR;
    private FindWordAdapter adapterDE;

    private NewWordViewModel newWordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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

        inputWordFR.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(newWordViewModel.getNewWord() == null) {
                    if(s.toString().isEmpty() || CollectionUtils.isEmpty(newWordViewModel.getWords()) || s.toString().length() < 3) {
                        suggestionWordFRRecyclerView.setVisibility(View.GONE);
                    } else {
                        suggestionWordFRRecyclerView.setVisibility(View.VISIBLE);
                        adapterFR.setWords(newWordViewModel.getWords().stream().filter(w -> w.getWordNative().toLowerCase().contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList()));
                        adapterFR.notifyDataSetChanged();
                    }
                }
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        inputWordFR.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionWordFRRecyclerView.setVisibility(View.GONE);
            }
        });

        inputWordDE.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(newWordViewModel.getNewWord() == null) {
                    if(s.toString().isEmpty() || CollectionUtils.isEmpty(newWordViewModel.getWords()) || s.toString().length() < 3) {
                        suggestionWordDERecyclerView.setVisibility(View.GONE);
                    } else {
                        suggestionWordDERecyclerView.setVisibility(View.VISIBLE);
                        adapterDE.setWords(newWordViewModel.getWords().stream().filter(w -> w.getWordTranslated().toLowerCase().contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList()));
                        adapterDE.notifyDataSetChanged();
                    }
                }
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        inputWordDE.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionWordDERecyclerView.setVisibility(View.GONE);
            }
        });

        hideKeyboardWhenTouchingOutside(view);

        Drawable drawable = getActivity().getDrawable(R.drawable.divider);
        suggestionWordDERecyclerView.addItemDecoration(
                new DividerItemDecoration(drawable,
                        false, false));
        suggestionWordFRRecyclerView.addItemDecoration(
                new DividerItemDecoration(drawable,
                        false, false));

        configureRecyclerView();
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
        inputWordFR = view.findViewById(R.id.inputWordNative);
        inputWordDE = view.findViewById(R.id.inputWordTranslation);
        inputWordContext = view.findViewById(R.id.inputWordContext);
        suggestionWordFRRecyclerView = view.findViewById(R.id.suggestionWordFR);
        suggestionWordDERecyclerView = view.findViewById(R.id.suggestionWordDE);
        popupEditTitleTextView = view.findViewById(R.id.popupEditTitle);
        exitEditWordButton = view.findViewById(R.id.exitEditWordButton);
        popupCurrentWordEditedTextView = view.findViewById(R.id.popupCurrentWordEdited);
        popupAddTitleTextView = view.findViewById(R.id.popupAddTitle);
        textViewWordTranslation = view.findViewById(R.id.textViewWordDE);
        textViewWordNative = view.findViewById(R.id.textview_word_fr);
        if(newWordViewModel.getCurrentLanguage().getValue() != null) {
            textViewWordTranslation.setText(getString(R.string.word_in, newWordViewModel.getCurrentLanguage().getValue().toString(getContext())));
        }
        textViewWordNative.setText(getString(R.string.word_in, newWordViewModel.getNativeLanguage().toString(getContext())));
        suggestionWordFRRecyclerView.setHasFixedSize(true);
        suggestionWordDERecyclerView.setHasFixedSize(true);
        popupCurrentWordEditedTextView.setVisibility(View.GONE);
        popupEditTitleTextView.setVisibility(View.GONE);
        exitEditWordButton.setVisibility(View.GONE);
        suggestionWordFRRecyclerView.setVisibility(View.GONE);
        suggestionWordDERecyclerView.setVisibility(View.GONE);
    }

    private void hideKeyboardWhenTouchingOutside(@NonNull View view) {
        view.findViewById(R.id.outPopup).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });

        view.findViewById(R.id.scrollView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });
    }

    private void checkRequiredFields() {
        if (!inputWordFR.getText().toString().isEmpty() && !inputWordDE.getText().toString().isEmpty()) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    private void addWord() {
        if(newWordViewModel.getNewWord() == null) {
            Word word = new Word(inputWordFR.getText().toString(), inputWordDE.getText().toString(),
                    new Date().getTime(), null, null,
                    0,0, inputWordContext.getText().toString(), 0, false);
            newWordViewModel.addWord(word);
        } else {
            newWordViewModel.updateWord(inputWordDE.getText().toString(),
                    inputWordFR.getText().toString(), inputWordContext.getText().toString());
            switchToAddWordMode();
        }
        emptyInputFields();
        deleteButton.setVisibility(View.GONE);
    }

    private void configureRecyclerView(){
        this.adapterFR = new FindWordAdapter(new ArrayList<>(), true, new FindWordAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(Word word) {
                switchToEditWordMode(word);
            }
        });
        this.suggestionWordFRRecyclerView.setAdapter(this.adapterFR);
        this.suggestionWordFRRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordFRRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.adapterDE = new FindWordAdapter(new ArrayList<>(), false, new FindWordAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(Word word) {
                switchToEditWordMode(word);
            }
        });
        this.suggestionWordDERecyclerView.setAdapter(this.adapterDE);
        this.suggestionWordDERecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordDERecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void switchToEditWordMode(Word word) {
        updateWithWord(word);
        addButton.setText(R.string.update_word);
        suggestionWordDERecyclerView.setVisibility(View.GONE);
        suggestionWordFRRecyclerView.setVisibility(View.GONE);
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
        inputWordDE.setText(word.getWordTranslated());
        inputWordFR.setText(word.getWordNative());
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
        inputWordFR.setText("");
        inputWordDE.setText("");
        inputWordContext.setText("");
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
