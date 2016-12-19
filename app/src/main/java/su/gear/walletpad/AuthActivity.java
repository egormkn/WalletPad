package su.gear.walletpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;

public class AuthActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = AuthActivity.class.getSimpleName();

    private static final int SIGNIN_REQUEST = 9001;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleApiClient googleApiClient;

    private SignInButton button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        button = (SignInButton) findViewById(R.id.auth_button);
        button.setOnClickListener(this);
        button.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.auth_progress);
        progressBar.setVisibility(View.VISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // 332933317887-nrpoqr5hfs048dlvdac8cm2qjadsil7v.apps.googleusercontent.com
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedIn();
                } else {
                    onSignedOut();
                }
            }
        };
    }

    private void onSignedOut() {
        button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void onSignedIn() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String mimetype = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(mimetype)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGNIN_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(AuthActivity.this, "Google Sign In failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Google Sign In failed.");
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
        /*else if (requestCode == ADD_REQUEST) {
            // TODO
            finish();
        }*/
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    FirebaseCrash.report(task.getException());
                    Toast.makeText(AuthActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, SIGNIN_REQUEST);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(AuthActivity.this, "Connection to Google Services failed",
                Toast.LENGTH_SHORT).show();
        button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    /*private void signOut(boolean force) {
        showUserLoader();
        Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "auth.GoogleSignInApi: Signed Out");
        if (!force) {
            auth.signOut();
            return;
        }
        auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    auth.signOut();
                    Log.d(TAG, "auth.GoogleSignInApi: Signed Out");
                } else {
                    showUserInfo(auth.getCurrentUser());
                    Toast.makeText(MainActivity.this, "Sign out failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}
