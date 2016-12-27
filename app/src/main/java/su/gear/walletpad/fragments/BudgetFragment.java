package su.gear.walletpad.fragments;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.CalendarView;
import com.samsistemas.calendarview.widget.DayView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import su.gear.walletpad.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BudgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    private int month = -1;
    private Map <Integer, double []> storage;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        storage = new HashMap <> ();

        final CalendarView calendar = (CalendarView) view.findViewById (R.id.calendar_view);
        calendar.setFirstDayOfWeek (Calendar.MONDAY);
        calendar.setIsOverflowDateVisible (false);

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance ()
                .getReference ("users")
                .child (user.getUid ())
                .child ("operations");

        final SimpleDateFormat dateForm = new SimpleDateFormat ("yyyyMMdd.HHmmss");

        reference.addValueEventListener(new ValueEventListener () {
            public void onDataChange (DataSnapshot dataSnapshot) {
                Map <Integer, double []> newStorage = new HashMap <> ();

                for (DataSnapshot snapshot: dataSnapshot.getChildren ()) {
                    String dbDate = (String) snapshot.child ("date")
                                                     .getValue ();
                    try {
                        Date date = dateForm.parse (dbDate);
                        Calendar operationCalendar = Calendar.getInstance ();
                        operationCalendar.setTime (date);

                        if (newStorage.get (operationCalendar.MONTH) == null) {
                            newStorage.put (operationCalendar.MONTH, new double [31]);
                        }

                        String dbAmount = (String) snapshot.child ("amount")
                                                           .getValue ();
                        Double amount = Double.parseDouble (dbAmount);
                        int operationDay = operationCalendar.DAY_OF_MONTH;

                        if (operationDay >= 0 && operationDay < 31) {
                            newStorage.get (operationCalendar.MONTH)
                                            [operationDay] += amount;
                        }
                    } catch (ParseException pe) {}

                    System.out.println ("cdscsdcsd cnsdkcn ksdcsdjkc snck ksdc sck ckds ");
                }

                //Flush to working data
                storage = newStorage;
            }

            public void onCancelled (DatabaseError databaseError) {

            }
        });

        month = Calendar.getInstance ()
                        .get (Calendar.MONTH);
        month += 1; //Improve number to real

        changeMonth (calendar);
    }

    private void changeMonth (final CalendarView calendar) {
        double tinf = 0, tsup = 0;

        final List <Double> values = new ArrayList <> ();

        for (int i = 0; i < values.size (); i ++) {
            double value = values.get (i);
            tinf = Math.min (tinf, value);
            tsup = Math.max (tsup, value);
        }

        final double inf = tinf, sup = tsup;

        final List <DayDecorator> decorators = new ArrayList <> ();
        decorators.add (new DayDecorator () {
            public void decorate (DayView day) {
                String value = day.getText ().toString ();
                int number = Integer.parseInt (value);

                if (number >= 1 && number <= values.size ()) {
                    double amount = values.get (number - 1);
                    if (amount < 0)      { day.setBackgroundColor (Color.argb (25 + (int) (50 * amount / inf), 250, 0, 0)); }
                    else if (amount > 0) { day.setBackgroundColor (Color.argb (25 + (int) (75 * amount / sup), 0, 250, 0)); }
                }

                day.setOnClickListener (new View.OnClickListener() {
                    public void onClick (View v) {
                        calendar.setDecoratorsList (decorators);
                        calendar.refreshCalendar (Calendar.getInstance (Locale.getDefault ()));
                    }
                });
            }
        });
        calendar.setDecoratorsList (decorators);

        calendar.refreshCalendar (Calendar.getInstance (Locale.getDefault ()));
    }

    private void loadValues () {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
