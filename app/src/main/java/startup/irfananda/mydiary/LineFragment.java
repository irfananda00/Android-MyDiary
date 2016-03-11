package startup.irfananda.mydiary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.LineDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LineFragment extends Fragment implements OnSeekBarChangeListener {

    private List<Diary> diaryList;
    private LineChart lineChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    public LineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart_line, container, false);
        lineChart = (LineChart)rootView.findViewById(R.id.lineChart);

        try {
            diaryList = new AsyncGetDiary(this.getActivity()).execute().get();

            tvX = (TextView) rootView.findViewById(R.id.tvXMax);
            tvY = (TextView) rootView.findViewById(R.id.tvYMax);

            mSeekBarX = (SeekBar) rootView.findViewById(R.id.seekBar1);
            mSeekBarY = (SeekBar) rootView.findViewById(R.id.seekBar2);

            mSeekBarX.setProgress(12);
            mSeekBarY.setProgress(2);

            mSeekBarY.setOnSeekBarChangeListener(this);
            mSeekBarX.setOnSeekBarChangeListener(this);

            lineChart.setViewPortOffsets(0, 20, 0, 0);
            lineChart.setBackgroundColor(R.color.colorGood);

            lineChart.setDescription("");

            lineChart.setTouchEnabled(true);

            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);

            lineChart.setPinchZoom(false);

            lineChart.setDrawGridBackground(false);

            XAxis x = lineChart.getXAxis();
            x.setEnabled(false);

            YAxis y = lineChart.getAxisLeft();
            y.setLabelCount(6, false);
            y.setStartAtZero(false);
            y.setTextColor(Color.WHITE);
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            y.setDrawGridLines(false);
            y.setAxisLineColor(Color.WHITE);

            lineChart.getAxisRight().setEnabled(false);

//            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");

            //set data
            setData(12, 2);

            lineChart.getLegend().setEnabled(false);

            lineChart.animateXY(2000, 2000);

            // dont forget to refresh the drawing
            lineChart.invalidate();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void setData(int count, int range) {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0;i < count;i++){
//                date = format.parse(diaryList.get(i).getDate());
//                intMonth = (String) android.text.format.DateFormat.format("M", date);
            xVals.add((1990+i)+"");
        }

        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0;i < count;i++){
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 20;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        LineDataSet set = new LineDataSet(yVals,"DataSet");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleSize(4f);
        set.setCircleColor(Color.WHITE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(Color.WHITE);
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(xVals, set);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        lineChart.setData(data);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());

        // redraw
        lineChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
