package su.gear.walletpad.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.CalendarView;
import com.samsistemas.calendarview.widget.DayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        final List <Double> values = new ArrayList <> ();
        Random random = new Random ();
        double tinf = 0, tsup = 0;

        for (int i = 0; i < 28; i ++) {
            double value = random.nextDouble () * 1000 * (random.nextBoolean () ? 1 : -1);
            values.add (value);

            tinf = Math.min (tinf, value);
            tsup = Math.max (tsup, value);
        }

        final double inf = tinf, sup = tsup;

        CalendarView calendar = (CalendarView) view.findViewById (R.id.calendar_view);
        calendar.setFirstDayOfWeek (Calendar.MONDAY);
        calendar.setIsOverflowDateVisible (false);

        List <DayDecorator> decorators = new ArrayList <> ();
        decorators.add(new DayDecorator () {
            public void decorate(DayView day) {
                String value = day.getText ().toString ();
                int number = Integer.parseInt (value);

                if (number >= 1 && number <= values.size ()) {
                    double amount = values.get (number - 1);
                    if (amount < 0) { day.setBackgroundColor (Color.argb (25 + (int) (50 * amount / inf), 250, 0, 0)); }
                    else if (amount > 0) { day.setBackgroundColor (Color.argb (25 + (int) (75 * amount / sup), 0, 250, 0)); }
                }
            }
        });
        calendar.setDecoratorsList (decorators);

        calendar.refreshCalendar (Calendar.getInstance (Locale.getDefault ()));

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
