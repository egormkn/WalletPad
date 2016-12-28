package su.gear.walletpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Operation;

public class EditorOperationFragment extends EditorFragment {

    private TextInputLayout amountLayout, descrLayout;
    private EditText amountEditText, descrEditText, tagsEditText;
    private MaterialSpinner currencySpinner, categorySpinner;
    private List<String> currencies, categories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencies = new ArrayList<>();
        currencies.add("RUB");
        currencies.add("USD");
        currencies.add("EUR");

        categories = new ArrayList<>();
        for (Operation.Category category : Operation.Category.values()) {
            categories.add(category.getTitle(getActivity()));
        }
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
        amountLayout = (TextInputLayout) view.findViewById(R.id.edit_operation_amount_holder);
        amountEditText = (EditText) view.findViewById(R.id.edit_operation_amount);
        descrLayout = (TextInputLayout) view.findViewById(R.id.edit_operation_descr_holder);
        descrEditText = (EditText) view.findViewById(R.id.edit_operation_descr);
        tagsEditText = (EditText) view.findViewById(R.id.edit_operation_tags);
        currencySpinner = (MaterialSpinner) view.findViewById(R.id.edit_operation_currency);
        currencySpinner.setItems(currencies);

        categorySpinner = (MaterialSpinner) view.findViewById(R.id.edit_operation_category);
        categorySpinner.setItems(categories);
        //spinner.setOnItemSelectedListener(this);
        //spinner.setSelectedIndex(type);

    }

    @Override
    public boolean onSave() {
        String amountText = amountEditText.getText().toString();
        double amount = 0.0;
        try {
            amount = Double.parseDouble(amountText);
            amountLayout.setErrorEnabled(false);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please, specify a valid amount", Toast.LENGTH_SHORT).show();
            amountLayout.setError("Please, specify a valid amount");
            amountLayout.setErrorEnabled(true);
            return false;
        }

        String descrText = descrEditText.getText().toString();
        if (descrText.length() == 0) {
            descrLayout.setError("Please, fill the description");
            descrLayout.setErrorEnabled(true);
            return false;
        } else {
            descrLayout.setErrorEnabled(false);
        }

        String tagsText = tagsEditText.getText().toString();
        String currency = currencies.get(currencySpinner.getSelectedIndex());
        String category = categories.get(categorySpinner.getSelectedIndex());

        List<String> tags = Arrays.asList(tagsText.split(","));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth == null || auth.getCurrentUser() == null) {
            return false;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newRef = database
                .getReference("users")
                .child(auth.getCurrentUser().getUid())
                .child("operations").push();

        Operation operation = new Operation(
                amount,
                newRef.getKey(),
                Operation.Type.INCOME,
                new Date(),
                Currency.getInstance(currency),
                "Wallet",
                descrText,
                category,
                tags);

        newRef.setValue(operation.toMap());
        return true;
    }
}
