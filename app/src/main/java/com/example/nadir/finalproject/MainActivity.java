package com.example.nadir.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment =null;
    BottomNavigationView navigation;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(getSupportFragmentManager().findFragmentByTag("Home") == null){
                        selectedFragment = new HomeFragment();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("Home");

                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"Home").commit();
                    break;
                case R.id.navigation_about:
                    if(getSupportFragmentManager().findFragmentByTag("About") == null){
                        selectedFragment = new AboutFragment();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("About");
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"About").commit();
                    break;
                case R.id.navigation_chatbot:
                    if(getSupportFragmentManager().findFragmentByTag("Chatbot") == null){
                        selectedFragment = new ChatFragment();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("Chatbot");
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"Chatbot").commit();
                    break;

            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new HomeFragment()).addToBackStack("Home").commit();
            navigation.setSelectedItemId(R.id.navigation_home);

        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        String fragment_name = getSupportFragmentManager().getBackStackEntryAt((getSupportFragmentManager().getBackStackEntryCount())-1).getName();
//        if (fragment_name.equals("Home")){
//        navigation.setSelectedItemId(R.id.navigation_home);
//        }else if(fragment_name.equals("About")){
//            navigation.setSelectedItemId(R.id.navigation_about);
//        }else if(fragment_name.equals("Chatbot")){
//            navigation.setSelectedItemId(R.id.navigation_chatbot);
//        }
//
//    }
}