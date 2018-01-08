package com.isep.tasker.tasker.Fragments;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.State;
import com.isep.tasker.tasker.Domain.UserItem;
import com.isep.tasker.tasker.R;
import com.isep.tasker.tasker.Services.TaskerService;

import org.apache.commons.io.IOUtils;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static com.isep.tasker.tasker.Services.GeofenceUtils.createGeofence;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {

    private EditText mName;
    private EditText mDescriptiom;
    private Button mBtnSubmit;
    private SettingFragment settingsFragment;
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
        settingsFragment = ((SettingFragment) getFragmentManager().getFragments().get(1));
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setupEdit();
        return mView;
    }

    private void setupEdit() {
        Note note = (Note) getArguments().getSerializable("obj");
        if (isNull(note)) {
            return;
        }
        mName.setText(note.getTitle());
        mDescriptiom.setText(note.getDescription());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBtnSubmit.setOnClickListener(view1 -> {
            if (mName.getText().toString().isEmpty()) {
                mName.requestFocus();
                mName.setError(getString(R.string.is_mandatory));
                return;
            }

            Note objNote = new Note();
            objNote.setTitle(mName.getText().toString());
            objNote.setDescription(mDescriptiom.getText().toString());
            objNote.setPriority((Priority) settingsFragment.spnPriority.getSelectedItem());
            objNote.setState(State.Active);

            if (settingsFragment.switchReminder.isChecked()) {
                if (settingsFragment.timeText.getText().toString().isEmpty()) {
                    settingsFragment.timeText.requestFocus();
                    settingsFragment.timeText.setError(getString(R.string.error_time_is_needed));
                    return;
                }
                objNote.setReminder(createReminder());
            }

            if (settingsFragment.switchUser.isChecked()) {
                objNote.setUserList(settingsFragment.usersArrayList);
            }

            if (settingsFragment.switchUser.isChecked()) {
                Toast.makeText(getContext(), "User is activated", Toast.LENGTH_SHORT).show();
            }

            DatabaseReference myNotes = getReference();

            objNote.setKey(mName.getText().toString(), mDescriptiom.getText().toString());
            myNotes.setValue(objNote);


            if(!isNull(objNote.getReminder())){
                new TaskerService().setAlarm(getActivity(),"",objNote);
                settingsFragment.locationPlaceArrayList.forEach(place -> createGeofence(getActivity(), place, myNotes.getKey()));
            }

            shareNote(objNote);
            clearBackStack();
            Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
        });
    }

    private DatabaseReference getReference() {
        Note obj = (Note) getArguments().getSerializable("obj");
        if (isNull(obj)) {
            return database.getReference("Notas/" + currentFirebaseUser.getUid()).push();
        }
        return database.getReference(obj.getId());
    }

    private void shareNote(Note note) {
        note.getUserList().forEach(userItem -> new Thread(() -> {
            Reminder reminder = note.getReminder();
            long time = System.currentTimeMillis();
            if (!isNull(reminder)) {
                time = note.getReminder().getDate().getTime();
            }
            doFirebasePost(userItem, time);
        }).start());
    }

    private void doFirebasePost(UserItem user, long timestamp) {
        try {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAAI2r3VZ8:APA91bHCEllM-izjeeDIeuW5WXZwEoBIdM9BcP5AUH2NTFpGi98AE-sBEiQy0YZ33sKlHHiojpDycysa2ip2POAAHNeRaO0zeN6nWg_rnohZ395CoZ35MQL9D1wBEdPmkqhHRxCuJ5U-");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write("{" +
                    "\"to\": \"/topics/" + user.getId() + "\"," +
                    "\"data\" : {" +
                    "      \"taskerId\":\"-L0q6cxEKvBhERS9OGoa\"" +
                    "      \"timestamp\":" + timestamp +
                    "      \"msg\":\"You've been added to a Tasker " + currentUser + "\"" +
                    "    }" +
                    "  }");
            outputStreamWriter.flush();
            outputStreamWriter.close();

            String s = IOUtils.toString(conn.getInputStream(), UTF_8);
            System.out.println(s);
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
