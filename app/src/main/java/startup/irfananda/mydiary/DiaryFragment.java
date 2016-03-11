package startup.irfananda.mydiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class DiaryFragment extends Fragment{

    private View rootView;
    private List<Diary> diaryList;
    private RecyclerView rv;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private String dateNow;

    public DiaryFragment(String dateNow) {
        this.dateNow = dateNow;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        getActivity().setTitle("Diaries");

        rv = (RecyclerView)rootView.findViewById(R.id.rv);

        updateLabel(null);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Diary diary = diaryList.get(position);
                Intent intent = new Intent(rootView.getContext(), DetailDiaryActivity.class);
                intent.putExtra("Diary", diary);
                startActivity(intent);
            }
        }));

        FloatingActionButton fab_write = (FloatingActionButton) rootView.findViewById(R.id.fab_write);
        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Diary> diaryList = new AsyncGetDiary(rootView.getContext(),dateNow).execute().get();
                    if(diaryList!=null) {
                        Diary diary = diaryList.get(0);
                        Intent intent = new Intent(rootView.getContext(), EditDiaryActivity.class);
                        intent.putExtra("Diary", diary);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(rootView.getContext(), InputDiaryActivity.class);
                        intent.putExtra("Date", dateNow);
                        startActivity(intent);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        myCalendar = Calendar.getInstance();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select_date) {
            selectDate();
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectDate() {
        // TODO Auto-generated method stub
        new DatePickerDialog(rootView.getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        //datepicker
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };
    }

    private void updateLabel(Calendar myCalendar) {
        String tanggal_input = "none";

        if(myCalendar!=null) {
            String myFormat = "yyyy-M-d"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            tanggal_input = sdf.format(myCalendar.getTime());
        }

        if(tanggal_input.equals("none")) {
            try {
                diaryList = new AsyncGetDiary(this.getActivity()).execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            try {
                diaryList = new AsyncGetDiary(this.getActivity(),tanggal_input).execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if(diaryList!=null) {
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            rv.setLayoutManager(llm);

            DiaryAdapter diaryAdapter = new DiaryAdapter(diaryList);
            rv.setAdapter(diaryAdapter);
        }
    }
}
