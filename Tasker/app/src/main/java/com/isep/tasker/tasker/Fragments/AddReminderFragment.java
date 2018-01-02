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
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.State;
import com.isep.tasker.tasker.R;

import java.sql.Time;
import java.util.Date;

import static com.isep.tasker.tasker.Services.GeofenceUtils.createGeofence;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReminderFragment extends Fragment {

    private EditText mName;
    private EditText mDescriptiom;
    private Button mBtnSubmit;
    private SettingFragment settingFragment;

    private FirebaseDatabase database;
    private FirebaseUser currentFirebaseUser;

    public AddReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate ( R.layout.fragment_add_reminder, container, false );
        database = FirebaseDatabase.getInstance ( );
        mName = mView.findViewById ( R.id.etName );
        mName.requestFocus ( );
        getActivity ( ).getWindow ( ).setSoftInputMode ( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
        mDescriptiom = mView.findViewById ( R.id.etDescriptio );
        mBtnSubmit = mView.findViewById ( R.id.btnSubmit );
        settingFragment = ((SettingFragment) getFragmentManager ( ).getFragments ( ).get ( 1 ));

        currentFirebaseUser = FirebaseAuth.getInstance ( ).getCurrentUser ( );

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        mBtnSubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (mName.getText ( ).toString ( ).equals ( "" )) {
                    mName.requestFocus ( );
                    mName.setError ( getString ( R.string.is_mandatory ) );
                    return;
                }
                DatabaseReference myReminder = database.getReference ( "Reminder/" + currentFirebaseUser.getUid ( ) ).push ( );
                Reminder objReminder = new Reminder ( );
                objReminder.setTitle ( mName.getText ( ).toString ( ) );
                objReminder.setDescription ( mDescriptiom.getText ( ).toString ( ) );
                Priority selectedItem = (Priority) settingFragment.spnPriority.getSelectedItem ( );
                objReminder.setPriority ( selectedItem );
                objReminder.setState ( State.Active );
                if (settingFragment.switchReminder.isActivated ( )) {
                    Reminder reminder = new Reminder ( );
                    if (!settingFragment.dateText.toString ( ).equals ( "" )) {
                        reminder.setDate ( new Date ( settingFragment.dateText.toString ( ) ) );
                        if (!settingFragment.timeText.toString ( ).equals ( "" )) {
                            reminder.setTime ( new Time ( Long.getLong ( settingFragment.timeText.toString ( ) ) ) );
                        } else {
                            settingFragment.timeText.requestFocus ( );
                            settingFragment.timeText.setError ( getString ( R.string.error_time_is_needed ) );
                            return;
                        }
                        if (settingFragment.locationPlaceArrayList.size ( ) > 0) {
                            settingFragment.locationPlaceArrayList.forEach ( place -> createGeofence ( getActivity ( ), place, myReminder.getKey ( ), objReminder ) );

                        }
                    }
                    objReminder.setReminder ( reminder );
                }
                if (settingFragment.switchUser.isActivated ( )) {
                    Toast.makeText ( getContext ( ), "User is activated", Toast.LENGTH_SHORT ).show ( );
                }

                objReminder.setKey ( mName.getText ( ).toString ( ), mDescriptiom.getText ( ).toString ( ) );
                myReminder.setValue ( objReminder );
                clearBackStack ( );
                Toast.makeText ( getContext ( ), R.string.success, Toast.LENGTH_SHORT ).show ( );
            }
        } );
    }

    private void clearBackStack() {
        FragmentManager manager = getActivity ( ).getSupportFragmentManager ( );
        if (manager.getBackStackEntryCount ( ) > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt ( 0 );
            manager.popBackStack ( first.getId ( ), FragmentManager.POP_BACK_STACK_INCLUSIVE );
        }
    }
}
