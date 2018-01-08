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
    public class ListArchivedItemsFragment extends Fragment {


        public ListArchivedItemsFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_list_archived_items, container, false);
        }

        public static Fragment newInstance() {
            ListArchivedItemsFragment fragment = new ListArchivedItemsFragment();

            return fragment;
        }
    }


