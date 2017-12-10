package com.isep.tasker.tasker.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.External.GeofenceTransitionsIntentService;
import com.isep.tasker.tasker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public NoteSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_note_settings, container, false);

        spnPriority = (Spinner) mView.findViewById(R.id.spnImportance);
        ArrayAdapter<Priority> spinnerAdapter = new ArrayAdapter<Priority>(getContext(), R.layout.spinner_full, Priority.values());
        spnPriority.setAdapter(spinnerAdapter);
        spnPriority.setSelection(1, true);

        dateText = (EditText) mView.findViewById(R.id.etDate);
        timeText = (EditText) mView.findViewById(R.id.etTime);
        autoLocationText = (EditText) mView.findViewById(R.id.adressAutoLocation);
        autoUserText = (EditText) mView.findViewById(R.id.userAutoLocation);
        lstViewLocations = (ListView) mView.findViewById(R.id.lstLocations);
        lstViewUser = (ListView) mView.findViewById(R.id.lstUsers);
        switchReminder = (Switch) mView.findViewById(R.id.switchReminder);
        switchUser = (Switch) mView.findViewById(R.id.switchUser);
        btnAddLocation = (Button) mView.findViewById(R.id.btnAddLocation);
        btnAddUSer = (Button) mView.findViewById(R.id.btnAddUser);

        myCalendar = Calendar.getInstance();


        return mView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View mView = view;
        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
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
                String date = dateText.getText().toString();
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
                        timeText.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location myLocation = getLastKnownLocation();
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                        .build();

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setBoundsBias(bounds)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                }
            }
        });
    }

    private Location getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void updateDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                addGeofence(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addGeofence(Place place) {
        int GEOFENCE_RADIUS_IN_METERS = 150;
        int GEOFENCE_EXPIRATION_IN_MILLISECONDS = 86400000; // 1 HOUR

        GeofencingClient mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

        Geofence geofence = new Geofence.Builder()
                .setRequestId(UUID.randomUUID().toString())
                .setCircularRegion(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest request = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        PendingIntent mGeofencePendingIntent = PendingIntent.getService(
                getActivity(), 0,
                new Intent(getActivity(), GeofenceTransitionsIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        mGeofencingClient.addGeofences(request, mGeofencePendingIntent);
    }
}
