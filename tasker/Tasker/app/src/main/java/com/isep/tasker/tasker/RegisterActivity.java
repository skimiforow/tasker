package com.isep.tasker.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;


/**
 * Created by skimiforow on 30/10/2017.
 */

public class RegisterActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;


    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private TextView mStatusTextView;
    private Button mRegisterUser;
    private Button mCancel;

    private ImageView mGoogle;
    private ImageView mTwitter;
    private ImageView mFacebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        setStatusBarTranslucent(true);

        mAuth = FirebaseAuth.getInstance();

        mUserName = (EditText)findViewById(R.id.user_name_field);
        mEmail = (EditText)findViewById(R.id.email_field);
        mPassword = (EditText)findViewById(R.id.password_field1);
        mPasswordConfirm = (EditText)findViewById(R.id.password_field2);
        mRegisterUser = (Button)findViewById(R.id.register_btn);
        mCancel = (Button)findViewById(R.id.cancel_action);
        mStatusTextView = (TextView)findViewById(R.id.status_text_view);

        mGoogle = (ImageView)findViewById(R.id.google);
        mTwitter = (ImageView)findViewById(R.id.twitter);
        mFacebook = (ImageView)findViewById(R.id.facabook);

        mGoogle.setOnClickListener(this);
        mTwitter.setOnClickListener(this);
        mFacebook.setOnClickListener(this);


        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmail.getText().toString(),mPassword.getText().toString());
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getText(R.string.required));
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getText(R.string.required));
            valid = false;
        } else if (mPassword.getText().length()<6) {
            mPasswordConfirm.setError(getText(R.string.password_size));
        } else if (!mPassword.getText().toString().matches(mPasswordConfirm.getText().toString())) {
            mPasswordConfirm.setError(getText(R.string.password_must_match));
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(mUserName.getText().toString()).build();
                            user.updateProfile(profileUpdates);
                            sendEmailVerification();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });

        // [END create_user_with_email]
    }


    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void sendEmailVerification() {
     // Disable button
     findViewById(R.id.register_btn).setEnabled(false);

     // Send verification email
     // [START send_email_verification]
     final FirebaseUser user = mAuth.getCurrentUser();
     user.sendEmailVerification()
     .addOnCompleteListener(this, new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
    // [START_EXCLUDE]
    // Re-enable button
    findViewById(R.id.register_btn).setEnabled(true);

    if (task.isSuccessful()) {
    Toast.makeText(RegisterActivity.this,
    "Verification email sent to " + user.getEmail(),
    Toast.LENGTH_SHORT).show();
    } else {
    Log.e(TAG, "sendEmailVerification", task.getException());
    Toast.makeText(RegisterActivity.this,
    "Failed to send verification email.",
    Toast.LENGTH_SHORT).show();
    }
    // [END_EXCLUDE]
    }
    });
     // [END send_email_verification]
     }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            mStatusTextView.setText(R.string.reg_failed);
        }
    }

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.google) {
            signIn();
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
