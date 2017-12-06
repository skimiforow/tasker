package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isep.tasker.tasker.Domain.Importance;
import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.State;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        mDescriptiom = mView.findViewById ( R.id.etDescriptio );
        mBtnSubmit = mView.findViewById ( R.id.btnSubmit );
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
        spnPriority = (Spinner)mView.findViewById(R.id.spnImportance);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        mBtnSubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if(mName.getText ().toString().matches ( "" )){
                    mName.requestFocus ();
                    mName.setError ( getString ( R.string.is_mandatory  ));
                }
               DatabaseReference myNotes = database.getReference("Notas/currentFirebaseUser.getUid ()"+currentFirebaseUser.getUid ());

                Map<String,Note> nota = new HashMap<> (  );
                Note objNote = new Note (  );
                objNote.setTitle ( mName .getText ().toString ());
                objNote.setDescription ( mDescriptiom .getText ().toString ());
                nota.put ( String.valueOf ( System.currentTimeMillis() ),objNote);

                myNotes.setValue ( nota );
            }
        } );
    }
}
