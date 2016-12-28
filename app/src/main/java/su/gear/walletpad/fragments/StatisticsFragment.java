package su.gear.walletpad.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

import su.gear.walletpad.R;

public class StatisticsFragment extends Fragment {


    private LineChart mLineChart;
    private PieChart  mPieChart;
    private HorizontalBarChart mStackedBarChart;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLineChart = (LineChart) view.findViewById(R.id.line_chart);
        mPieChart  = (PieChart)  view.findViewById(R.id.pie_chart);
        mStackedBarChart = (HorizontalBarChart) view.findViewById(R.id.stacked_bar_chart);

        /////////////////////////////////////////////
        mLineChart.setData(getLineChartData());
        mLineChart.getDescription().setEnabled(false);
        mLineChart.animateY(1000);
        mLineChart.setDrawGridBackground(false);
        Legend mLineChartLegent = mLineChart.getLegend();
        mLineChartLegent.setEnabled(false);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);                   // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        mLineChart.invalidate();
        /////////////////////////////////////////////
        mPieChart.setData(getPieChartData());
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(50f);
        Legend mPieChartLegend = mPieChart.getLegend();
        mPieChartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mPieChartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        mPieChartLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mPieChartLegend.setDrawInside(false);
        mPieChart.setCenterText(generateCenterPieChartText());
        mPieChart.setCenterTextSize(6f);
        mPieChart.invalidate();
        //////////////////////////////////////////
        mStackedBarChart.setData(getStackedData());
        mStackedBarChart.getDescription().setEnabled(false);
        mStackedBarChart.setDrawGridBackground(false);
        mStackedBarChart.setPinchZoom(false);// scaling can now only be done on x- and y-axis separately
        mStackedBarChart.setDrawBarShadow(false);
        mStackedBarChart.setDrawValueAboveBar(true);
        mStackedBarChart.setHighlightFullBarEnabled(false);
        mStackedBarChart.getAxisLeft().setEnabled(false);
        //mStackedBarChart.getAxisRight().setAxisMaximum(50f);
        //mStackedBarChart.getAxisRight().setAxisMinimum(-50f);
        mStackedBarChart.getAxisRight().setDrawGridLines(false);
        mStackedBarChart.getAxisRight().setDrawZeroLine(true);
        mStackedBarChart.getAxisRight().setLabelCount(6, false);
        mStackedBarChart.getAxisRight().setValueFormatter(new CustomFormatter());
        mStackedBarChart.getAxisRight().setTextSize(9f);
        XAxis xStackedBarAxis = mStackedBarChart.getXAxis();
        xStackedBarAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xStackedBarAxis.setDrawGridLines(false);
        xStackedBarAxis.setDrawAxisLine(false);
        xStackedBarAxis.setTextSize(9f);
        xStackedBarAxis.setAxisMinimum(0f);
        xStackedBarAxis.setAxisMaximum(60f);
        xStackedBarAxis.setCenterAxisLabels(true);
//        xStackedBarAxis.setLabelCount(6);
//        xStackedBarAxis.setGranularity(10f);
        xStackedBarAxis.setValueFormatter(new IAxisValueFormatter() {
            private DecimalFormat format = new DecimalFormat("###");

            String []xLabels = new String[]{"January", "February", "March", "April", "May", "June"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("XXX", value+"");
                int t = ((int)value)/10;
                t = t <= 0 ? 0 : t;
                t %= 6;
                return xLabels[t];//TODO: DELETE crutch for presentation
            }
        });
        mStackedBarChart.invalidate();
    }

    private LineData getLineChartData() {
        // y-axis
        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            float val = (float) (Math.random() * 30) + 3;
            lineEntries.add(new Entry(i, val));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Line");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextColor(Color.RED);

        return new LineData(lineDataSet);
    }


    private BarData getStackedData() {
        Legend l = mStackedBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        // IMPORTANT: When using negative values in stacked bars, always make sure the negative values are in the array first
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(5,  new float[]{ -100, 60 }));
        yValues.add(new BarEntry(15, new float[]{ -602, 139 }));
        yValues.add(new BarEntry(25, new float[]{ -40, 1500 }));
        yValues.add(new BarEntry(35, new float[]{ -307, 407 }));
        yValues.add(new BarEntry(45, new float[]{ -190, 240 }));
        yValues.add(new BarEntry(55, new float[]{ -90 , 30 }));


        BarDataSet set = new BarDataSet(yValues, "Statistics per month");
        set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(7f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColors(new int[] {Color.rgb(255, 77, 0), Color.rgb(25, 184, 11)});
        set.setStackLabels(new String[]{
                "Expense", "Income"
        });


        BarData data = new BarData(set);
        data.setBarWidth(8.5f);
        return data;
    }

    private class CustomFormatter implements IValueFormatter, IAxisValueFormatter {
        private DecimalFormat mFormat;

        public CustomFormatter() { mFormat = new DecimalFormat("###"); }

        // data
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(Math.abs(value)) + "$";
        }

        // YAxis
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(Math.abs(value)) + "$";
        }
    }

    private PieData getPieChartData() {
        int count = 4;

//        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child(user.getUid())
//                .child("operations");



        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Trip");
        labels.add("Food");
        labels.add("Clothes");
        labels.add("Fishing");
        labels.add("Poker");
        labels.add("Vodka");

        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

        for(int i = 0; i < count; i++) {
            pieEntries.add(new PieEntry((float) ((Math.random() * 60) + 40), labels.get(i)));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);

        return new PieData(pieDataSet);
    }

    private SpannableString generateCenterPieChartText() {
        SpannableString s = new SpannableString("Statistics\nby charge");
        s.setSpan(new RelativeSizeSpan(2f), 0, 10, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 10, s.length(), 0);
        return s;
    }

    final String[] quarters = new String[] { "January", "February", "March", "April", "May", "June"};
    IAxisValueFormatter formatter = new IAxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return quarters[(int) value];
        }

        // we don't draw numbers, so no decimal digits needed
        public int getDecimalDigits() {  return 0; }
    };

}
