package com.paulribe.memowords;

import android.content.Context;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


public class MainActivity extends AppCompatActivity {

    //popup
    private Button addButton;
    private EditText inputWordFR;
    private EditText inputWordDE;
    private BottomNavigationView bottomNav;
    private BottomNavigationView bottomMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBottomMenu();
    }

    private void createBottomMenu() {
        final Fragment fragment1 = new LearningFragment();
        final Fragment fragment2 = new NewWordFragment();
        final Fragment fragment3 = new ListFragment();
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment[] active = {fragment1};

        fm.beginTransaction().add(R.id.content, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.content, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.content,fragment1, "1").commit();

        bottomMenu = findViewById(R.id.bottom_nav);
        Menu menu = bottomMenu.getMenu();


        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                fm.beginTransaction().hide(active[0]).show(fragment1).commit();
                active[0] = fragment1;
                return true;
            }
        });

        bottomMenu.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                fm.beginTransaction().hide(active[0]).show(fragment2).commit();
                active[0] = fragment2;
                return true;
            }
        });

        bottomMenu.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                fm.beginTransaction().hide(active[0]).show(fragment3).commit();
                active[0] = fragment3;
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        View outPopup = popupView.findViewById(R.id.outPopup);
        // dismiss the popup window when touched
        outPopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        final View popupContent = popupView.findViewById(R.id.popupContent);
        popupContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(popupContent.getWindowToken(), 0);

                //Utils.hideSoftKeyboard();
                return true;
            }
        });

        addButton = (Button) popupView.findViewById(R.id.popupButton);
        addButton.setEnabled(false);
        inputWordFR = (EditText) popupView.findViewById(R.id.inputWordFR);
        inputWordDE = (EditText) popupView.findViewById(R.id.inputWordDE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //words.add(new Word());
                popupWindow.dismiss();
            }
        });

        inputWordFR.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        inputWordDE.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

    }

    private void checkRequiredFields() {
        if (!inputWordFR.getText().toString().isEmpty() && (!inputWordDE.getText().toString().isEmpty())) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }
}
