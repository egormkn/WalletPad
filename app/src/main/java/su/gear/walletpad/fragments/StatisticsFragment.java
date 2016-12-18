package su.gear.walletpad.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import su.gear.walletpad.R;

public class StatisticsFragment extends Fragment {


    private LineChart mLineChart;
    private PieChart  mPieChart;

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

        mLineChart.setData(getLineChartData());
        mLineChart.getDescription().setEnabled(false);
        mLineChart.animateY(1000);
        mLineChart.setDrawGridBackground(false);
        Legend mLineChartLegent = mLineChart.getLegend();
        mLineChartLegent.setEnabled(false);
        /////////////////////////////////////////////
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);                   // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        /////////////////////////////////////////////
        mLineChart.invalidate();

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
    }

    private LineData getLineChartData() {
        // x-axis
        /*ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/

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

    private PieData getPieChartData() {
        int count = 4;

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
