package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.isep.tasker.tasker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    private FirebaseAuth mAuth;

    private Button btnLogout;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_settings, container, false );
        mAuth = FirebaseAuth.getInstance();
        btnLogout = (Button) view.findViewById ( R.id.btnLogout );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        btnLogout.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        } );
    }

    public static Fragment newInstance() {
        SettingsFragment fragment = new SettingsFragment ( );
        return fragment;
    }
}
