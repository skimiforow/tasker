package com.isep.tasker.tasker.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;

/**
 * Created by skimiforow on 09/12/2017.
 */

public class ArrayItemAdapter<T> extends ArrayAdapter<T> {

    public ArrayItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<T> objects) {
        super ( context, resource, objects );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Object item = getItem ( position );
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from ( getContext ( ) ).inflate ( R.layout.item_layout, parent, false );
        }

        if (item instanceof Note) {

            // Lookup view for data population
            ImageView logo = convertView.findViewById ( R.id.logo_type );
            TextView title = convertView.findViewById ( R.id.title );
            TextView description = convertView.findViewById ( R.id.description );
            ImageView importance = convertView.findViewById ( R.id.important );
            ImageView reminder = convertView.findViewById ( R.id.reminder );

            logo.setImageResource ( R.drawable.ic_note_outline_white_48dp );
            title.setText ( ((Note) item).getTitle ( ) );
            description.setText ( ((Note) item).getDescription ( ) );

            if (((Note) item).getPriority ( ) == Priority.HIGH) {
                importance.setVisibility ( View.VISIBLE );
            } else {
                importance.setVisibility ( View.INVISIBLE );
            }

            if (((Note) item).getReminder ( ) != null) {
                reminder.setVisibility ( View.VISIBLE );
            } else {
                reminder.setVisibility ( View.INVISIBLE );
            }

        } else if (item instanceof Reminder) {

            // Lookup view for data population
            ImageView logo = convertView.findViewById ( R.id.logo_type );
            TextView title = convertView.findViewById ( R.id.title );
            TextView description = convertView.findViewById ( R.id.description );
            ImageView importance = convertView.findViewById ( R.id.important );
            ImageView reminder = convertView.findViewById ( R.id.reminder );

            logo.setImageResource ( R.drawable.ic_alarm_white_48dp );
            title.setText ( ((Reminder) item).getTitle ( ) );
            description.setText ( ((Reminder) item).getDescription ( ) );

            if (((Reminder) item).getPriority ( ) == Priority.HIGH) {
                importance.setVisibility ( View.VISIBLE );
            } else {
                importance.setVisibility ( View.INVISIBLE );
            }

            if (((Reminder) item).getReminder ( ) != null) {
                reminder.setVisibility ( View.VISIBLE );
            } else {
                reminder.setVisibility ( View.INVISIBLE );
            }

        }

        return convertView;
    }

    public void refresh() {
        notifyDataSetChanged ( );
    }
}
