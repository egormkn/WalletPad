package su.gear.walletpad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
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
import su.gear.walletpad.model.Separator;
import su.gear.walletpad.model.Wallet;
import su.gear.walletpad.model.WalletsListItem;


public class SummaryFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mFirebaseDatabaseReference;
    /*private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
            mFirebaseAdapter;*/

    private List<OperationsListItem> operations;
    private List<PlansListItem> plans;
    private List<WalletsListItem> wallets;
    private boolean addMenuShown = false;

    private TextView totalSum;
    private FloatingActionMenu menu;
    private long amount = 0;

    public SummaryFragment() {
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        operations = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                operations.add(new Separator("30 ноября 2016"));
            }


            operations.add(new Operation("id", Operation.Type.INCOME, "RUB", 100.0, "Описание", "Category", new ArrayList<>(Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), 100));
        }

        plans = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < 30; i++) {
            plans.add(new Plan(i,
                    0,
                    Plan.Type.GIFT,
                    3 + r.nextInt(30),
                    "USD",
                    "Buy gift",
                    "For my darling"));
        }

        wallets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            wallets.add(new Wallet(43 + r.nextInt(700),
                    0,
                    Wallet.Type.CARD,
                    "RUB",
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

        //operations.add(new ShowMore());
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                menu.showMenuButton(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        RecyclerView recyclerView = (RecyclerView) pagerAdapter.findViewById(R.id.tab_summary_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OperationsAdapter(getActivity(), operations));

        RecyclerView recyclerView2 = (RecyclerView) pagerAdapter.findViewById(R.id.tab_plans_recycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(new PlansAdapter(getActivity(), plans));

        RecyclerView recyclerView3 = (RecyclerView) pagerAdapter.findViewById(R.id.tab_wallets_recycler);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView3.setAdapter(new WalletsAdapter(getActivity(), wallets));
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
}
