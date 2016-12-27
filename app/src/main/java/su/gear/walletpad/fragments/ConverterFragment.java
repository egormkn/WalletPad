package su.gear.walletpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import su.gear.walletpad.R;
import su.gear.walletpad.converter.ConverterResult;
import su.gear.walletpad.converter.CurrencyConverter;

public class ConverterFragment extends Fragment {

    private Spinner from, to;
    private TextView result, date;
    private EditText amount;

    private String fromCur, toCur;
    private String amountCur;

    private boolean converting = false;

    public ConverterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        from = (Spinner) view.findViewById (R.id.fromCUR);
        to   = (Spinner) view.findViewById (R.id.toCUR);

        result = (TextView) view.findViewById (R.id.resultCUR);
        date   = (TextView) view.findViewById (R.id.dateCUR);
        amount = (EditText) view.findViewById (R.id.amountCUR);

        fromCur = "RUB";
        toCur   = "RUB";

        from.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected (AdapterView <?> parent,
                                        View itemSelected,
                                        int selectedItemPosition,
                                        long selectedId) {
                fromCur = getResources ().getStringArray (R.array.currencies) [selectedItemPosition];
                amountCur = amount.getText ().toString ();
                noticeChanges ();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        to.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected (AdapterView <?> parent,
                                        View itemSelected,
                                        int selectedItemPosition,
                                        long selectedId) {
                toCur = getResources ().getStringArray (R.array.currencies) [selectedItemPosition];
                amountCur = amount.getText().toString();
                noticeChanges();
            }

            public void onNothingSelected (AdapterView <?> parent) {}
        });

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) { amountCur = charSequence.toString(); }
                else { amountCur = "0"; }
                noticeChanges();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

        });
    }

    private void noticeChanges() {
        if (!converting
                &&fromCur != null
                && toCur != null
                && amountCur != null) {

            Log.d("ConverterFragment", "From and to: " + fromCur + " to " + toCur);
            converting = true;

            final ConverterFragment frag = this;
            Log.d ("ConverterFragment", "Loading...");

            Bundle args = new Bundle();
            args.putString ("from", fromCur);
            args.putString ("to", toCur);
            args.putDouble ("amount", Double.parseDouble (amountCur));

            getActivity()
                    .getSupportLoaderManager()
                    .restartLoader(0, args, new LoaderManager.LoaderCallbacks<ConverterResult>() {

                        public Loader<ConverterResult> onCreateLoader(int id, Bundle args) {
                            String from = args.getString("from");
                            String to = args.getString("to");
                            double amount = args.getDouble("amount");

                            Log.d ("ConverterFragment", "onCreateLoader");
                            return new CurrencyConverter(frag.getContext(), from, to, amount);
                        }

                        public void onLoadFinished(Loader<ConverterResult> loader, ConverterResult res) {
                            if (res.getResult() != -1) {
                                result.setText(((double) Math.round(res.getResult() * 100)) / 100 + "");
                                date.setText(res.getDate());
                                Log.d("ConverterFragment", res.getResult() + "");
                            } else {
                                result.setText("Converting failed");
                                date.setText(res.getDate());
                                Log.d("ConverterFragment", "Loading failed");
                            }

                            converting = false;
                        }

                        public void onLoaderReset(Loader<ConverterResult> loader) {
                            // Do nothing
                        }
                    });
        }
    }
}
