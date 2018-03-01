package com.example.frank.jyanfapa.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.frank.jyanfapa.R;


/**
 * Created by Frank on 2018/2/4.
 */

public class TermDialog extends DialogFragment{

    Button button;
    CheckBox checkBox;

    private MyDialogFragment_Listener myDialogFragment_Listener;


    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment(int Data01);
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

        View view = inflater.inflate(R.layout.terms_layout, null);
        System.out.println("tag = "+getTag()); // tag which is from acitivity which started this fragment
        getDialog().setTitle("用戶條款");
         button = (Button)view.findViewById(R.id.button4);
         checkBox = (CheckBox)view.findViewById(R.id.checkBox2);
         getDialog().setCanceledOnTouchOutside(false);



         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d("Dialog","下一步");
                 if (checkBox.isChecked())
                 {
                     Log.d("Dialog","確認完畢");
                     myDialogFragment_Listener.getDataFrom_DialogFragment(1);
                     dismiss();
                 }
                 else
                 {
                     Log.d("Dialog","未確認");
                         myDialogFragment_Listener.getDataFrom_DialogFragment(0);

                     dismiss();
                 }

             }
         });


        return view;
    }

}
