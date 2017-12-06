package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {

    private EditText mName;
    private EditText mDescriptiom;
    private Button mBtnSubmit;


    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_add_note, container, false);

        mName = mView.findViewById ( R.id.etName );
        mDescriptiom = mView.findViewById ( R.id.etDescriptio );
        mBtnSubmit = mView.findViewById ( R.id.btnSubmit );

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        mBtnSubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if(mName.getText ().toString().matches ( "" )){
                    mName.requestFocus ();
                    mName.setError ( getString ( R.string.is_mandatory  ));
                }
            }
        } );
    }
}
