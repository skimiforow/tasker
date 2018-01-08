package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isep.tasker.tasker.Adapters.TabAdapter;
import com.isep.tasker.tasker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewItemFragment extends Fragment {


    public AddNewItemFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        AddNewItemFragment fragment = new AddNewItemFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);

        String type = "";


        // Setting ViewPager for each Tabs
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }
        setupViewPager(viewPager, type);

        // Set Tabs inside Toolbar
        TabLayout tabs = view.findViewById(R.id.add_tab);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager, String type) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        if (type == "Note") {
            AddNoteFragment addNoteFragment = new AddNoteFragment();
            addNoteFragment.setArguments(getArguments());
            adapter.addFragment(addNoteFragment, getString(R.string.add_note));
            SettingFragment settingFragment = new SettingFragment();
            settingFragment.setArguments(getArguments());
            adapter.addFragment(settingFragment, getString(R.string.action_settings));
        } else if (type == "Reminder") {
            adapter.addFragment(new AddReminderFragment(), getString(R.string.add_reminder));
            adapter.addFragment(new SettingFragment(), getString(R.string.action_settings));
        } else if (type == "Checklist") {
            adapter.addFragment(new AddChecklistFragment(), getString(R.string.add_checklist));
            //adapter.addFragment(new SettingFragment(), getString(R.string.action_settings));
        }

        viewPager.setAdapter(adapter);
    }
}
