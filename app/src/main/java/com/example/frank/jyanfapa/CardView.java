package com.example.frank.jyanfapa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jyanfapa.Dialog.InputDialog;
import com.example.frank.jyanfapa.SQLite.GroupsDAO;
import com.example.frank.jyanfapa.SQLite.PigeonDAO;
import com.example.frank.jyanfapa.entities.Pigeon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2018/2/26.
 */

public class CardView extends AppCompatActivity {

    List<Map<String, Object>> items;
    PigeonDAO pigeonDAO;
    GroupsDAO groupsDAO;
    RecyclerView dList;
    public String[][] data = {
            {"生日", "請輸入生日"},
            {"電子郵件", "請輸入電子郵件"},
            {"真實姓名", "請輸入真實姓名"},
            {"地址", "請輸入地址"},
    };

    int position,choose;
    @Override
    protected void onResume() {
        super.onResume();
//        List<Map<String, Object>> items = new ArrayList<>();
//        if(pigeonDAO.getCount()>0)
//        {
//            for(int i =1;i<=pigeonDAO.getCount();i++)
//            {
//                Log.d("鴿子",pigeonDAO.get(i).getId()+" : "+pigeonDAO.get(i).getName());
////                imgText.add(pigeonDAO.get(i).getName());
//
//                Map<String, Object> item = new HashMap<>();
//                item.put("name", pigeonDAO.get(i).getName());
//                item.put("picture",pigeonDAO.get(i).getPhoto());
//                item.put("circle",pigeonDAO.get(i).getCircle());
//                items.add(item);
//            }
//        }
//
//        MyAdapter myAdapter = new MyAdapter(items);
//        dList.setAdapter(myAdapter);
//        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Log.d("RecycleView","選取: "+position);
//            }
//        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydove);



        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        choose = bundle.getInt("choose");

        pigeonDAO = new PigeonDAO(getApplicationContext());
        groupsDAO = new GroupsDAO(getApplicationContext());



        items = new ArrayList<>();
        if(pigeonDAO.getCount()>0)
        {
            for(int i =1;i<=pigeonDAO.getCount();i++)
            {
                Log.d("鴿子",pigeonDAO.get(i).getId()+" : "+pigeonDAO.get(i).getName());
//                imgText.add(pigeonDAO.get(i).getName());
                Map<String, Object> item = new HashMap<>();
                Log.d("鴿子資料庫",pigeonDAO.get(i).getId() +" : " + position);
                if(pigeonDAO.get(i).getId() == position)
                {
                    //donothing
                }
                else
                {
                    item.put("name", pigeonDAO.get(i).getName());
                    item.put("picture",pigeonDAO.get(i).getPhoto());
                    item.put("circle",pigeonDAO.get(i).getCircle());
                    item.put("id",pigeonDAO.get(i).getId());
                    items.add(item);
                }
            }
        }

        MyAdapter myAdapter = new MyAdapter(items);
        dList = (RecyclerView) findViewById(R.id.dove_recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dList.setLayoutManager(layoutManager);
        dList.setHasFixedSize( false) ;
        dList.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position1) {
                Log.d("RecycleView","選取: "+position1);
                int id = Integer.parseInt(items.get(position1).get("id").toString());
                Log.d("選取的父母親id",id+"");
                if(choose == 0)
                {
                    pigeonDAO.get(position).setFather(id);
                    Log.d("id設定",pigeonDAO.get(position).getFather()+"");
                    Pigeon newdove = pigeonDAO.get(position);
                    newdove.setFather(id);
                    Log.d("新的準備update",newdove.getName()+ ","+newdove.getId());
                    pigeonDAO.update(newdove);
                }
                else if(choose == 1)
                {
                    pigeonDAO.get(position).setMother(id);
                    Log.d("id設定",pigeonDAO.get(position).getMother()+"");
                    Pigeon newdove = pigeonDAO.get(position);
                    newdove.setMother(id);
                    Log.d("新的準備update",newdove.getName()+ ","+newdove.getId());
                    pigeonDAO.update(newdove);
                }

//                Log.d("新的準備update",newdove.getName()+ ","+newdove.getId());
//                pigeonDAO.update(newdove);

                        Log.d("累死",position+"");
                finish();
            }
        });
    }


    private void gotoAddDove()
    {
        Intent intent = new Intent(this, AddDoveActivity.class);
        startActivity(intent);
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
       // private List<String> dData;
       private  List<Map<String, Object>> items;
        private OnItemClickListener mOnItemClickListener = null;

        //define interface
        public interface OnItemClickListener {
            void onItemClick(View view , int position);
        }
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public CircleImageView circleImageView;
            public TextView circleView;
            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.d_name);
                circleView = (TextView)v.findViewById(R.id.d_id);
                circleImageView = (CircleImageView)v.findViewById(R.id.d_picture);
            }
        }

        public MyAdapter(List<Map<String, Object>> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dove_card, parent, false);
            ViewHolder vh = new ViewHolder(v);
            v.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setTag(position);

            holder.mTextView.setText(items.get(position).get("name").toString());
            holder.circleView.setText("環號: "+items.get(position).get("circle").toString());

            Log.d("圖片",items.get(position).get("picture").toString());

            if(items.get(position).get("picture").toString().equals(""))
            {
                Log.d("圖片","沒圖");
            }
            else
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte bytes[] = stream.toByteArray();
                bytes = Base64.decode(items.get(position).get("picture").toString(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap
                holder.circleImageView.setImageBitmap(bmp);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }
}
