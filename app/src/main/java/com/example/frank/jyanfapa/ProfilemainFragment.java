package com.example.frank.jyanfapa;


import android.app.Activity;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Frank on 2018/2/28.
 */

public class ProfilemainFragment extends Fragment implements OnMapReadyCallback{
    TextView nickname,phone,type,birthday,sex,email,realname,address,loftname,lati,lng,loftaddress;
    //MapView mapView;
    private GoogleMap mMap;
    private View rootView;
    MapView mMapView;
    private FragmentActivity myContext;
    private android.support.v7.widget.CardView cardView,userCardView;
    CircleImageView circleImageView;

    @Override
    public void onResume() {
        super.onResume();
        loftGet();
        setAllUserData();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            setAllUserData();
        } else {
            //相当于Fragment的onPause
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profilemain,container,false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
      //  setUpMapIfNeeded();
//        FragmentManager manager = getFragmentManager();
//        Fragment fragment = manager.findFragmentById(R.id.profilemain_mapview);

//        FragmentManager fragManager = myContext.getFragmentManager();

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.profilemain_mapview);
//        mapFragment.getMapAsync(this);

        nickname = (TextView)view.findViewById(R.id.profilemain_name);
        phone =(TextView)view.findViewById(R.id.profilemain_username);
        type = (TextView)view.findViewById(R.id.profilemain_type);
        birthday = (TextView)view.findViewById(R.id.profilemain_birth);
        sex = (TextView)view.findViewById(R.id.profilemain_sex);
        email = (TextView)view.findViewById(R.id.profilemain_email);
        realname =  (TextView)view.findViewById(R.id.profilemain_realname);
        address  = (TextView)view.findViewById(R.id.profilemain_address);
        loftname = (TextView)view.findViewById(R.id.profilemain_loftname);
        lati  = (TextView)view.findViewById(R.id.profilemain_lati);
        lng  = (TextView)view.findViewById(R.id.profilemain_long);
        cardView = (CardView)view.findViewById(R.id.profilemain_loftlayout);
        userCardView = (CardView)view.findViewById(R.id.profilemain_userlayout);
        circleImageView = (CircleImageView)view.findViewById(R.id.profilemain_picture);
        loftaddress = (TextView)view.findViewById(R.id.profilemain_loftaddress);

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoftActivity.class);
                startActivity(intent);
            }
        });
        userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UserProfileActivity.class);
                startActivity(intent);
            }
        });



        setAllUserData();
    }

    private void setAllUserData() {

        SharedPreferences userData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String g_username = userData.getString("User_username", "");
        String g_password = userData.getString("User_password", "");
        String g_nickname = userData.getString("User_nickname", "");
        String g_realname = userData.getString("User_realname", "");
        String g_email = userData.getString("User_email", "");
        String g_address = userData.getString("User_address", "");
        int g_sex = userData.getInt("User_sex", 0);
        String g_birthday = userData.getString("User_birthday", "");
        int g_identity = userData.getInt("User_identity", 0);
        int g_token = userData.getInt("User_token", 0);
        String picture = userData.getString("User_picture","");
        Log.d("圖片url",picture);

        if(picture!= "")
        {
            Picasso.with(getApplicationContext()).load(picture)
                    .placeholder(R.drawable.photo).error(R.drawable.photo)
                    .into(circleImageView);
        }




        SharedPreferences loftData = getActivity().getSharedPreferences("LoftData",MODE_PRIVATE);
        String g_loftname = loftData.getString("Loft_name","");
        String g_loftlong = loftData.getString("Loft_longitude","");
        String g_loftlati = loftData.getString("Loft_latitude","");
        String g_loftaddress = loftData.getString("Loft_address","");

        nickname.setText(g_nickname);
        phone.setText(g_username);
        switch (g_identity)
        {
            case 0:
                type.setText("種鴿場");
                break;
            case 1:
                type.setText("作出者");
                break;
            case 2:
                type.setText("教練");
                break;
            case 3:
                type.setText("鴿友");
                break;
            default:
                type.setText("尚未輸入");
        }
        birthday.setText(g_birthday);
        if(g_sex == 1)
        {
            sex.setText("男");
        }
        else if(g_sex == 2)
        {
            sex.setText("女");
        }
        else
        {
            sex.setText("尚未輸入");
        }
        email.setText(g_email);
        realname.setText(g_realname);
        address.setText(g_address);
//        loftname.setText(g_loftname);
//        lati.setText(g_loftlati);
//        lng.setText(g_loftlong);

        loftGet();

        if(!g_loftlati.equals("")  && !g_loftlong.equals(""))
        {
            double lati = Double.parseDouble(g_loftlati);
            double lng = Double.parseDouble(g_loftlong);
            //setUpMap(g_loftname,lati,lng,"");
        }





//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        byte bytes[] = stream.toByteArray();
//        bytes = Base64.decode(picture, Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap
//
//        //  BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
//        circleImageView.setImageBitmap(bmp);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
    public void loftGet() {
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
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

//        setitemdata(jsonObject.get("loftname").getAsString(),0);
//        setitemdata(jsonObject.get("address").getAsString(),1);
//        setitemdata(jsonObject.get("latitude").getAsString(),2);
//        setitemdata(jsonObject.get("longitude").getAsString(),3);

        loftname.setText("鴿舍名: "+jsonObject.get("loftname").getAsString());
        lati.setText("緯度: "+jsonObject.get("latitude").getAsString());
        lng.setText("經度: "+jsonObject.get("longitude").getAsString());
        loftaddress.setText("鴿舍地址: "+jsonObject.get("address").getAsString());

    }
    public String getUsername() {
        SharedPreferences userData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String username = userData.getString("User_username", "");
        Log.d("Loftsent", username);
        return username;
    }

    public void showtoast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}