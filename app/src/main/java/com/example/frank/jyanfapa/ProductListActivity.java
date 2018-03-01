package com.example.frank.jyanfapa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.frank.jyanfapa.entities.Product;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;
    SimpleDraweeView draweeView;

  // public static final String URL = "https://api.learn2crack.com/android/jsonandroid/";
   public static final String URL = "http://140.125.81.27/blog2/public/Home";

    private List<HashMap<String, String>> mProductMapList = new ArrayList<>();

    private static final String KEY_PICTUREURL = "pictureurl";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_product_list);
        mListView = (ListView) findViewById(R.id.main_listview);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
    }
    @Override
    public void onLoaded(List<Product> productListList) {

        for (Product product : productListList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_PICTUREURL, "http://140.125.81.27/blog2/public/"+product.getUrl());
            map.put(KEY_NAME, product.getName());
            map.put(KEY_TYPE, product.getType());

            mProductMapList.add(map);
        }

        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this,  mProductMapList.get(i).get(KEY_NAME),Toast.LENGTH_LONG).show();
        Toast.makeText(this,  "購物商城建置中，系統將轉至網站頁面..",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,WebActivity.class);
        startActivity(intent);
    }

    private void loadListView() {

        Log.d("到底","load");
        ListAdapter adapter = new SimpleAdapter(ProductListActivity.this,  mProductMapList, R.layout.product_row,
                new String[] { KEY_PICTUREURL, KEY_NAME, KEY_TYPE },
                new int[] { R.id.img_thumb,R.id.text_title, R.id.text_desc});

        mListView.setAdapter(adapter);

    }

}
