package com.example.frank.jyanfapa;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frank.jyanfapa.Dialog.InputDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UserProfileActivity extends AppCompatActivity implements InputDialog.MyDialogFragment_Listener{
    Spinner identity_spinner;
    String imageString = "";
    String i_spinnerIndex,s_spinnerIndex;
    EditText profilename;
    ListView listview;
    String username;
    Spinner sex_spinner;
    CircleImageView circleImageView;

    public String[][] data = {
            {"生日", "請輸入生日"},
            {"電子郵件", "請輸入電子郵件"},
            {"真實姓名", "請輸入真實姓名"},
            {"地址", "請輸入地址"},
    };

    List<Map<String, Object>> items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sdk初始化

        setContentView(R.layout.profile);


        identity_spinner = (Spinner) findViewById(R.id.profile_type);
        sex_spinner = (Spinner)findViewById(R.id.profile_sex);
        profilename = (EditText)findViewById(R.id.profile_name);
        listview = (ListView) findViewById(R.id.profile_list);
        circleImageView = (CircleImageView)findViewById(R.id.profile_imageview);

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
        ListView listview = (ListView) findViewById(R.id.profile_list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onClickListView);


        //身分下拉選單
        final ArrayAdapter<CharSequence> identityList = ArrayAdapter.createFromResource(this,
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item);
        identity_spinner.setAdapter(identityList);
        identity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_spinnerIndex = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //性別下拉選單
        final ArrayAdapter<CharSequence> sexList = ArrayAdapter.createFromResource(this,
                R.array.sex,
                android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(sexList);
        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_spinnerIndex = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    albumopen();
                }
            });
            setAllUserData();
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

    //塞入icon到toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_yes, menu);
        return true;
    }
    //點擊toolBarIcon
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save_btn:
                Log.d("儲存","確認");
                register(username);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void getDataFrom_DialogFragment(String Data01,int position) {

        Log.d("Dialog", "" + Data01 + " : " + position);
        setitemdata(Data01, position);
        String s = items.get(0).get("data").toString();
        Log.d("有沒有",s);
//        if(position == 0)
//        {
//
//        }
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

        listview.setAdapter(adapter);
    }

    private void setAllUserData() {

        SharedPreferences userData = getSharedPreferences("UserData", MODE_PRIVATE);
        username = userData.getString("User_username", "");
        String password = userData.getString("User_password", "");
        String nickname = userData.getString("User_nickname", "");
        String realname = userData.getString("User_realname", "");
        String email = userData.getString("User_email", "");
        String address = userData.getString("User_address", "");
        int sex = userData.getInt("User_sex", 0);
        String birthday = userData.getString("User_birthday", "");
        int identity = userData.getInt("User_identity", 0);
        int token = userData.getInt("User_token", 0);
        String pictureurl = userData.getString("User_picture","");


        profilename.setText(nickname);
        setitemdata(birthday,0);
        setitemdata(email,1);
        setitemdata(realname,2);
        setitemdata(address,3);


        Log.d("圖片url",pictureurl);

        if(pictureurl != "")
        {
            Picasso.with(getApplicationContext()).load(pictureurl)
                    .placeholder(R.drawable.photo).error(R.drawable.photo)
                    .into(circleImageView);
        }




        Log.d("token中的",sex+" : "+ identity);
        sex_spinner.setSelection(sex);
        identity_spinner.setSelection(identity);
    }


    public void register(String name) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/update";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("更新成功")) {
                            SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
                            settings.edit()
                                    .putString("User_nickname",profilename.getText().toString())
                                    .putString("User_realname",items.get(2).get("data").toString())
                                    .putString("User_email", items.get(1).get("data").toString())
                                    .putString("User_address", items.get(3).get("data").toString())
                                    .putInt("User_sex",(int) sex_spinner.getSelectedItemId() )
                                    .putInt("User_identity",(int)identity_spinner.getSelectedItemId())
                                    .putString("User_birthday", items.get(0).get("data").toString())
                                    .putString("User_picture","http://140.125.81.27/blog2/public/userimages/"+username+".png")
                                    .apply();
                            showtoast("更新成功");
                        }
                        else if(response.equals("未進行更新"))
                        {
                            //donothing
                        }
                        else
                        {
                            showtoast("更新失敗");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                        Log.e("VolleyError---", error.getMessage(), error);
                        byte[] htmlBodyBytes =error.networkResponse.data;  //回应的报文的包体内容
                        Log.e("VolleyError body---->", new String(htmlBodyBytes), error);
                        return;

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username",username);
                map.put("nickname",profilename.getText().toString());
                map.put("realname",items.get(2).get("data").toString());
                map.put("email",items.get(1).get("data").toString());
                map.put("birthday",items.get(0).get("data").toString());
                map.put("address",items.get(3).get("data").toString());
                map.put("identity",identity_spinner.getSelectedItemId()+"");
                map.put("sex",sex_spinner.getSelectedItemId()+"");
                map.put("picture",imageString);

                return map;
            }
        };
        mQueue.add(stringRequest);

    }
    public void showtoast(String s)
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
}