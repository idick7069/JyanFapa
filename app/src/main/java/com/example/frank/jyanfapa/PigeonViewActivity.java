package com.example.frank.jyanfapa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.frank.jyanfapa.SQLite.GroupsDAO;
import com.example.frank.jyanfapa.SQLite.PigeonDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PigeonViewActivity extends AppCompatActivity {
    private GridView gridView;
    ListView listView;
    PigeonDAO pigeonDAO;
    GroupsDAO groupsDAO;
    List<String> imgText = new ArrayList<>();



//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
////                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
////                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
////                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_view);
        this.setTitle("養鴿紀錄");





        pigeonDAO = new PigeonDAO(getApplicationContext());
        groupsDAO = new GroupsDAO(getApplicationContext());


        List<Map<String, Object>> items = new ArrayList<>();
        if(pigeonDAO.getCount()>0)
        {
            for(int i =1;i<=pigeonDAO.getCount();i++)
            {
                Log.d("鴿子",pigeonDAO.get(i).getId()+" : "+pigeonDAO.get(i).getName());
                imgText.add(pigeonDAO.get(i).getName());

                Map<String, Object> item = new HashMap<>();
                item.put("name", pigeonDAO.get(i).getName());
                item.put("feather",pigeonDAO.get(i).getFeather());
                item.put("id",pigeonDAO.get(i).getId());
                items.add(item);
            }
        }


//        List<Map<String, Object>> items = new ArrayList<>();
//        for (int i = 0; i < imgText.size(); i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("text", imgText.get(i));
//            item.put("name",)
//            items.add(item);
//        }
        List<Map<String, Object>> groupitems = new ArrayList<>();
        if(groupsDAO.getCount()>0)
        {
            for(int i =1;i<=groupsDAO.getCount();i++)
            {
                Log.d("群組",groupsDAO.get(i).getGroupname());
                Map<String, Object> item = new HashMap<>();
                item.put("name", groupsDAO.get(i).getGroupname());
                groupitems.add(item);
            }

        }


//        SimpleAdapter adapter1 = new SimpleAdapter(this,
//                items, R.layout.group_item, new String[]{"name"}, new int[]{R.id.grouptext});
//        listView = (ListView)findViewById(R.id.custview);
//        listView.setAdapter(adapter1);




        SimpleAdapter adapter = new SimpleAdapter(this,
                items, R.layout.grid_item, new String[]{"name","feather","id"}, new int[]{R.id.text,R.id.textFeather,R.id.textID});
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PigeonViewActivity.this, "你選擇了" + pigeonDAO.get(position+1).getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
