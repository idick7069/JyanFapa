package com.example.frank.jyanfapa;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jyanfapa.SQLite.GroupsDAO;
import com.example.frank.jyanfapa.SQLite.PigeonDAO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2018/2/28.
 */

public class CardFragment extends Fragment {

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

    @Override
    public void onResume() {
        super.onResume();
        List<Map<String, Object>> items = new ArrayList<>();
        if(pigeonDAO.getCount()>0)
        {
            for(int i =1;i<=pigeonDAO.getCount();i++)
            {
                Log.d("鴿子",pigeonDAO.get(i).getId()+" : "+pigeonDAO.get(i).getName());
//                imgText.add(pigeonDAO.get(i).getName());

                Map<String, Object> item = new HashMap<>();
                item.put("name", pigeonDAO.get(i).getName());
                item.put("picture",pigeonDAO.get(i).getPhoto());
                item.put("circle",pigeonDAO.get(i).getCircle());
                items.add(item);
            }
        }

        MyAdapter myAdapter = new MyAdapter(items);
        dList.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("RecycleView","選取: "+position);

                Intent intent = new Intent(getActivity(),DoveDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);

                //將Bundle物件傳給intent
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.mydove,container,false);

    }

    @Override

    public void onViewCreated(View view,Bundle savedInstanceState){

        super.onViewCreated(view,savedInstanceState);


        pigeonDAO = new PigeonDAO(getActivity().getApplicationContext());
        groupsDAO = new GroupsDAO(getActivity().getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "新增鴿子", Toast.LENGTH_LONG).show();
                gotoAddDove();
            }
        });

        List<Map<String, Object>> items = new ArrayList<>();
        if(pigeonDAO.getCount()>0)
        {
            for(int i =1;i<=pigeonDAO.getCount();i++)
            {
                Log.d("鴿子",pigeonDAO.get(i).getId()+" : "+pigeonDAO.get(i).getName());
//                imgText.add(pigeonDAO.get(i).getName());
                Log.d("鴿子2",pigeonDAO.get(i).getPhoto());

                Map<String, Object> item = new HashMap<>();
                item.put("name", pigeonDAO.get(i).getName());
                item.put("picture",pigeonDAO.get(i).getPhoto());
                item.put("circle",pigeonDAO.get(i).getCircle());
                items.add(item);
            }
        }

        MyAdapter myAdapter = new MyAdapter(items);
        dList = (RecyclerView) view.findViewById(R.id.dove_recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dList.setLayoutManager(layoutManager);
        dList.setHasFixedSize( false) ;
        dList.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("RecycleView","選取: "+position);
                Intent intent = new Intent(getActivity(),DoveDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);

                //將Bundle物件傳給intent
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    private void gotoAddDove()
    {
        Intent intent = new Intent(getActivity(), AddDoveActivity.class);
        startActivity(intent);
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
        // private List<String> dData;
        private  List<Map<String, Object>> items;
        private MyAdapter.OnItemClickListener mOnItemClickListener = null;

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

        public class ViewHolder extends RecyclerView.ViewHolder {
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
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dove_card, parent, false);
            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            v.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // holder.mTextView.setText(dData.get(position));
            //将position保存在itemView的Tag中，以便点击时进行获取
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

        public void setOnItemClickListener(MyAdapter.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }
    public void getBase(String base64)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte bytes[] = stream.toByteArray();
        bytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap

        //  BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
    }

}
