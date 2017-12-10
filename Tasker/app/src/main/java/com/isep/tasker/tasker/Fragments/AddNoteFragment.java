package com.isep.tasker.tasker.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {

    private EditText mName;
    private EditText mDescriptiom;
    private Button mBtnSubmit;

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
        mName = mView.findViewById ( R.id.etName );
        mName.requestFocus ( );
        getActivity ( ).getWindow ( ).setSoftInputMode ( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
        mDescriptiom = mView.findViewById ( R.id.etDescriptio );
        mBtnSubmit = mView.findViewById ( R.id.btnSubmit );

        List<Fragment> fragmentList = getFragmentManager ( ).getFragments ( );
        Fragment fragment = fragmentList.get ( fragmentList.size ( ) - 1 );
        LayoutInflater noteSettingsInflater = (LayoutInflater) fragment.getContext ( )
                .getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        View view = noteSettingsInflater.inflate ( R.layout.fragment_note_settings, null );

        dateText = view.findViewById ( R.id.etDate );
        timeText = view.findViewById ( R.id.etTime );
        autoLocationText = view.findViewById ( R.id.adressAutoLocation );
        autoUserText = view.findViewById ( R.id.userAutoLocation );
        lstViewLocations = view.findViewById ( R.id.lstLocations );
        lstViewUser = view.findViewById ( R.id.lstUsers );
        switchReminder = view.findViewById ( R.id.switchReminder );
        switchUser = view.findViewById ( R.id.switchUser );
        btnAddLocation = view.findViewById ( R.id.btnAddLocation );
        btnAddUSer = view.findViewById ( R.id.btnAddUser );
        spnPriority = view.findViewById ( R.id.spnImportance );

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );


        mBtnSubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (mName.getText ( ).toString ( ).equals ( "" )) {
                    mName.requestFocus ();
                    mName.setError ( getString ( R.string.is_mandatory  ));
                    return;
                }
                DatabaseReference myNotes = database.getReference ( "Notas/" + currentFirebaseUser.getUid ( ) ).push ( );

                Note objNote = new Note (  );
                objNote.setTitle ( mName .getText ().toString ());
                objNote.setDescription ( mDescriptiom .getText ().toString ());
                Priority selectedItem = (Priority) spnPriority.getSelectedItem ( );
                objNote.setPriority ( selectedItem );
                objNote.setState ( State.Active );
                if (switchReminder.isActivated ( )) {
                    Reminder reminder = new Reminder ( );
                    if (!dateText.toString ( ).equals ( "" )) {
                        reminder.setDate ( new Date ( dateText.toString ( ) ) );
                        if (!timeText.toString ( ).equals ( "" )) {
                            reminder.setTime ( new Time ( Long.getLong ( timeText.toString ( ) ) ) );
                        } else {
                            timeText.requestFocus ( );
                            timeText.setError ( getString ( R.string.error_time_is_needed ) );
                            return;
                        }
                        if (lstViewLocations.getAdapter ( ).getCount ( ) > 0) {
                            int count = lstViewLocations.getAdapter ( ).getCount ( );
                        }
                    }
                    objNote.setReminder ( reminder );
                }
                if (switchUser.isActivated ( )) {
                    Toast.makeText ( getContext ( ), "User is activated", Toast.LENGTH_SHORT ).show ( );
                }

                objNote.setKey ( mName.getText ( ).toString ( ), mDescriptiom.getText ( ).toString ( ) );
                myNotes.setValue ( objNote );
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
