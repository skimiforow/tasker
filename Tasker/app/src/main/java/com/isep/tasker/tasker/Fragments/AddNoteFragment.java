package com.isep.tasker.tasker.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.State;
import com.isep.tasker.tasker.R;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.isep.tasker.tasker.Services.GeofenceUtils.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {

    private EditText mName;
    private EditText mDescriptiom;
    private Button mBtnSubmit;
    private NoteSettingsFragment settingsFragment;
    private FirebaseDatabase database;
    private FirebaseUser currentFirebaseUser;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_add_note, container, false);
        database = FirebaseDatabase.getInstance();
        mName = mView.findViewById(R.id.etName);
        mName.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mDescriptiom = mView.findViewById(R.id.etDescriptio);
        mBtnSubmit = mView.findViewById(R.id.btnSubmit);
        settingsFragment = ((NoteSettingsFragment) getFragmentManager().getFragments().get(1));
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (mName.getText().toString().isEmpty()) {
                    mName.requestFocus();
                    mName.setError(getString(R.string.is_mandatory));
                    return;
                }
                DatabaseReference myNotes = database.getReference("Notas/" + currentFirebaseUser.getUid()).push();

                Note objNote = new Note();
                objNote.setTitle(mName.getText().toString());
                objNote.setDescription(mDescriptiom.getText().toString());
                objNote.setPriority((Priority) settingsFragment.spnPriority.getSelectedItem());
                objNote.setState(State.Active);

                if (settingsFragment.switchReminder.isChecked()) {
                    settingsFragment.locationPlaceArrayList.forEach(p -> createGeofence(getActivity(), p));
                    if (settingsFragment.timeText.getText().toString().isEmpty()) {
                        settingsFragment.timeText.requestFocus();
                        settingsFragment.timeText.setError(getString(R.string.error_time_is_needed));
                        return;
                    }
                    objNote.setReminder(createReminder());
                }

                if (settingsFragment.switchUser.isChecked()) {
                    Toast.makeText(getContext(), "User is activated", Toast.LENGTH_SHORT).show();
                }

                objNote.setKey(mName.getText().toString(), mDescriptiom.getText().toString());
                myNotes.setValue(objNote);

                clearBackStack();
                Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Reminder createReminder() {
        Reminder reminder = new Reminder();
        if (!settingsFragment.dateText.getText().toString().isEmpty()) {
            reminder.setListLocations(settingsFragment.locationPlaceArrayList);
            String dateString = settingsFragment.dateText.getText().toString() + " " + settingsFragment.timeText.getText().toString(); //"19/11/2017 5:11"
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
            try {
                reminder.setDate(format.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return reminder;
    }

    private void clearBackStack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
