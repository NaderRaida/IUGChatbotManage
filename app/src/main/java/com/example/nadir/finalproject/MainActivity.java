package com.example.nadir.finalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //    private TextView mTextMessage;
    Fragment selectedFragment =null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    if(getSupportFragmentManager().findFragmentByTag("Home") == null){
                        selectedFragment = new HomeFragment();
//                        Toast.makeText(MainActivity.this, "new Home Instance", Toast.LENGTH_SHORT).show();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("Home");
//                        Toast.makeText(MainActivity.this, "سابق", Toast.LENGTH_SHORT).show();

                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"Home").addToBackStack("Home").commit();

//                    Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigation_account:
//                    mTextMessage.setText(R.string.title_dashboard);
                    if(getSupportFragmentManager().findFragmentByTag("Account") == null){
                        selectedFragment = new AccountFragment();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("Account");
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"Account").addToBackStack("Account").commit();
//                    Toast.makeText(MainActivity.this, "account", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.navigation_features:
                    if(getSupportFragmentManager().findFragmentByTag("Features") == null){
                        selectedFragment = new FeaturesFragment();
                    }else{
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("Features");
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment,"Features").addToBackStack("Features").commit();

//                    Toast.makeText(MainActivity.this, "features", Toast.LENGTH_SHORT).show();

//                    mTextMessage.setText(R.string.title_notifications);
                    break;

            }


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new HomeFragment()).addToBackStack("Home").commit();
            navigation.setSelectedItemId(R.id.navigation_home);

        }
    }

}