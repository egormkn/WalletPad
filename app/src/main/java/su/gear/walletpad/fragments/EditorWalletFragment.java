package su.gear.walletpad.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Currency;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Wallet;

public class EditorWalletFragment extends EditorFragment {

    private EditText amountEdit, titleEdit;
    private Spinner  currencyEdit, typeEdit;
    private String   currency, type;
    private CheckBox considerEdit;

    public EditorWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_wallet, container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        amountEdit = (EditText) view.findViewById (R.id.edit_wallet_amount);
        titleEdit  = (EditText) view.findViewById (R.id.edit_wallet_title);

        currencyEdit = (Spinner) view.findViewById (R.id.edit_wallet_currency);
        typeEdit     = (Spinner) view.findViewById (R.id.edit_wallet_type);

        considerEdit = (CheckBox) view.findViewById (R.id.edit_wallet_consider);

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
                        .getStringArray (R.array.wallets_type)
                         [selectedItemPosition];
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onSave() {
        String amount = amountEdit.getText ().toString ();
        String title  = titleEdit.getText  ().toString ();
        amount = amount != null && amount.length () > 0 ? amount : "0";

        double  amountValue = Double.parseDouble (amount);
        String  id          = "0"; //TODO: ID REQUIRED
        boolean checked     = considerEdit.isChecked ();

        //New wallet initialized and ready
        //to be written into database
        //Just do it!
        Wallet wallet = new Wallet (amountValue, id,
                                    Wallet.Type.fetchType (type),
                                    Currency.getInstance (currency), title,
                                    checked, null);

        Toast.makeText (getActivity(), "New wallet: " + title + ", added :)", Toast.LENGTH_SHORT).show();

        return true;
    }
}
