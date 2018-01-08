package com.isep.tasker.tasker.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isep.tasker.tasker.Adapters.PlaceArrayAdapter;
import com.isep.tasker.tasker.Domain.LocationPlace;
import com.isep.tasker.tasker.Domain.Note;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.Reminder;
import com.isep.tasker.tasker.Domain.UserItem;
import com.isep.tasker.tasker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private static final String LOG_TAG = "NoteSettings_Fragment";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    Spinner spnPriority;
    EditText dateText;
    EditText timeText;
    Switch switchReminder;
    Switch switchUser;
    ArrayList<LocationPlace> locationPlaceArrayList;
    ArrayList<UserItem> usersArrayList;
    ArrayAdapter<LocationPlace> locationPlaceArrayAdapter;
    private EditText autoUserText;
    private ListView lstViewLocations;
    private ListView lstViewUser;
    private Button btnAddUSer;
    private Button btnAddLocation;
    private Button btnSubmit;
    private double longitude;
    private double latitude;
    private ArrayAdapter<UserItem> userArrayAdapter;
    private LocationPlace locationPlace;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private FirebaseDatabase database;

    private LatLngBounds atualLocation;


    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            locationPlace = new LocationPlace();
            locationPlace.setName(Html.fromHtml(place.getName() + "").toString());
            locationPlace.setAddress(Html.fromHtml(place.getAddress() + "").toString());
            locationPlace.setLatitude(place.getLatLng().latitude);
            locationPlace.setLongitude(place.getLatLng().longitude);
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    public SettingFragment() {
    }

    public void checkPermissionsForLocation(){

        if (!(getContext().checkSelfPermission
                (android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED))
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_note_settings, container, false);
        database = FirebaseDatabase.getInstance();
        atualLocation = new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude));
        locationPlaceArrayList = new ArrayList<>();
        spnPriority = mView.findViewById(R.id.spnImportance);
        ArrayAdapter<Priority> spinnerAdapter = new ArrayAdapter<Priority>(getContext(), R.layout.spinner_full, Priority.values());
        spnPriority.setAdapter(spinnerAdapter);
        spnPriority.setSelection(1, true);

        dateText = mView.findViewById(R.id.etDate);
        timeText = mView.findViewById(R.id.etTime);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView = mView.findViewById(R.id
                .adressAutoLocation);

        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                atualLocation, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        autoUserText = mView.findViewById(R.id.userAutoLocation);
        lstViewLocations = mView.findViewById(R.id.lstLocations);
        locationPlaceArrayAdapter = new ArrayAdapter<LocationPlace>(getContext(), android.R.layout.simple_list_item_1, locationPlaceArrayList);
        lstViewLocations.setAdapter(locationPlaceArrayAdapter);
        lstViewUser = mView.findViewById(R.id.lstUsers);
        usersArrayList = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usersArrayList);
        lstViewUser.setAdapter(userArrayAdapter);
        switchReminder = mView.findViewById(R.id.switchReminder);
        switchUser = mView.findViewById(R.id.switchUser);
        btnAddLocation = mView.findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationPlace != null) {
                    locationPlaceArrayList.add(locationPlace);
                    locationPlaceArrayAdapter.notifyDataSetChanged();
                    mAutocompleteTextView.setText("");
                }
            }
        });
        btnAddUSer = mView.findViewById(R.id.btnAddUser);
        btnAddUSer.setOnClickListener(this::findInsertedUser);

        myCalendar = Calendar.getInstance();
        Fragment fragment = getFragmentManager().getFragments().get(0);
        if (fragment instanceof AddReminderFragment) {
            switchReminder.setEnabled(false);
            switchReminder.setChecked(true);
            mView.findViewById(R.id.reminderDates).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.adressAutoLocation).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.btnAddLocation).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.lstLocationView).setVisibility(View.VISIBLE);
        }
        setupEdit(mView);

        registerForContextMenu(lstViewLocations);
        registerForContextMenu(lstViewUser);

        return mView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu ( menu, v, menuInfo );
        if (v.getId()==R.id.lstLocations) { //For first listview
            MenuInflater inflater = getActivity ().getMenuInflater();
            inflater.inflate(R.menu.menu_listview1, menu);
        }
        if (v.getId()==R.id.lstUsers) { //For second listview
            MenuInflater inflater = getActivity ().getMenuInflater();
            inflater.inflate(R.menu.menu_listview2, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position; //Use this for getting the list item value
        View view = info.targetView;
        switch(item.getItemId()) {
            case R.id.removelocation:
                Log.d("onContextItemSelected","Remove location Pressed");
                locationPlaceArrayList.remove ( locationPlaceArrayList.get ( index ) );
                locationPlaceArrayAdapter.notifyDataSetChanged ( );
                return true;

            case R.id.removeuser:
                Log.d("onContextItemSelected","Remove user Pressed");
                usersArrayList.remove ( usersArrayList.get ( index ) );
                userArrayAdapter.notifyDataSetChanged ( );
                return true;

            default:
                return super.onContextItemSelected ( item );
        }
    }

    private void setupEdit(View mView) {
        Note note = (Note) getArguments().getSerializable("obj");
        if (isNull(note)) {
            return;
        }
        Reminder reminder = note.getReminder();
        if (!isNull(reminder)) {
            if(!isNull(reminder.getListLocations())){
                switchReminder.setChecked(!reminder.getListLocations().isEmpty());
                if (switchReminder.isChecked()) {
                    mView.findViewById(R.id.reminderDates).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.adressAutoLocation).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.btnAddLocation).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.lstLocationView).setVisibility(View.VISIBLE);

                    locationPlaceArrayList.addAll(reminder.getListLocations());
                    locationPlaceArrayAdapter.notifyDataSetChanged();
                }
                Date date = reminder.getDate();
                dateText.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));
                timeText.setText(new SimpleDateFormat("HH:mm").format(date));
            }
        }

        switchUser.setChecked(!note.getUserList().isEmpty());
        if (switchUser.isChecked()) {
            mView.findViewById(R.id.sharing1).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.sharing2).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.sharing3).setVisibility(View.VISIBLE);

            usersArrayList.addAll(note.getUserList());
            userArrayAdapter.notifyDataSetChanged();
        }
    }

    private void findInsertedUser(View view) {
        String userValue = autoUserText.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        final Query query = reference.orderByChild("email");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasFound = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, String> user = (Map<String, String>) postSnapshot.getValue();
                    if (Objects.equals(user.get("email"), userValue)) {
                        UserItem uItem = new UserItem(user.get("uid"), userValue);
                        usersArrayList.add(uItem);
                        userArrayAdapter.notifyDataSetChanged();
                        hasFound = true;
                    }
                }
                if (!hasFound) {
                    Toast toast = Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View mView = view;


        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkPermissionsForLocation ( );
                    mView.findViewById(R.id.reminderDates).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.adressAutoLocation).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.btnAddLocation).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.lstLocationView).setVisibility(View.VISIBLE);
                } else {
                    mView.findViewById(R.id.reminderDates).setVisibility(View.GONE);
                    mView.findViewById(R.id.adressAutoLocation).setVisibility(View.GONE);
                    mView.findViewById(R.id.btnAddLocation).setVisibility(View.GONE);
                    mView.findViewById(R.id.lstLocationView).setVisibility(View.GONE);
                }
            }
        });
        switchUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mView.findViewById(R.id.sharing1).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.sharing2).setVisibility(View.VISIBLE);
                    mView.findViewById(R.id.sharing3).setVisibility(View.VISIBLE);
                } else {
                    mView.findViewById(R.id.sharing1).setVisibility(View.GONE);
                    mView.findViewById(R.id.sharing2).setVisibility(View.GONE);
                    mView.findViewById(R.id.sharing3).setVisibility(View.GONE);
                }
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mcurrentDate.get(Calendar.MONTH);
                int year = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateText.setText(i2 + "/" + i1 + "/" + i);
                    }
                }
                        , year, month, day);
                datePickerDialog.show();
            }
        });
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                atualLocation = new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

    }


    private void updateDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
