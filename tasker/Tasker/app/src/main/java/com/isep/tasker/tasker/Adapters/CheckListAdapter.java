package com.isep.tasker.tasker.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.isep.tasker.tasker.Domain.Items;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/11/2017.
 */

public class CheckListAdapter extends ArrayAdapter {
    public CheckListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Items> objects) {
        super ( context, resource, objects );
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Items items = (Items) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, parent, false);
        }
        // Lookup view for data population
        CheckBox checkBox = (CheckBox) convertView.findViewById( R.id.checkBox);
        EditText text = (EditText) convertView.findViewById(R.id.textItem);

        // Populate the data into the template view using the data object
        checkBox.setActivated ( items.isChecked() );
        text.setText(items.getDescription ());
        // Return the completed view to render on screen
        return convertView;
    }




}
