package com.example.frank.jyanfapa;


import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frank.jyanfapa.Dialog.FullDialog;
import com.example.frank.jyanfapa.Dialog.TermDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity  implements TermDialog.MyDialogFragment_Listener {
    private TextInputEditText e_phone, e_password, e_passowrd2;
    private TextInputLayout l_phone, l_password, l_passowrd2;
    private Button reg_btn;
    private String spinnerIndex;

    private Long startTime;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        e_phone = (TextInputEditText) findViewById(R.id.register_username);
        e_password = (TextInputEditText) findViewById(R.id.register_password);
        e_passowrd2 = (TextInputEditText) findViewById(R.id.register_repassword);
        reg_btn = (Button) findViewById(R.id.register_register_btn);
        l_passowrd2 = (TextInputLayout) findViewById(R.id.register_repassword_layout);
        l_phone = (TextInputLayout)findViewById(R.id.register_username_layout);


        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.logoview);
        Uri uri = Uri.parse("res:/" + R.drawable.relogo);

        draweeView.setImageURI(uri);



        e_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                l_phone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkPhone();
            }
        });




        e_passowrd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                l_passowrd2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInput();
            }
        });


        //用戶條款視窗
        new TermDialog()
                .show(getFragmentManager(), "TermDialog");
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkInput();
                register();
            }
        });


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

    @Override
    public void getDataFrom_DialogFragment(int Data01) {

        Log.d("Dialog", "" + Data01);
        //不確認就關閉
        if (Data01 == 0) {
            goback();
        }
    }

    private void checkInput() {
        if (e_password.getText().toString().equals(e_passowrd2.getText().toString())) {
            l_passowrd2.setErrorEnabled(false);
        }
        else
        {
            l_passowrd2.setError("兩次密碼不相符");
        }
    }
    private void checkPhone()
    {

        Log.d("電話號碼驗證",e_phone.getText().toString().length()+"");
        if(e_phone.getText().toString().length() > 10)
        {
            l_phone.setError("電話輸入需要10碼");
        }
        else if(e_phone.getText().toString().length() < 10)
        {
            l_phone.setError("電話輸入需要10碼");
        }
        else
        {
            l_phone.setErrorEnabled(false);
        }
    }

    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    public void goback() {
        finish();
    }

    public void register() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("Register Success")) {

                            showSuccess();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    gotoLogin();
                                }}, 5000);
                        }
                        else if(response.equals("帳號重複"))
                        {
                            showtoast("註冊的帳號已被使用");
                        }
                        else
                        {
                            showtoast("註冊失敗");
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
                map.put("username", e_phone.getText().toString());
                map.put("password", e_password.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    private void showSuccess() {
        new FullDialog()
                .show(getFragmentManager(), "FullDialog");
    }
    public void showtoast(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    public void gotoLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
