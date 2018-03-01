package com.example.frank.jyanfapa;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class Main2Activity extends AppCompatActivity {


//    private TextView mTextMessage;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };
     Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);


        //設定toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層

        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.selectTabAtPosition(1,true);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Log.d("Tab","選擇: "+tabId);
                Log.d("Tab鳥","id "+R.id.tab_bird);
                Log.d("Tab行事曆","id "+R.id.tab_calendar);
                Log.d("Tab檔案","id "+R.id.tab_profile);
                Log.d("Tab商品","id "+R.id.tab_product);
                switch (tabId)
                {
                    case R.id.tab_bird:
                        Log.d("Tab","鳥");
                       // gotoAddDove();
                       // gotoCard();
                        toolbar.setTitle("養鴿紀錄");
                        // Create new fragment and transaction
                        Fragment newFragment1 = new CardFragment();
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                        transaction1.replace(R.id.fragment_container, newFragment1);
                        transaction1.addToBackStack(null);

// Commit the transaction
                        transaction1.commit();
                        break;
                    case R.id.tab_calendar:
                        Log.d("Tab","行事曆");
                        toolbar.setTitle("九彥養鴿");
                        // Create new fragment and transaction
                        Fragment newFragment = new CalendarFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                        transaction.replace(R.id.fragment_container, newFragment);
                        transaction.addToBackStack(null);

// Commit the transaction
                        transaction.commit();
                      //  gotoCalendar();
                        break;
                    case R.id.tab_profile:
                        Log.d("Tab","個人檔案");
                        toolbar.setTitle("個人資料");

                        // Create new fragment and transaction



                        Fragment newFragment2 = new ProfilemainFragment();
                        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                        transaction2.replace(R.id.fragment_container, newFragment2);
                        transaction2.addToBackStack(null);
// Commit the transaction
                        transaction2.commit();


                        break;
                    case R.id.tab_product:
                        Log.d("Tab","購物清單");
                        gotoProductList();
                        break;
                    default:
                        break;
                }


            }
        });

    }
    private void gotoProfile()
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    private void gotoProductList()
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
    private void gotoLoft()
    {
        Intent intent = new Intent(this, LoftActivity.class);
        startActivity(intent);
    }
    private void gotoAddPigeon()
    {
        Intent intent = new Intent(this, AddPigeonActivity.class);
        startActivity(intent);
    }
    private void gotoCalendar()
    {
        Intent intent = new Intent(this, CalendarFragment.class);
        startActivity(intent);
    }
    private void gotoCard()
    {
        Intent intent = new Intent(this, CardView.class);
        startActivity(intent);
    }
    public void clearData()
    {
        SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
        settings.edit().clear().commit();
    }
    private void gotoAddDove()
    {
        Intent intent = new Intent(this, AddDoveActivity.class);
        startActivity(intent);
    }
    private void setDefaultFragment()
    {

    }

}
