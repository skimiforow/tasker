package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isep.tasker.tasker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSettingsFragment extends Fragment {


    public AccountSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    public static Fragment newInstance() {
        AccountSettingsFragment fragment = new AccountSettingsFragment();

        return fragment;
    }

}
