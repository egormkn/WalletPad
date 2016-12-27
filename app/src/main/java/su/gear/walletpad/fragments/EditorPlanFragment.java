package su.gear.walletpad.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Currency;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Plan;

public class EditorPlanFragment extends EditorFragment {

    private EditText amountEdit, titleEdit, descEdit;
    private Spinner  currencyEdit, typeEdit;
    private String   currency, type;

    public EditorPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_plan, container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        amountEdit = (EditText) view.findViewById (R.id.edit_plan_amount);
        titleEdit  = (EditText) view.findViewById (R.id.edit_plan_title);
        descEdit   = (EditText) view.findViewById (R.id.edit_plan_description);

        currencyEdit = (Spinner) view.findViewById (R.id.edit_plan_currency);
        typeEdit     = (Spinner) view.findViewById (R.id.edit_plan_type);

        currencyEdit.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected (AdapterView <?> parent,
                                        View itemSelected,
                                        int selectedItemPosition,
                                        long selectedId) {
                currency = getResources ()
                            .getStringArray (R.array.currencies)
                                [selectedItemPosition];
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        typeEdit.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected (AdapterView <?> parent,
                                        View itemSelected,
                                        int selectedItemPosition,
                                        long selectedId) {
                type = getResources ()
                        .getStringArray (R.array.plans_type)
                            [selectedItemPosition];
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onSave() {
        String amount = amountEdit.getText ().toString ();
        amount = amount != null && amount.length () > 0 ? amount : "0";

        String title  = titleEdit.getText  ().toString ();
        String desc   = descEdit.getText   ().toString ();

        if (title == null || title.length () < 1) {
            Toast.makeText (getActivity (),
                            "Please, enter a title",
                            Toast.LENGTH_SHORT).show ();
            return false;
        }

        double amountValue = Double.parseDouble (amount);
        long   id          = 0; //TODO: ID REQUIRED
        long   time        = System.currentTimeMillis ();

        Currency  currencyISO = Currency.getInstance (currency);
        Plan.Type typeValue   = Plan.Type.fetchType (type);

        FirebaseAuth auth = FirebaseAuth.getInstance ();
        if (auth == null || auth.getCurrentUser () == null) { return false; }

        FirebaseDatabase db   = FirebaseDatabase.getInstance ();
        FirebaseUser     user = auth.getCurrentUser();

        DatabaseReference reference = db.getReference ("users")
                                        .child (user.getUid ())
                                        .child ("plans").push ();

        //New plan initialized and ready
        //to be written into database
        //Just do it!
        Plan plan = new Plan (id, time,
                              typeValue,
                              amountValue,
                              currencyISO,
                              title, desc);

        reference.setValue (plan.toMap ());

        Toast.makeText (getActivity(), "New plan: " + title + ", added :)", Toast.LENGTH_SHORT).show();

        return true;
    }

}
