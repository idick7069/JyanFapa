package com.example.frank.jyanfapa.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frank.jyanfapa.R;

/**
 * Created by Frank on 2018/2/25.
 */

public class InputDialog extends DialogFragment implements android.text.TextWatcher{
    Button btn;
    EditText editText;
    String name;
    String select="";
    int position;

    private MyDialogFragment_Listener myDialogFragment_Listener;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment(String Data01, int postion);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            myDialogFragment_Listener = (MyDialogFragment_Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditNameDialogListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.input_layout, null);
        System.out.println("tag = "+getTag()); // tag which is from acitivity which started this fragment

        select = getArguments().getString("TEXT","");
        position = getArguments().getInt("POSITION",0);

        Log.d("bundle",select + ":"+position);
        this.getDialog().setTitle(select+"");
        btn = (Button)view.findViewById(R.id.input_layout_btn);
        editText = (EditText)view.findViewById(R.id.input_layout_text);

        if(select.equals("生日"))
        {
            editText.addTextChangedListener(this);
            editText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            Log.d("是不是生日","是");


        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(select.equals("生日"))
                {
                    String pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
                    String str = editText.getText().toString();

                    if(str.matches(pattern)){
                        //doSomething
                        Log.d("Dialog","生日格式確認完畢");
                        myDialogFragment_Listener.getDataFrom_DialogFragment(editText.getText().toString(),position);
                        dismiss();
                    }
                    else
                    {
                        toast();
                    }

                }
                else
                {
                    Log.d("Dialog","確認完畢");
                    myDialogFragment_Listener.getDataFrom_DialogFragment(editText.getText().toString(),position);
                    dismiss();
                }

            }
        });


        return view;
    }
    private void toast()
    {
        Toast.makeText(getActivity(),"請輸入格式為 yyyy-mm-dd",Toast.LENGTH_SHORT).show();
    }

}
