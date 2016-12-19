package su.gear.walletpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.adapters.OperationsAdapter;
import su.gear.walletpad.model.Operation;
import su.gear.walletpad.model.OperationsListItem;
import su.gear.walletpad.model.Separator;

public class OperationsFragment extends Fragment {

    private List<OperationsListItem> operations;

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

        /*ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);*/

        operations = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
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
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_operations_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OperationsAdapter(getActivity(), operations));
    }
}
