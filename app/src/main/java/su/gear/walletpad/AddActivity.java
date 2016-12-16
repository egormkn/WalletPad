package su.gear.walletpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import su.gear.walletpad.fragments.EditorFragment;
import su.gear.walletpad.fragments.EditorOperationFragment;
import su.gear.walletpad.fragments.EditorPlanFragment;
import su.gear.walletpad.fragments.EditorWalletFragment;
import su.gear.walletpad.model.Operation;

public class AddActivity extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener {

    private static final String TAG = AddActivity.class.getSimpleName();

    private EditorFragment currentFragment = null;

    List<String> menuItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            throw new RuntimeException("Toolbar == null");
        }
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);

        menuItems = new ArrayList<>();
        menuItems.add(getResources().getString(R.string.item_expense));
        menuItems.add(getResources().getString(R.string.item_income));
        menuItems.add(getResources().getString(R.string.item_transfer));
        menuItems.add(getResources().getString(R.string.item_plan));
        menuItems.add(getResources().getString(R.string.item_wallet));


        MaterialSpinner spinner = new MaterialSpinner(bar.getThemedContext());


        spinner.setItems(menuItems);
        spinner.setOnItemSelectedListener(this);
        spinner.setBackgroundColor(ContextCompat.getColor(bar.getThemedContext(), R.color.colorPrimary));

        toolbar.addView(spinner);


        Intent intent = getIntent();
        switch (intent.getStringExtra("type")) {
            case "Plan":
                currentFragment = new EditorPlanFragment();
                break;
            case "Wallet":
                currentFragment = new EditorWalletFragment();
                break;
            default:
                currentFragment = new EditorOperationFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, (Fragment) currentFragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Operation operation = new Operation(1, Operation.Type.INCOME, "RUB", 1000, "Описание", "Категория", new ArrayList<String>(), new Date().getTime());
/*
this.id = id;
        this.type = type;
        this.currency = Currency.getInstance(currencyCode);
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.date = new Date(timestamp);
 */
            currentFragment.onSave();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        String selected = menuItems.get(position);
        switch (selected) {
            case "Plan":
                currentFragment = new EditorPlanFragment();
                break;
            case "Wallet":
                currentFragment = new EditorWalletFragment();
                break;
            default:
                currentFragment = new EditorOperationFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, (Fragment) currentFragment)
                .commit();
    }
}
