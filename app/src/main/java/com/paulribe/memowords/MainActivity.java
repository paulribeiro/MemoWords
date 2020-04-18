package com.paulribe.memowords;


import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulribe.memowords.model.mContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomMenu;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBottomMenu();

        if(!mContext.getIsRevisionFinished()) {
            int nbWordToRevise = mContext.getWordsToDisplay().size();
            if(nbWordToRevise != 0) {
                setBadgeText(Integer.toString(nbWordToRevise));
            }
        }
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

    public void setBadgeText(String badgeText) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomMenu.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        if(notificationBadge != null) {
            itemView.removeView(notificationBadge);
        }
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notifications_badgeTextView);
        textView.setText(badgeText);
        itemView.addView(notificationBadge);
    }

    public void deleteBadge() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomMenu.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);

        itemView.removeView(notificationBadge);
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
}
