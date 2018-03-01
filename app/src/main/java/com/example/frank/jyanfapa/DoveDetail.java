package com.example.frank.jyanfapa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frank.jyanfapa.SQLite.PigeonDAO;
import com.example.frank.jyanfapa.entities.Pigeon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Frank on 2018/3/1.
 */

public class DoveDetail extends AppCompatActivity {

    int position;
    PigeonDAO pigeonDAO;
    CircleImageView d_picture,d_fatherP,d_motherP;
    TextView d_name,d_circle,d_group,d_eye,d_feather,d_birth,d_source,d_ps,d_father,d_mother,d_father_id,d_mother_id;
    LinearLayout father_Layout,mother_Layout;

    Pigeon dove;
    int doveFather,doveMother;
    RecyclerView dList;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("選取",position+"");
        dove = pigeonDAO.get(position+1);


        doveFather = dove.getFather();
        doveMother = dove.getMother();

        Log.d("dove爸",doveFather+"");
        Log.d("dove媽",doveMother+"");

        setdove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doveprofile);

        //設定toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層


        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");


        pigeonDAO = new PigeonDAO(getApplicationContext());
        d_name = (TextView)findViewById(R.id.dove_detail_name);
        d_circle = (TextView)findViewById(R.id.dove_detail_circle);
        d_group = (TextView)findViewById(R.id.dove_detail_group);
        d_eye = (TextView)findViewById(R.id.dove_detail_eye);
        d_feather = (TextView)findViewById(R.id.dove_detail_feather);
        d_birth = (TextView)findViewById(R.id.dove_detail_birth);
        d_source = (TextView)findViewById(R.id.dove_detail_source);
        d_ps = (TextView)findViewById(R.id.dove_detail_ps);
        d_father = (TextView)findViewById(R.id.dove_detail_father);
        d_father_id = (TextView)findViewById(R.id.dove_detail_father_id);
        d_mother = (TextView)findViewById(R.id.dove_detail_mother);
        d_mother_id = (TextView)findViewById(R.id.dove_detail_mother_id);

        d_picture = (CircleImageView)findViewById(R.id.dove_detail_picture);
        d_fatherP =(CircleImageView)findViewById(R.id.dove_detail_father_picture);
        d_motherP =(CircleImageView)findViewById(R.id.dove_detail_mother_picture);

        father_Layout = (LinearLayout)findViewById(R.id.dove_detail_father_layout);
        mother_Layout = (LinearLayout)findViewById(R.id.dove_detail_mother_layout);

        father_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("選擇父親","select");
                toParents( pigeonDAO.get(position+1).getId(),0);
            }
        });
        mother_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("選擇母親","select");
                toParents( pigeonDAO.get(position+1).getId(),1);
            }
        });

        Log.d("選取",position+"");
        dove = pigeonDAO.get(position+1);


        doveFather = dove.getFather();
        doveMother = dove.getMother();

        Log.d("dove爸",doveFather+"");
        Log.d("dove媽",doveMother+"");

        setdove();

    }
    private void toParents(int position,int choose)
    {
        Intent intent = new Intent(this,CardView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putInt("choose",choose);
        //將Bundle物件傳給intent
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
//                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private  void setdove() {
        d_name.setText(dove.getName().toString());
        d_circle.setText(dove.getCircle().toString());
        d_group.setText(dove.getType());
        d_eye.setText(dove.getEye());
        d_feather.setText(dove.getFeather());
        d_birth.setText(dove.getBirth());
        d_source.setText(dove.getSource());
        d_ps.setText(dove.getPs());
        //d_father_id.setText();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte bytes[] = stream.toByteArray();

      //  Log.d("測個",pigeonDAO.get(doveFather).getPhoto()+"");
        if (dove.getPhoto() != "")
        {
            bytes = Base64.decode(dove.getPhoto(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap
            d_picture.setImageBitmap(bmp);
        }
        if(doveFather !=0)
        {
            if(pigeonDAO.get(doveFather).getPhoto()!= "")
            {
                bytes = Base64.decode(pigeonDAO.get(doveFather).getPhoto(), Base64.DEFAULT);
                Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap
                d_fatherP.setImageBitmap(bmp2);
            }
        }
        if(doveMother !=0)
        {
            Log.d("媽",pigeonDAO.get(doveMother).getPhoto()+"");
            if(pigeonDAO.get(doveMother).getPhoto()!= "")
            {
                bytes = Base64.decode(pigeonDAO.get(doveMother).getPhoto(), Base64.DEFAULT);
                Bitmap bmp3 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap
                d_motherP.setImageBitmap(bmp3);
            }
        }




        Log.d("dovemother",doveMother+""); //0
        Log.d("dovefather",doveFather+""); //2

        if(doveMother != 0)
        {
            d_mother.setText(pigeonDAO.get(doveMother).getName());
            d_mother_id.setText(pigeonDAO.get(doveMother).getCircle());
        }
        if(doveFather!=0)
        {
            d_father.setText(pigeonDAO.get(doveFather).getName());

            d_father_id.setText(pigeonDAO.get(doveFather).getCircle());
        }




    }
}