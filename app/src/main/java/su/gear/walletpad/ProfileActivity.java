package su.gear.walletpad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private TextView usernameView;
    private Button logoutButton;
    private CircleImageView imageView;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            throw new RuntimeException("Toolbar not available");
        }
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("User profile");

        usernameView = (TextView) findViewById(R.id.profile_username);
        logoutButton = (Button) findViewById(R.id.profile_logout);
        imageView = (CircleImageView) findViewById(R.id.profile_image);

        usernameView.setText(user.getDisplayName());
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(imageView);
        }
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // 332933317887-nrpoqr5hfs048dlvdac8cm2qjadsil7v.apps.googleusercontent.com
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    auth.signOut();
                    Log.d(TAG, "auth.GoogleSignInApi: Signed Out");
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Sign out failed", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(ProfileActivity.this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }
}
