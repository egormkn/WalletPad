package su.gear.walletpad;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import su.gear.walletpad.fragments.BanksFragment;
import su.gear.walletpad.fragments.BudgetFragment;
import su.gear.walletpad.fragments.ConverterFragment;
import su.gear.walletpad.fragments.ExportFragment;
import su.gear.walletpad.fragments.ImportFragment;
import su.gear.walletpad.fragments.OperationsFragment;
import su.gear.walletpad.fragments.StatisticsFragment;
import su.gear.walletpad.fragments.SummaryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout userInfo;

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

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View travelModeView = MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_travel));
        final AppCompatCheckBox travelCheckbox = (AppCompatCheckBox) travelModeView.findViewById(R.id.nav_checkbox);
        travelModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelCheckbox.performClick();
            }
        });
        travelCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(MainActivity.this, "Travel mode: " + b, Toast.LENGTH_SHORT).show();
            }
        });

        userInfo = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.user);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        String userName = user.getDisplayName();
        String userEmail = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (userName != null) {
            ((TextView) userInfo.findViewById(R.id.username)).setText(userName);
        }
        if (userEmail != null) {
            ((TextView) userInfo.findViewById(R.id.sync_info)).setText(userEmail);
        }
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into((CircleImageView) userInfo.findViewById(R.id.avatar));
        }

        /*
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e(TAG, "CONNECTED");
                } else {
                    Log.e(TAG, "DISCONNECTED");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        */

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new SummaryFragment()).commit();
    }

    private boolean isDrawerLocked() {
        return drawer.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_OPEN;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        // TODO Close fab menu
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_update:
                Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        Intent intent;

        switch(item.getItemId()) {
            case R.id.nav_summary:
                fragmentClass = SummaryFragment.class;
                break;
            case R.id.nav_operations:
                fragmentClass = OperationsFragment.class;
                break;
            case R.id.nav_budget:
                fragmentClass = BudgetFragment.class;
                break;
            case R.id.nav_statistics:
                fragmentClass = StatisticsFragment.class;
                break;
            case R.id.nav_import:
                fragmentClass = ImportFragment.class;
                break;
            case R.id.nav_banks:
                fragmentClass = BanksFragment.class;
                break;
            case R.id.nav_converter:
                fragmentClass = ConverterFragment.class;
                break;
            case R.id.nav_export:
                fragmentClass = ExportFragment.class;
                break;
            case R.id.nav_travel:
                CheckBox checkbox = (CheckBox) MenuItemCompat.getActionView(item).findViewById(R.id.nav_checkbox);
                checkbox.performClick();
                return true;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            default:
                fragmentClass = SummaryFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}