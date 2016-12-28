package su.gear.walletpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.adapters.OperationsAdapter;
import su.gear.walletpad.model.Operation;
import su.gear.walletpad.model.OperationsListItem;
import su.gear.walletpad.model.Separator;
import su.gear.walletpad.utils.DateUtils;

public class OperationsFragment extends Fragment {

    private List<OperationsListItem> operations;

    private ValueEventListener operationsListener;
    private DatabaseReference operationsReference;
    private TextView operationsError;
    private ProgressBar operationsProgress;
    private RecyclerView operationsRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_operations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        operationsError = (TextView) view.findViewById(R.id.fragment_operations_error);
        operationsProgress = (ProgressBar) view.findViewById(R.id.fragment_operations_progress);
        operationsRecycler = (RecyclerView) view.findViewById(R.id.fragment_operations_recycler);


        operations = new ArrayList<>();
        operationsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        final OperationsAdapter operationsAdapter = new OperationsAdapter(getActivity(), operations);
        operationsRecycler.setAdapter(operationsAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        operationsReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid())
                .child("operations");

        operationsListener = new ValueEventListener() {
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
                operationsAdapter.notifyDataSetChanged();
                if (operations.size() > 0) {
                    operationsProgress.setVisibility(View.GONE);
                    operationsError.setVisibility(View.GONE);
                    operationsRecycler.setVisibility(View.VISIBLE);
                } else {
                    operationsProgress.setVisibility(View.GONE);
                    operationsError.setVisibility(View.VISIBLE);
                    operationsError.setText("Operations not found");
                    operationsRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                operationsProgress.setVisibility(View.GONE);
                operationsError.setVisibility(View.VISIBLE);
                operationsError.setText(R.string.data_loading_error);
                operationsRecycler.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        operationsReference.addValueEventListener(operationsListener);
    }

    @Override
    public void onStop() {
        operationsReference.removeEventListener(operationsListener);
        super.onStop();
    }
}
