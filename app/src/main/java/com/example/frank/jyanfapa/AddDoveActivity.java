package com.example.frank.jyanfapa;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.frank.jyanfapa.Dialog.InputDialog;
import com.example.frank.jyanfapa.SQLite.GroupsDAO;
import com.example.frank.jyanfapa.SQLite.PigeonDAO;
import com.example.frank.jyanfapa.entities.Pigeon;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDoveActivity extends AppCompatActivity implements InputDialog.MyDialogFragment_Listener{
    GroupsDAO groupsDAO;
    PigeonDAO pigeonDAO;
    ImageButton imageButton;
    String imageString="";
    EditText addname,addcircle;
    ListView listView;

    Button button;

    public String[][] data = {
            {"生日", "請輸入生日"},
            {"羽色", "請輸入羽色"},
            {"性別", "請輸入性別"},
            {"眼砂", "請輸入眼砂"},
            {"來源", "請輸入來源"},
            {"群組", "請輸入群組"},
            {"父親", "請輸入父親"},
            {"母親", "請輸入母親"},
    };

    List<Map<String, Object>> items;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dove);

        groupsDAO = new GroupsDAO(getApplicationContext());
        pigeonDAO = new PigeonDAO(getApplicationContext());

        addname = (EditText)findViewById(R.id.new_dove_name);
        addcircle = (EditText)findViewById(R.id.new_dove_id);
        circleImageView = (CircleImageView)findViewById(R.id.new_dove_picture);


        //設定toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層


        //取得資料 請將他替換成個人的資料
        items = new ArrayList<Map<String,Object>>();
        for (int i=0;i < data.length;i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", data[i][0]);
            item.put("data", data[i][1]);
            items.add(item);
        }

        //帶入對應資料
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                items,
                R.layout.list_item_2line,
                new String[]{"title", "data"},
                new int[]{R.id.line1, R.id.line2}
        );
        listView = (ListView)findViewById(R.id.new_dove_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListView);



        if (groupsDAO.getCount() == 0) {
            groupsDAO.sample();
        }
        if(pigeonDAO.getCount() == 0)
        {
            pigeonDAO.sample();
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("做不完","debug");
                albumopen();
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // Pigeon pigeon = new Pigeon(pigeonDAO.getCount()+1,addcolor.getText().toString(),addfeather.getText().toString(),addbirth.getText().toString(),addname.getText().toString(),imageString,addcircle.getText().toString(),"-","-",0,"",0,0,0);
////                 Pigeon item = new Pigeon(0,"藍色","雞翅","9月","焦阿巴","沒有圖","3345678","撿來的","小鴿",1,"",0,0,0);
//                //pigeonDAO.insert(pigeon);
//
//                showToast("新增成功");
//                Intent intent1 = new Intent(AddDoveActivity.this,PigeonViewActivity.class);
//                startActivity(intent1);
//            }
//        });
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                albumopen();
//            }
//        });


    }
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //到時候用來打開dialog的ＵＩ用


            InputDialog dialogFragment = new InputDialog();


            Bundle bundle = new Bundle();
            bundle.putString("TEXT",data[position][0]);
            bundle.putInt("POSITION",position);

            dialogFragment.setArguments(bundle);
            //輸入視窗
            dialogFragment.show(getFragmentManager(), "InputDialog");

        }

    };

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

                BitmapFactory.Options mOptions = new BitmapFactory.Options();
//Size=2為將原始圖片縮小1/2，Size=4為1/4，以此類推
                mOptions.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,mOptions);


                int oldwidth = photo.getWidth();
                int oldheight = photo.getHeight();
                float scaleWidth = 50 / (float) oldwidth;
                float scaleHeight = 50 / (float) oldheight;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // create the new Bitmap object

                Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0, oldwidth,
                        oldheight, matrix, true);
                setBase(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    public void setBase(Bitmap bmp)
    {
        // 先把 bitmpa 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream );
        byte bytes[] = stream.toByteArray();
        // Android 2.2以上才有內建Base64，其他要自已找Libary或是用Blob存入SQLite
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP); // 把byte變成base64
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        String encodeString = new String(encode);
        Log.d("base64",base64);
        Log.d("base642",encodeString);
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
        circleImageView.setImageBitmap(bmp);

    }


    @Override
    public void getDataFrom_DialogFragment(String Data01,int position) {

        Log.d("Dialog", "" + Data01 + " : " + position);
        setitemdata(Data01, position);
        String s = items.get(0).get("data").toString();
        Log.d("有沒有",s);
    }
    private void setitemdata(String Data01,int position)
    {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("title",data[position][0]);
        item.put("data", Data01);
        items.set(position,item);
        //帶入對應資料
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                items,
                R.layout.list_item_2line,
                new String[]{"title", "data"},
                new int[]{R.id.line1, R.id.line2}
        );

        listView.setAdapter(adapter);
    }
    //塞入icon到toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_yes, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save_btn:
                Log.d("儲存","確認");
                //register(username);
//                Pigeon pigeon = new Pigeon(pigeonDAO.getCount()+1,);
//                Pigeon pigeon1 = new Pigeon(pigeonDAO.getCount()+1,items.get(2).get("data").toString(),)
                int group,father,mother;
                if(items.get(5).get("data").toString().isEmpty() || items.get(5).get("data").toString().equals("請輸入群組"))
                {
                    group =0;
                }
                else
                {
                    group = Integer.valueOf(items.get(5).get("data").toString());
                }
                if(items.get(6).get("data").toString().isEmpty() || items.get(6).get("data").toString().equals("請輸入父親"))
                {
                    father =0;
                }
                else
                {
                    father = Integer.valueOf(items.get(6).get("data").toString());
                }
                if(items.get(7).get("data").toString().isEmpty() || items.get(7).get("data").toString().equals("請輸入母親"))
                {
                    mother =0;
                }
                else
                {
                   mother = Integer.valueOf(items.get(7).get("data").toString());
                }


                Pigeon pigeon1 = new Pigeon(pigeonDAO.getCount()+1,items.get(3).get("data").toString(),items.get(1).get("data").toString(),items.get(0).get("data").toString(),addname.getText().toString(),imageString,addcircle.getText().toString(),items.get(4).get("data").toString(),"-",0,"",father,mother,0);
//                 Pigeon item = new Pigeon(0,"藍色","雞翅","9月","焦阿巴","沒有圖","3345678","撿來的","小鴿",1,"",0,0,0);
                pigeonDAO.insert(pigeon1);

                showToast("新增成功");
//                Intent intent1 = new Intent(AddDoveActivity.this,PigeonViewActivity.class);
//                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
