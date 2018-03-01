package com.example.frank.jyanfapa;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frank.jyanfapa.Dialog.InputDialog;
import com.example.frank.jyanfapa.Dialog.TermDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class LoftActivity extends AppCompatActivity implements OnMapReadyCallback {
    public class LoftActivity extends AppCompatActivity implements InputDialog.MyDialogFragment_Listener,OnMapReadyCallback{
    EditText edlong, edlati, edloftname, edaddress;
    //MapView mapView;
    private GoogleMap mMap;

    String geocoderurl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    String addressurl = "";
    String keybefore = "&key=";
    String key = "AIzaSyAsCIj5z_S4ep-pYaX9d5FbEg5_uUYe-K8";
    ListView loftlistview;


    public String[][] locationData = {
            {"鴿舍名稱","請輸入鴿舍名稱"},
            {"鴿舍地址","請輸入鴿舍地址"},
            {"緯度","請輸入緯度"},
            {"經度","請輸入經度"},
    };
    List<Map<String, Object>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
//        edloftname = (EditText) findViewById(R.id.loftname);
//        edaddress = (EditText) findViewById(R.id.loftaddress);
//        edlong = (EditText) findViewById(R.id.edlong);
//        edlati = (EditText) findViewById(R.id.edlati);
//        autobutton = (Button) findViewById(R.id.autoBtn);
//        loftBtn = (Button) findViewById(R.id.loftBtn);
        //mapView = (MapView) findViewById(R.id.mapView);




        //設定toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層


        //取得資料 請將他替換成地點的資料
        items = new ArrayList<Map<String,Object>>();
        for (int i=0;i < locationData.length;i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", locationData[i][0]);
            item.put("data", locationData[i][1]);
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


        loftlistview = (ListView) findViewById(R.id.loft_listview);
        loftlistview.setAdapter(adapter);
        loftlistview.setOnItemClickListener(onClickListView);








        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.loft_mapview);
        mapFragment.getMapAsync(this);
//
//
//        autobutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String enToStr = Base64.encodeToString(edaddress.getText().toString() .getBytes(), Base64.DEFAULT);
//                try {
//                    String encodestring = URLEncoder.encode(edaddress.getText().toString(), "UTF-8");
//                    getlonglati(geocoderurl + encodestring + keybefore + key);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
////                getlonglati(geocoderurl + enToStr + keybefore + key);
//            }
//        });
//        loftBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loftUpdate();
//            }
//        });
//
////        getLoftData();
        loftGet();
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
                loftUpdate();
               // register(username);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //到時候用來打開dialog的ＵＩ用


            InputDialog dialogFragment = new InputDialog();


            Bundle bundle = new Bundle();
            bundle.putString("TEXT",locationData[position][0]);
            bundle.putInt("POSITION",position);

            dialogFragment.setArguments(bundle);
            //輸入視窗
            dialogFragment.show(getFragmentManager(), "InputDialog");

        }

    };

    @Override
    public void getDataFrom_DialogFragment(String Data01,int position) {

        Log.d("Dialog", "" + Data01 + " : " + position);
        setitemdata(Data01, position);
        String s = items.get(0).get("data").toString();
        Log.d("有沒有",s);
        if(position == 1 && Data01 != null)
        {
                try {
                    String encodestring = URLEncoder.encode(Data01, "UTF-8");
                    getlonglati(geocoderurl + encodestring + keybefore + key);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
        }
    }


    private void setitemdata(String Data01,int position)
    {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("title",locationData[position][0]);
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

        loftlistview.setAdapter(adapter);
    }


    public void getlonglati(String url) {
        Log.d("經緯sent ", url);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest getRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("回傳json ", s);

                        jsonAnalysis(s);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        mQueue.add(getRequest);
    }

    public void jsonAnalysis(String s) {
        //解析 googlemap api  Object > array > Object > Object

        //最外層
        JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();

        //進到results的第一筆
        JsonElement jsonElement = jsonObject.getAsJsonArray("results").get(0);

        Log.d("json測試2", jsonElement.toString());

        //轉object
        JsonObject jsonObject1 = jsonElement.getAsJsonObject();

        Log.d("json測試3", jsonObject1.toString());

        //進到geometry
        JsonObject jsonObject2 = jsonObject1.getAsJsonObject("geometry");

        Log.d("json測試4", jsonObject2.toString());

        //進到location
        JsonObject jsonObject3 = jsonObject2.getAsJsonObject("location");

        Log.d("json測試5", jsonObject3.toString());


        Double lat = jsonObject3.get("lat").getAsDouble();
        Double lng = jsonObject3.get("lng").getAsDouble();

        Log.d("json測試6", "經度:" + lat + " 緯度:" + lng);

        setitemdata(lat+"",2);
        setitemdata(lng+"",3);
        setUpMap(items.get(0).get("data").toString(),lat,lng,items.get(1).get("data").toString());
    }



    public void loftUpdate() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/loft";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("鴿舍更新成功")) {
                            showtoast("鴿舍更新成功");
                            storeData();
                            //設定地圖
                            //setUpMap(edloftname.getText().toString(),Double.parseDouble(edlati.getText().toString()),Double.parseDouble(edlong.getText().toString()),edaddress.getText().toString());
                            double lati = Double.parseDouble(items.get(2).get("data").toString());
                            double lng = Double.parseDouble(items.get(3).get("data").toString());
                            setUpMap(items.get(0).get("data").toString(),lati,lng,items.get(1).get("data").toString());
                            finish();
                        } else {
                            showtoast("鴿舍更新失敗");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", getUsername());
                map.put("loftname", items.get(0).get("data").toString());
                map.put("address", items.get(1).get("data").toString());
                map.put("latitude", items.get(2).get("data").toString());
                map.put("longitude", items.get(3).get("data").toString());
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    public String getUsername() {
        SharedPreferences userData = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = userData.getString("User_username", "");
        Log.d("Loftsent", username);
        return username;
    }

    public void showtoast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void storeData() {
        SharedPreferences settings = getSharedPreferences("LoftData", MODE_PRIVATE);
        settings.edit()
                .putString("Loft_name", items.get(0).get("data").toString())
                .putString("Loft_address",items.get(1).get("data").toString())
                .putString("Loft_latitude",items.get(2).get("data").toString())
                .putString("Loft_longitude", items.get(3).get("data").toString())
                .apply();
    }

    public void loftGet() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/loftGet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("無資料")) {
                            showtoast("尚未建立鴿舍資訊");
                        } else {
                            loftJson(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", getUsername());
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    public void loftJson(String s) {

        JsonArray jsonArray = new JsonParser().parse(s).getAsJsonArray();
        JsonElement jsonElement = jsonArray.get(0);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
        Log.d("jsonloft", jsonObject.toString());

        Log.d("loftjson",jsonObject.get("latitude").toString());

        setitemdata(jsonObject.get("loftname").getAsString(),0);
        setitemdata(jsonObject.get("address").getAsString(),1);
        setitemdata(jsonObject.get("latitude").getAsString(),2);
        setitemdata(jsonObject.get("longitude").getAsString(),3);

        setUpMap(jsonObject.get("loftname").getAsString(),jsonObject.get("latitude").getAsDouble(),jsonObject.get("longitude").getAsDouble(),jsonObject.get("address").getAsString());
    }

    @Override
    public void onMapReady(GoogleMap map) {
          mMap = map;
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void setUpMap(String title,double la,double lo,String address) {
        // 刪除原來預設的內容
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // 建立位置的座標物件
        LatLng place = new LatLng(la, lo);
        // 移動地圖
        moveMap(place);

        // 加入地圖標記
        addMarker(place, title,address);
    }
    // 在地圖加入指定位置與標題的標記
    private void addMarker(LatLng place, String title,String snippet) {


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(snippet);

        mMap.addMarker(markerOptions);
    }
    // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
