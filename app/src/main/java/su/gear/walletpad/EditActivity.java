package su.gear.walletpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import su.gear.walletpad.fragments.EditorFragment;
import su.gear.walletpad.fragments.EditorOperationFragment;
import su.gear.walletpad.fragments.EditorPlanFragment;
import su.gear.walletpad.fragments.EditorWalletFragment;

public class EditActivity extends AppCompatActivity
        implements MaterialSpinner.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = EditActivity.class.getSimpleName();

    public static final String TYPE_TAG = "type";
    public static final String MODE_TAG = "mode";

    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_TRANSFER = 2;
    public static final int TYPE_PLAN = 3;
    public static final int TYPE_WALLET = 4;

    private static final int[] menuRes = {
            R.string.item_expense,
            R.string.item_income,
            R.string.item_transfer,
            R.string.item_plan,
            R.string.item_wallet
    };

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;

    private int mode = MODE_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        int type = intent.getIntExtra(TYPE_TAG, 0);
        mode = intent.getIntExtra(MODE_TAG, MODE_ADD);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            throw new RuntimeException("Toolbar not available");
        }
        bar.setDisplayHomeAsUpEnabled(true);

        // Customize toolbar for add/edit
        if (mode == MODE_ADD) {
            bar.setDisplayShowTitleEnabled(false);

            // Add menu items to spinner (in order of their ids)
            List<String> menuItems = new ArrayList<>();
            menuItems.add(getResources().getString(R.string.item_expense));
            menuItems.add(getResources().getString(R.string.item_income));
            menuItems.add(getResources().getString(R.string.item_transfer));
            menuItems.add(getResources().getString(R.string.item_plan));
            menuItems.add(getResources().getString(R.string.item_wallet));

            // Set up spinner
            MaterialSpinner spinner = (MaterialSpinner) LayoutInflater.from(bar.getThemedContext())
                    .inflate(R.layout.toolbar_spinner, toolbar, false);
            spinner.setItems(menuItems);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelectedIndex(type);
            toolbar.addView(spinner);
        } else {
            bar.setTitle(getResources().getString(menuRes[type]));
        }

        Fragment currentFragment = getFragment(type);
        currentFragment.setArguments(intent.getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.edit_fragment_container, currentFragment)
                .commit();
    }

    private EditorFragment getFragment(int type) {
        switch (type) {
            case 3:
                return new EditorPlanFragment();
            case 4:
                return new EditorWalletFragment();
            default:
                return new EditorOperationFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        View saveButton = MenuItemCompat.getActionView(menu.findItem(R.id.action_save_menuitem));
        saveButton.setOnClickListener(this);
        return true;
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        Fragment fragment = getFragment(position);
        Bundle args = new Bundle();
        args.putInt(MODE_TAG, mode);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.edit_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        EditorFragment currentFragment = (EditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.edit_fragment_container);
        if (currentFragment != null && currentFragment.onSave()) {
            finish();
        }
    }
}
