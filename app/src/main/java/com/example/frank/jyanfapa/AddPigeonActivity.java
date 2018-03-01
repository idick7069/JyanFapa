package com.example.frank.jyanfapa;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.frank.jyanfapa.SQLite.GroupsDAO;
import com.example.frank.jyanfapa.SQLite.PigeonDAO;
import com.example.frank.jyanfapa.entities.Pigeon;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class AddPigeonActivity extends AppCompatActivity {
    GroupsDAO groupsDAO;
    PigeonDAO pigeonDAO;
    ImageButton imageButton;
    String imageString="";


    TextInputEditText addname,addbirth,addcircle,addcolor,addfeather;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pigeon);

        groupsDAO = new GroupsDAO(getApplicationContext());
        pigeonDAO = new PigeonDAO(getApplicationContext());
        addname = (TextInputEditText)findViewById(R.id.addname);
        addbirth= (TextInputEditText)findViewById(R.id.addbirth);
        addcircle = (TextInputEditText)findViewById(R.id.addcircle);
        addcolor = (TextInputEditText)findViewById(R.id.addcolor);
        addfeather = (TextInputEditText)findViewById(R.id.addfeather);
        button = (Button)findViewById(R.id.addP);
        imageButton = (ImageButton)findViewById(R.id.imagebutton);

        if (groupsDAO.getCount() == 0) {
            groupsDAO.sample();
        }
        if(pigeonDAO.getCount() == 0)
        {
            pigeonDAO.sample();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pigeon pigeon = new Pigeon(pigeonDAO.getCount()+1,addcolor.getText().toString(),addfeather.getText().toString(),addbirth.getText().toString(),addname.getText().toString(),imageString,addcircle.getText().toString(),"-","-",0,"",0,0,0);
//                 Pigeon item = new Pigeon(0,"藍色","雞翅","9月","焦阿巴","沒有圖","3345678","撿來的","小鴿",1,"",0,0,0);
                pigeonDAO.insert(pigeon);

                showToast("新增成功");
                Intent intent1 = new Intent(AddPigeonActivity.this,PigeonViewActivity.class);
                startActivity(intent1);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumopen();
            }
        });


    }

    private void showToast(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    public void albumopen()
    {
        //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap photo = BitmapFactory.decodeStream(cr.openInputStream(uri));
                int oldwidth = photo.getWidth();
                int oldheight = photo.getHeight();
                float scaleWidth = 100 / (float) oldwidth;
                float scaleHeight = 100 / (float) oldheight;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // create the new Bitmap object

                Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0, oldwidth,
                        oldheight, matrix, true);
                setBase(resizedBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//
//                int oldwidth = photo.getWidth();
//                int oldheight = photo.getHeight();
//                float scaleWidth = 100 / (float) oldwidth;
//                float scaleHeight = 100 / (float) oldheight;
//                Matrix matrix = new Matrix();
//                matrix.postScale(scaleWidth, scaleHeight);
//                // create the new Bitmap object
//
//                Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0, oldwidth,
//                        oldheight, matrix, true);
//                setBase(resizedBitmap);
//            }
        }

    }
    public void setBase(Bitmap bmp)
    {
        // 先把 bitmpa 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream );
        byte bytes[] = stream.toByteArray();
        // Android 2.2以上才有內建Base64，其他要自已找Libary或是用Blob存入SQLite
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT); // 把byte變成base64
        Log.d("base64",base64);
        imageString = base64;
        getBase(base64);
    }
    private void getBase(String base64)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte bytes[] = stream.toByteArray();
        bytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap

     //  BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
        imageButton.setImageBitmap(bmp);
    }
}
