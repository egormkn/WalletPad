package su.gear.walletpad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import su.gear.walletpad.EditActivity;
import su.gear.walletpad.R;
import su.gear.walletpad.adapters.ChildrenPagesAdapter;
import su.gear.walletpad.adapters.OperationsAdapter;
import su.gear.walletpad.adapters.PlansAdapter;
import su.gear.walletpad.adapters.WalletsAdapter;
import su.gear.walletpad.model.Operation;
import su.gear.walletpad.model.OperationsListItem;
import su.gear.walletpad.model.Plan;
import su.gear.walletpad.model.PlansListItem;
import su.gear.walletpad.model.RecyclerButton;
import su.gear.walletpad.model.Separator;
import su.gear.walletpad.model.Wallet;
import su.gear.walletpad.model.WalletsListItem;
import su.gear.walletpad.utils.DateUtils;


public class SummaryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SummaryFragment.class.getSimpleName();

    private List<OperationsListItem> operations;
    private List<PlansListItem> plans;
    private List<WalletsListItem> wallets;

    private TextView tabSummaryError;
    private ProgressBar tabSummaryProgress;
    private RecyclerView tabSummaryRecycler;

    private TextView tabWalletsError;
    private ProgressBar tabWalletsProgress;
    private RecyclerView tabWalletsRecycler;

    private TextView tabPlansError;
    private ProgressBar tabPlansProgress;
    private RecyclerView tabPlansRecycler;

    private TextView totalAmount;
    private FloatingActionMenu menu;
    private long amount = 0;

    private ValueEventListener listener;
    private DatabaseReference operationsReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operations = new ArrayList<>();

        /*for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                operations.add(new Separator("30 ноября 2016"));
            }

            operations.add(new Operation(
                    100.0,
                    "id",
                    Operation.Type.INCOME,
                    new Date(),
                    Currency.getInstance("RUB"),
                    "Wallet",
                    "Description",
                    "Category",
                    new ArrayList<>(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"))));

        }*/

        plans = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < 30; i++) {
            plans.add (new Plan (i,
                    0,
                    Plan.Type.GIFT,
                    3 + r.nextInt(30),
                    Currency.getInstance ("USD"),
                    "Buy gift",
                    "For my darling"));
        }

        wallets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            wallets.add(new Wallet(43 + r.nextInt(700),
                    "0",
                    Wallet.Type.CARD,
                    Currency.getInstance ("RUB"),
                    "Bank #" + (i + 1),
                    true,
                    null
            ));
        }


        for (WalletsListItem wallet : wallets) {
            if (wallet instanceof Wallet) {
                amount += ((Wallet) wallet).getAmount();
            }
        }

        //operations.add(new RecyclerButton());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView summaryAmount = (TextView) view.findViewById(R.id.summary_amount);
        summaryAmount.setText(String.valueOf(amount) + " $");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        final View overlay = view.findViewById(R.id.overlay);
        menu = (FloatingActionMenu) view.findViewById(R.id.add_menu);
        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                overlay.setVisibility(opened ? View.VISIBLE : View.GONE);
            }
        });
        overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (overlay.getVisibility() == View.VISIBLE) {
                    overlay.setVisibility(View.GONE);
                    menu.close(true);
                }
                return true;
            }
        });

        view.findViewById(R.id.add_menu_income).setOnClickListener(this);
        view.findViewById(R.id.add_menu_expense).setOnClickListener(this);
        view.findViewById(R.id.add_menu_transfer).setOnClickListener(this);
        view.findViewById(R.id.add_menu_plan).setOnClickListener(this);
        view.findViewById(R.id.add_menu_wallet).setOnClickListener(this);

        ChildrenPagesAdapter pagerAdapter = new ChildrenPagesAdapter(viewPager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        tabSummaryError = (TextView) pagerAdapter.findViewById(R.id.tab_summary_error);
        tabSummaryProgress = (ProgressBar) pagerAdapter.findViewById(R.id.tab_summary_progress);
        tabSummaryRecycler = (RecyclerView) pagerAdapter.findViewById(R.id.tab_summary_recycler);

        final OperationsAdapter operationsAdapter = new OperationsAdapter(getActivity(), operations);
        tabSummaryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        tabSummaryRecycler.setAdapter(operationsAdapter);

        RecyclerView recyclerView2 = (RecyclerView) pagerAdapter.findViewById(R.id.tab_plans_recycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(new PlansAdapter(getActivity(), plans));

        RecyclerView recyclerView3 = (RecyclerView) pagerAdapter.findViewById(R.id.tab_wallets_recycler);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView3.setAdapter(new WalletsAdapter(getActivity(), wallets));

        operationsReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("operations");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                operations.clear();
                String lastDate = "";
                SimpleDateFormat dateFormat = DateUtils.getDateFormat(getContext());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Operation operation = new Operation(postSnapshot);
                    String operationDate = dateFormat.format(operation.getDate());
                    if (!operationDate.equals(lastDate)) {
                        lastDate = operationDate;
                        operations.add(new Separator(operationDate));
                    }
                    operations.add(operation);
                }
                operations.add(new RecyclerButton("Show all"));
                operationsAdapter.notifyDataSetChanged();
                if (operations.size() > 0) {
                    tabSummaryProgress.setVisibility(View.GONE);
                    tabSummaryError.setVisibility(View.GONE);
                    tabSummaryRecycler.setVisibility(View.VISIBLE);
                } else {
                    tabSummaryProgress.setVisibility(View.GONE);
                    tabSummaryError.setVisibility(View.VISIBLE);
                    tabSummaryError.setText("Operations not found");
                    tabSummaryRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
    }

    @Override
    public void onClick(View v) {
        menu.close(false);
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.MODE_TAG, EditActivity.MODE_ADD);
        switch (v.getId()) {
            case R.id.add_menu_income:
                intent.putExtra(EditActivity.TYPE_TAG, EditActivity.TYPE_INCOME);
                break;
            case R.id.add_menu_expense:
                intent.putExtra(EditActivity.TYPE_TAG, EditActivity.TYPE_EXPENSE);
                break;
            case R.id.add_menu_transfer:
                intent.putExtra(EditActivity.TYPE_TAG, EditActivity.TYPE_TRANSFER);
                break;
            case R.id.add_menu_plan:
                intent.putExtra(EditActivity.TYPE_TAG, EditActivity.TYPE_PLAN);
                break;
            case R.id.add_menu_wallet:
                intent.putExtra(EditActivity.TYPE_TAG, EditActivity.TYPE_WALLET);
                break;
            default:
                throw new RuntimeException("Unknown type of item");
        }
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        operationsReference.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        operationsReference.removeEventListener(listener);
        super.onStop();
    }
}
