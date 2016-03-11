package startup.irfananda.mydiary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ArcFragment extends Fragment {
    public ArcFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart_arc, container, false);
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pieChart);

        try {
            int Good = new AsyncGetDiaryConclusion(this.getActivity(),"GOOD").execute().get();
            int Bad = new AsyncGetDiaryConclusion(this.getActivity(),"BAD").execute().get();

            //configure pie chart
            pieChart.setUsePercentValues(true);
            pieChart.setDescription("");
            pieChart.setExtraOffsets(5, 10, 5, 5);
            pieChart.setDragDecelerationFrictionCoef(0.95f);

            pieChart.setCenterText(generateCenterSpannableText());

            //enable hole and configure
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);
            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);
            pieChart.setDrawCenterText(true);

            //enable rotation of chart touch
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);

            //add data chart

            ArrayList<Entry> Value = new ArrayList<>();
            Value.add(new Entry(Good, 0));
            Value.add(new Entry(Bad, 1));

            ArrayList<String> Name = new ArrayList<>();
            Name.add("Good");
            Name.add("Bad");

            //create pie data set
            PieDataSet pieDataSet = new PieDataSet(Value,"");
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setSelectionShift(5f);

            //add colors
            String color1 = getString(R.color.colorGood);
            String color2 = getString(R.color.colorBad);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor(color1));
            colors.add(Color.parseColor(color2));

//            for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                colors.add(c);

//            for (int c : ColorTemplate.JOYFUL_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.COLORFUL_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.LIBERTY_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.PASTEL_COLORS)
//                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());
            pieDataSet.setColors(colors);

            //instance pie data set now
            PieData data = new PieData(Name,pieDataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.GRAY);

            pieChart.setData(data);

            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            //undo all highlights
            pieChart.highlightValues(null);

            //update piechart
            pieChart.invalidate();

            //customize legends
            Legend legend = pieChart.getLegend();
            legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(0f);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //set chart value selected listener
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;
                if (entry.getXIndex() == 0)
                    Toast.makeText(ArcFragment.this.getActivity(), "Total Good : " + (int) entry.getVal(), Toast.LENGTH_SHORT).show();
                else if (entry.getXIndex() == 1)
                    Toast.makeText(ArcFragment.this.getActivity(), "Total Bad : " + (int) entry.getVal(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return rootView;
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Total of\nGood & Bad Day");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.0f), 8, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 8, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 5, s.length(), 0);
        return s;
    }
}