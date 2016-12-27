package su.gear.walletpad.fragments;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Operation;

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
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    private DatabaseReference  reference;
    private ValueEventListener valueListener;
    private SimpleDateFormat   dateFormat;

    private Map <Integer, List <Double>> storage;

    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        final CalendarView calendar = (CalendarView) view.findViewById (R.id.calendar_view);
        calendar.setFirstDayOfWeek        (Calendar.MONDAY);
        calendar.setIsOverflowDateVisible (false);
        calendar.refreshCalendar          (Calendar.getInstance (Locale.getDefault ()));

        calendar.setOnMonthChangedListener (new CalendarView.OnMonthChangedListener () {
            public void onMonthChanged (Date monthDate) { changeMonth (calendar); }
        });

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser();
        reference = FirebaseDatabase.getInstance ()
                .getReference ("users")
                .child (user.getUid ())
                .child ("operations");

        dateFormat = new SimpleDateFormat ("yyyyMMdd.HHmmss", Locale.US);
        storage    = new HashMap <> ();

        valueListener = new ValueEventListener () {
            public void onDataChange (DataSnapshot dataSnapshot) {
                try {
                    Map <Integer, List <Double>> tmpStorage = new HashMap <> ();

                    for (DataSnapshot data: dataSnapshot.getChildren ()) {
                        String dbDate = (String) data.child ("date")
                                                     .getValue ();
                        Date date = dateFormat.parse (dbDate);
                        Calendar nodeCalendar = Calendar.getInstance ();
                        nodeCalendar.setTime (date);

                        int year  = nodeCalendar.get (Calendar.YEAR);
                        int month = nodeCalendar.get (Calendar.MONTH);
                        int day   = nodeCalendar.get (Calendar.DAY_OF_MONTH);

                        if (!tmpStorage.containsKey (year * 100 + month)) {
                            tmpStorage.put (year * 100 + month, new ArrayList <Double> ());
                            List <Double> currentList = tmpStorage.get (year * 100 + month);
                            for (int i = 0; i < 31; i ++) { currentList.add (0D); }
                        }

                        String dbAmount = (String) data.child ("amount")
                                                       .getValue();
                        dbAmount = dbAmount != null && dbAmount.length () > 0
                                    ? dbAmount
                                    : "0";
                        double amount = Double.parseDouble (dbAmount);

                        double currentAmount = tmpStorage.get (year * 100 + month).get (day - 1);
                        String dbType = (String) data.child ("type")
                                                     .getValue ();
                        Operation.Type type = Operation.Type.valueOf (dbType);
                        if (type == Operation.Type.EXPENSE) { currentAmount *= -1; }
                        tmpStorage.get (year * 100 + month).set (day - 1, currentAmount + amount);
                    }

                    //Flush in real data
                    storage = tmpStorage;
                    changeMonth (calendar);
                } catch (Exception e) { e.printStackTrace (); }
            }

            public void onCancelled(DatabaseError databaseError) {}
        };

        reference.addValueEventListener (valueListener);
        changeMonth (calendar);
    }

    private final List <String> months = new ArrayList <> (); {
        months.add ("JANUARY");
        months.add ("FEBRUARY");
        months.add ("MARCH");
        months.add ("APRIL");
        months.add ("MAY");
        months.add ("JUNE");
        months.add ("JULY");
        months.add ("AUGUST");
        months.add ("SEPTEMBER");
        months.add ("OCTOBER");
        months.add ("NOVEMBER");
        months.add ("DECEMBER");
    }

    private void changeMonth (final CalendarView calendar) {
        double tinf = 0, tsup = 0;

        int month = months.indexOf (calendar.getCurrentMonth ());
        int year  = Integer.parseInt (calendar.getCurrentYear ());
        final List <Double> values = loadValues (year, month);
        System.out.println ("Data: " + values.size ());

        for (int i = 0; i < values.size (); i ++) {
            double value = values.get (i);
            tinf = Math.min (tinf, value);
            tsup = Math.max (tsup, value);
        }

        final double inf = tinf, sup = tsup;

        List <DayDecorator> decorators = new ArrayList <> ();
        decorators.add (new DayDecorator () {

            {
                System.out.println ("Day decorator - static");
            }

            public void decorate (DayView day) {
                String value = day.getText ().toString ();
                int number = Integer.parseInt (value);

                if (number >= 1 && number <= values.size ()) {
                    double amount = values.get (number - 1);
                    if (amount < 0)      { day.setBackgroundColor (Color.argb (25 + (int) (50 * amount / inf), 250, 0, 0)); }
                    else if (amount > 0) { day.setBackgroundColor (Color.argb (25 + (int) (75 * amount / sup), 0, 250, 0)); }
                }

                day.setOnClickListener (new View.OnClickListener () {
                    public void onClick (View v) {}
                });
            }
        });

        calendar.setDecoratorsList (decorators);
        SimpleDateFormat date = new SimpleDateFormat ("yyyyMM", Locale.US);
        Calendar currentMonth = Calendar.getInstance ();

        try {
            currentMonth.setTime (date.parse (year + ""
                                    + (month + 1 < 10 ? "0" : "")
                                    + (month + 1)));
            calendar.refreshCalendar   (currentMonth);
        } catch (Exception e) { e.printStackTrace (); }
    }

    private List <Double> loadValues (int year, int month) {
        System.out.println ("Loading data for " + year + "-" + month);
        if (storage.containsKey (year * 100 + month)) {
            System.out.println ("Data for key " + (year * 100 + month) + " found");
            return storage.get (year * 100 + month);
        }

        return new ArrayList <> ();
    }

    public void onStop () {
        reference.removeEventListener (valueListener);
        super.onStop ();
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
