package com.paulribe.memowords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.recyclerViews.FindWordAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NewWordFragment extends Fragment {

    private Button addButton;
    private EditText inputWordFR;
    private EditText inputWordDE;
    private EditText inputWordContext;
    private mContext mContext;
    private RecyclerView suggestionWordFRRecyclerView;
    private RecyclerView suggestionWordDERecyclerView;

    private FindWordAdapter adapterFR;
    private FindWordAdapter adapterDE;

    private Word newWord;
    private boolean isInputFRFromDB;
    private boolean isInputDEFromDB;
    private boolean isChangeFromClickOnRecyclerView = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_word, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = new mContext();
        isInputFRFromDB = false;
        isInputDEFromDB = false;

        addButton = view.findViewById(R.id.popupButton);
        addButton.setEnabled(false);
        inputWordFR = view.findViewById(R.id.inputWordFR);
        inputWordDE = view.findViewById(R.id.inputWordDE);
        inputWordContext = view.findViewById(R.id.inputWordContext);
        suggestionWordFRRecyclerView = view.findViewById(R.id.suggestionWordFR);
        suggestionWordDERecyclerView = view.findViewById(R.id.suggestionWordDE);
        suggestionWordFRRecyclerView.setHasFixedSize(true);
        suggestionWordDERecyclerView.setHasFixedSize(true);
        suggestionWordFRRecyclerView.setVisibility(View.GONE);
        suggestionWordDERecyclerView.setVisibility(View.GONE);

        //DividerItemDecoration decoration = new DividerItemDecoration(getParentFragment().getContext(), DividerItemDecoration.HORIZONTAL);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWord();
                inputWordFR.setText("");
                inputWordDE.setText("");
                inputWordContext.setText("");
            }
        });

        inputWordFR.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!isChangeFromClickOnRecyclerView) {
                    if(newWord != null && isInputFRFromDB) {
                        newWord = null;
                        isInputFRFromDB = false;
                        addButton.setText("add word");
                    }
                    if(s.toString().isEmpty()) {
                        suggestionWordFRRecyclerView.setVisibility(View.GONE);
                    } else {
                        suggestionWordFRRecyclerView.setVisibility(View.VISIBLE);
                        adapterFR.setWords(mContext.getWords().stream().filter(w -> w.getWordFR().toLowerCase().contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList()));
                        adapterFR.notifyDataSetChanged();
                    }
                } else {
                    if(isInputFRFromDB) {
                        isChangeFromClickOnRecyclerView = false;
                    }
                }
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        inputWordFR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    suggestionWordFRRecyclerView.setVisibility(View.GONE);
                }
            }});

        inputWordDE.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!isChangeFromClickOnRecyclerView) {
                    if(newWord != null && isInputDEFromDB) {
                        newWord = null;
                        isInputDEFromDB = false;
                        addButton.setText("add word");
                    }
                    if(s.toString().isEmpty()) {
                        suggestionWordDERecyclerView.setVisibility(View.GONE);
                    } else {
                        suggestionWordDERecyclerView.setVisibility(View.VISIBLE);
                        adapterDE.setWords(mContext.getWords().stream().filter(w -> w.getWordDE().toLowerCase().contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList()));
                        adapterDE.notifyDataSetChanged();
                    }
                } else {
                    if(isInputDEFromDB) {
                        isChangeFromClickOnRecyclerView = false;
                    }
                }
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        inputWordDE.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    suggestionWordDERecyclerView.setVisibility(View.GONE);
                }
            }});

        hideKeyboardWhenTouchingOutside(view);
        configureRecyclerView();
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
        if(newWord == null) {
            Word word = new Word(inputWordFR.getText().toString(), inputWordDE.getText().toString(),
                    new Date().getTime(), mContext.NO_LAST_SUCCESS, mContext.NO_LAST_TRY,
                    0,0, inputWordContext.getText().toString(), 0);
            mContext.addWord(word);
        } else {
            newWord.setWordDE(inputWordDE.getText().toString());
            newWord.setWordFR(inputWordFR.getText().toString());
            newWord.setContext(inputWordContext.getText().toString());
            mContext.updateWord(newWord);
        }
    }

    private void configureRecyclerView(){
        this.adapterFR = new FindWordAdapter(new ArrayList<>(), true, new FindWordAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(Word word) {
                newWord = word;
                isInputFRFromDB = true;
                isChangeFromClickOnRecyclerView = true;
                inputWordDE.setText(word.getWordDE());
                inputWordFR.setText(word.getWordFR());
                inputWordContext.setText(word.getContext());
                addButton.setText("update word");
                suggestionWordFRRecyclerView.setVisibility(View.GONE);
                suggestionWordDERecyclerView.setVisibility(View.GONE);
            }
        });
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.suggestionWordFRRecyclerView.setAdapter(this.adapterFR);
        // 3.4 - Set layout manager to position the items
        this.suggestionWordFRRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordFRRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.adapterDE = new FindWordAdapter(new ArrayList<>(), false, new FindWordAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(Word word) {
                newWord = word;
                isInputDEFromDB = true;
                isChangeFromClickOnRecyclerView = true;
                inputWordFR.setText(word.getWordFR());
                inputWordContext.setText(word.getContext());
                inputWordDE.setText(word.getWordDE());
                addButton.setText("update word");
                suggestionWordDERecyclerView.setVisibility(View.GONE);
                suggestionWordFRRecyclerView.setVisibility(View.GONE);
            }
        });
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.suggestionWordDERecyclerView.setAdapter(this.adapterDE);
        // 3.4 - Set layout manager to position the items
        this.suggestionWordDERecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.suggestionWordDERecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
