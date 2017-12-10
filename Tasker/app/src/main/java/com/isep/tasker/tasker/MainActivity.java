package com.isep.tasker.tasker;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.User;
import com.isep.tasker.tasker.Fragments.AccountSettingsFragment;
import com.isep.tasker.tasker.Fragments.AddNewItemFragment;
import com.isep.tasker.tasker.Fragments.HomeFragment;
import com.isep.tasker.tasker.Fragments.ListAllItemsFragment;
import com.isep.tasker.tasker.Fragments.ListArchivedItemsFragment;
import com.isep.tasker.tasker.Fragments.ListSharedItemsFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference database;
    private FirebaseAuth mAuth;

    private FragmentManager manager;
    private android.support.v4.app.FragmentTransaction fragmentTrasaction;

    private TextView mDate;
    private TextView mName;
    private TextView mEmail;

    private NavigationView navigationView;
    private View headerView;
    private FloatingActionMenu mFabMenu;

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase.getInstance ().setPersistenceEnabled ( true );
        database = FirebaseDatabase.getInstance().getReference();


        mFabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        mDate = headerView.findViewById(R.id.date_view);
        mName = headerView.findViewById(R.id.name_view);
        mEmail = headerView.findViewById(R.id.email_view);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        FloatingActionButton fab_note = (FloatingActionButton) findViewById(R.id.add_note);
        fab_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack();
                Bundle bundle = new Bundle();
                bundle.putString("type","Note");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getSupportFragmentManager().beginTransaction().addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment,"AddNewNote");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });

        FloatingActionButton fab_reminder = (FloatingActionButton) findViewById(R.id.add_reminder);
        fab_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack();
                Bundle bundle = new Bundle();
                bundle.putString("type","Reminder");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getSupportFragmentManager().beginTransaction().addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment,"AddNewReminder");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });

        FloatingActionButton fab_checklist = (FloatingActionButton) findViewById(R.id.add_checklist);
        fab_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack();
                Bundle bundle = new Bundle();
                bundle.putString("type","Checklist");
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.setArguments(bundle);
                mFabMenu.hideMenu(true);
                fragmentTrasaction = getSupportFragmentManager().beginTransaction().addToBackStack("Home");
                fragmentTrasaction.replace(R.id.main_container, addNewItemFragment,"AddNewChecklist");
                fragmentTrasaction.commit();
                mFabMenu.close(true);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentTrasaction = getSupportFragmentManager().beginTransaction();
        fragmentTrasaction.add(R.id.main_container, HomeFragment.newInstance(),"Home");
        fragmentTrasaction.commit();
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date instance = new Date();
        String format = dateFormat.format(instance);
        mDate.setText(format);
        final User user = getUser();
        if (!user.isEmailVerified()){
            mName.setText(getString(R.string.emailNotVerified).toString());
            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!user.isEmailVerified()){
                        SendEmailVerification();
                    }   mDrawer.closeDrawer(GravityCompat.START);
                }
            });
        } else {
            mName.setText(user.getName());
        }
        mEmail.setText(user.getEmail());
    }

    private User getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            return new User(name,email,photoUrl,emailVerified);
        }
        return null;
    }

    private void SendEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Task<Void> voidTask = user.sendEmailVerification();
        voidTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 Toast.makeText(MainActivity.this, R.string.email_verification_sent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signOut();

    }

    private void signOut() {
        mAuth.signOut();
        // Google sign out
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        clearBackStack();
        fragmentTrasaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.dashboard) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, HomeFragment.newInstance(),"Home");
            fragmentTrasaction.commit();
        } else if (id == R.id.add_new) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, AddNewItemFragment.newInstance(),"New");
            fragmentTrasaction.commit();

        } else if (id == R.id.list_all) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, ListAllItemsFragment.newInstance(),"List");
            fragmentTrasaction.commit();
        } else if (id == R.id.list_archived) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, ListArchivedItemsFragment.newInstance(),"Archived");
            fragmentTrasaction.commit();
        } else if (id == R.id.list_shared) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, ListSharedItemsFragment.newInstance(),"Shared");
            fragmentTrasaction.commit();
        } else if (id == R.id.account_settings) {
            fragmentTrasaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            fragmentTrasaction.replace(R.id.main_container, AccountSettingsFragment.newInstance(),"Settings");
            fragmentTrasaction.commit();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    @Override
    public void onBackPressed() {
        boolean other = false;
        if (mFabMenu.isOpened()) {
            mFabMenu.close(true);
            other=true;
        }
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
            other=true;
        }
        if (mFabMenu.isMenuHidden()){
            mFabMenu.showMenu(true);
        }
        if(!other){
            super.onBackPressed();
        }
    }
}
