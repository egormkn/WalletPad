package su.gear.walletpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Operation;

public class EditorOperationFragment extends Fragment implements EditorFragment {

    private EditText amountEditText, descrEditText, tagsEditText;

    // Required empty public constructor
    public EditorOperationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_operation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amountEditText = (EditText) view.findViewById(R.id.edit_operation_amount);
        descrEditText = (EditText) view.findViewById(R.id.edit_operation_descr);
        tagsEditText = (EditText) view.findViewById(R.id.edit_operation_tags);
    }

    @Override
    public boolean onSave() {
        String amountText = amountEditText.getText().toString();
        String descrText = descrEditText.getText().toString();
        String tagsText = tagsEditText.getText().toString();

        double amount = Double.parseDouble(amountText);
        List<String> tags = Arrays.asList(tagsText.split(","));

        Operation operation = new Operation(1, Operation.Type.INCOME, "RUB", 1000, "Описание", "Категория", tags, new Date().getTime());
        Toast.makeText(getActivity(), "" + amount + " " + descrText, Toast.LENGTH_SHORT).show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null && auth.getCurrentUser() != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference newRef = database
                    .getReference("users")
                    .child(auth.getCurrentUser().getUid())
                    .child("operations").push();
            newRef.setValue(operation.toMap());
        }

        return true;
    }
}
