package com.isep.tasker.tasker.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.isep.tasker.tasker.Adapters.ArrayItemAdapter;
import com.isep.tasker.tasker.Domain.LocationPlace;
import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.UserItem;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ArrayList<Object> noteArrayList;
    private FirebaseDatabase database;
    private FirebaseUser currentFirebaseUser;
    private FragmentManager manager;
    private android.support.v4.app.FragmentTransaction fragmentTrasaction;
    private FloatingActionMenu mFabMenu;
    private DrawerLayout mDrawer;
    private ListView listView;
    private ArrayItemAdapter<Object> noteArrayAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().subscribeToTopic(currentFirebaseUser.getUid());
        FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser.getUid()).setValue(currentFirebaseUser);
        database = FirebaseDatabase.getInstance();
        mFabMenu = inflate.findViewById(R.id.fab_menu);
        mDrawer = inflate.findViewById(R.id.drawer_layout);
        noteArrayList = new ArrayList<Object>();
        listView = inflate.findViewById(R.id.main_list);
        noteArrayAdapter = new ArrayItemAdapter<>(getContext(), R.layout.item_layout, noteArrayList);
        listView.setAdapter(noteArrayAdapter);
        noteArrayAdapter.notifyDataSetChanged();
        registerForContextMenu(listView);

        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                noteArrayAdapter.notifyDataSetChanged();
                noteArrayAdapter.refresh();
            }
        });
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = noteArrayAdapter.getItem(i);
                Note newNote;
                Reminder newReminder;
                Bundle bundle = new Bundle();
                if (item instanceof Note) {
                    newNote = (Note) item;
                    bundle.putString("type", "Note");
                    bundle.putSerializable("obj", newNote);
                }

                if (item instanceof Reminder) {
                    newReminder = (Reminder) item;
                    bundle.putString("type", "Reminder");
                    bundle.putSerializable("obj", newReminder);
                }
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                fragmentTrasaction = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment, "AddNewNote");
                fragmentTrasaction.commit();

            }
        });

        FloatingActionButton fab_note = view.findViewById(R.id.add_note);
        fab_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "Note");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment, "AddNewNote");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });

        FloatingActionButton fab_reminder = view.findViewById(R.id.add_reminder);
        fab_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "Reminder");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment, "AddNewReminder");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });

        FloatingActionButton fab_checklist = view.findViewById(R.id.add_checklist);
        fab_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "Checklist");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment, "AddNewChecklist");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });
        DatabaseReference myNotes = FirebaseDatabase.getInstance().getReference().child("Notas/" + currentFirebaseUser.getUid());
        myNotes.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectAllNotes((Map<String, Object>) dataSnapshot.getValue(), true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        collectSharedNotes();

        DatabaseReference myReminders = FirebaseDatabase.getInstance().getReference().child("Reminder/" + currentFirebaseUser.getUid());
        myReminders.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectAllReminders((Map<String, Reminder>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


    }

    private void collectSharedNotes() {
        FirebaseDatabase.getInstance().getReference().child("Notas")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> notes = new HashMap<>();
                        dataSnapshot.getChildren().forEach(snapshot -> snapshot.getChildren().forEach(child -> {
                            String newKey = "Notas/" + snapshot.getKey() + "/" + child.getKey();
                            Map<String, Object> note = (Map<String, Object>) child.getValue();
                            List<Map<String, String>> users = (List<Map<String, String>>) note.get("userList");
                            if (users != null) {
                                users.forEach(u -> {
                                    if (u.get("email").equals(currentFirebaseUser.getEmail())) {
                                        notes.put(newKey, child.getValue());
                                    }
                                });
                            }
                        }));
                        collectAllNotes(notes, false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void collectAllReminders(Map<String, Reminder> value) {
        if (value != null) {
            for (Map.Entry<String, Reminder> entry : value.entrySet()) {

                //new reminder is created
                Reminder reminder = new Reminder();
                Map singleReminder = (Map) entry.getValue();
                reminder.setTitle((String) singleReminder.get("title"));
                reminder.setDescription((String) singleReminder.get("description"));
                if (singleReminder.get("state") != null)
                    reminder.setStringState((String) singleReminder.get("state"));
                if (singleReminder.get("importance") != null) {
                    //reminder.setStringPriority ( (String) singleReminder.get ( "importance" ) );
                }
                if (reminder.getPriority ( ).toString ( ).equals ( "High priority" )) {
                    noteArrayList.add ( 0, reminder );
                } else {
                    noteArrayList.add ( reminder );
                }
            }
            noteArrayAdapter.notifyDataSetChanged();
            noteArrayAdapter.refresh();

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_for_actions_on_items, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position; //Use this for getting the list item value
        View view = info.targetView;
        switch (item.getItemId()) {
            case R.id.remove:
                String key = "";
                Object itemSelected = listView.getAdapter().getItem(index);
                DatabaseReference myNotes = FirebaseDatabase.getInstance().getReference();
                if (itemSelected instanceof Note) {
                    myNotes = FirebaseDatabase.getInstance().getReference().child("Notas/" + currentFirebaseUser.getUid());
                    key = ((Note) itemSelected).getTitle() + ((Note) itemSelected).getDescription();
                }
                if (itemSelected instanceof Reminder) {
                    myNotes = FirebaseDatabase.getInstance().getReference().child("Reminder/" + currentFirebaseUser.getUid());
                    key = ((Reminder) itemSelected).getTitle() + ((Reminder) itemSelected).getDescription();
                }

                Query queryRef = myNotes.orderByChild("key").equalTo(key);
                queryRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().setValue(null);
                                    Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                    noteArrayAdapter.notifyDataSetChanged();
                                    noteArrayAdapter.refresh();
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });
                noteArrayAdapter.notifyDataSetChanged();
                noteArrayAdapter.refresh();
                return true;


            default:
                return super.onContextItemSelected(item);
        }
    }

    private void collectAllNotes(Map<String, Object> value, boolean owner) {
        if (value != null) {
            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //new note is created
                Note note = new Note();
                note.setId(owner ? "Notas/" + currentFirebaseUser.getUid() + "/" + entry.getKey() : entry.getKey());
                Map singleNote = (Map) entry.getValue();
                note.setTitle((String) singleNote.get("title"));
                note.setDescription((String) singleNote.get("description"));
                if (singleNote.get("state") != null)
                    note.setStringState((String) singleNote.get("state"));
                if (singleNote.get("priority") != null) {
                    note.setStringPriority((String) singleNote.get("priority"));
                }
                if (singleNote.get("reminder") != null) {
                    Reminder reminder = new Reminder();

                    Map noteRem = (Map) singleNote.get("reminder");
                    long timestamp = (long) ((Map) (noteRem).get("date")).get("time");
                    reminder.setDate(new Date(timestamp));

                    if (!isNull(noteRem.get("listLocations"))) {
                        ArrayList<LocationPlace> places = new ArrayList<>();
                        ((List<Map<String, Object>>) noteRem.get("listLocations")).forEach(location -> {
                            LocationPlace locationPlace = new LocationPlace();
                            locationPlace.setName((String) location.get("name"));
                            locationPlace.setAddress((String) location.get("address"));
                            locationPlace.setLatitude((double) location.get("latitude"));
                            locationPlace.setLongitude((double) location.get("longitude"));
                            places.add(locationPlace);
                        });
                        reminder.setListLocations(places);
                    }

                    note.setReminder(reminder);
                }

                List<Map<String, String>> userList = (List<Map<String, String>>) ((Map<String, Object>) entry.getValue()).get("userList");
                if (!isNull(userList)) {
                    ArrayList<UserItem> users = new ArrayList<>();
                    userList.forEach(user -> {
                        UserItem userItem = new UserItem(user.get("id"), user.get("email"));
                        users.add(userItem);
                    });
                    note.setUserList(users);
                }


                if (note.getPriority ( ).toString ( ).equals ( "High priority" )) {
                    noteArrayList.add ( 0, note );
                } else {
                    noteArrayList.add ( note );
                }
            }
            noteArrayAdapter.notifyDataSetChanged();
            noteArrayAdapter.refresh();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        noteArrayAdapter.notifyDataSetChanged();
        noteArrayAdapter.refresh();

    }

    private void clearBackStack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        int backStackEntryCount = manager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


}
