package com.isep.tasker.tasker.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteSettingsFragment extends Fragment {


    private Spinner spnPriority;
    private EditText dateText;
    private EditText timeText;
    private EditText autoLocationText;
    private EditText autoUserText;
    private ListView lstViewLocations;
    private ListView lstViewUser;
    private Switch switchReminder;
    private Switch switchUser;
    private Button btnAddUSer;
    private Button btnAddLocation;
    private Button btnSubmit;


    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;


    public NoteSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_note_settings, container, false);

        spnPriority = (Spinner)mView.findViewById(R.id.spnImportance);
        ArrayAdapter<Priority> spinnerAdapter = new ArrayAdapter<Priority> ( getContext (),R.layout.spinner_full,Priority.values () );
        spnPriority.setAdapter(spinnerAdapter);
        spnPriority.setSelection ( 1,true );

        dateText = (EditText) mView.findViewById ( R.id.etDate );
        timeText = (EditText) mView.findViewById ( R.id.etTime );
        autoLocationText = (EditText) mView.findViewById ( R.id.adressAutoLocation ) ;
        autoUserText = (EditText) mView.findViewById ( R.id.userAutoLocation );
        lstViewLocations = (ListView) mView.findViewById ( R.id.lstLocations );
        lstViewUser = (ListView) mView.findViewById ( R.id.lstUsers );
        switchReminder = (Switch) mView.findViewById ( R.id.switchReminder );
        switchUser = (Switch) mView.findViewById ( R.id.switchUser );
        btnAddLocation = (Button) mView.findViewById ( R.id.btnAddLocation );
        btnAddUSer = (Button) mView.findViewById ( R.id.btnAddUser );

        myCalendar = Calendar.getInstance();



        return mView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        final View mView = view;
        switchReminder.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mView.findViewById ( R.id.reminderDates ).setVisibility ( View.VISIBLE );
                    mView.findViewById ( R.id.adressAutoLocation ).setVisibility ( View.VISIBLE );
                    mView.findViewById ( R.id.btnAddLocation ).setVisibility ( View.VISIBLE );
                    mView.findViewById ( R.id.lstLocationView ).setVisibility ( View.VISIBLE );
                } else {
                    mView.findViewById ( R.id.reminderDates ).setVisibility ( View.GONE );
                    mView.findViewById ( R.id.adressAutoLocation ).setVisibility ( View.GONE );
                    mView.findViewById ( R.id.btnAddLocation ).setVisibility ( View.GONE );
                    mView.findViewById ( R.id.lstLocationView ).setVisibility ( View.GONE );
                }
            }});
        switchUser.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mView.findViewById ( R.id.sharing1 ).setVisibility ( View.VISIBLE );
                    mView.findViewById ( R.id.sharing2 ).setVisibility ( View.VISIBLE );
                    mView.findViewById ( R.id.sharing3 ).setVisibility ( View.VISIBLE );
                } else {
                    mView.findViewById ( R.id.sharing1 ).setVisibility ( View.GONE );
                    mView.findViewById ( R.id.sharing2 ).setVisibility ( View.GONE );
                    mView.findViewById ( R.id.sharing3 ).setVisibility ( View.GONE );
                }
            }
        } );

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateText.getText ().toString ();
                new DatePickerDialog(getActivity(), NoteSettingsFragment.this.date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

    }

    private void updateDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateText.setText(sdf.format(myCalendar.getTime()));
    }

}
