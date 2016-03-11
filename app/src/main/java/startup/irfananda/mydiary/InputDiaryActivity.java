package startup.irfananda.mydiary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputDiaryActivity extends AppCompatActivity {

    private EditText edt_story;
    private EditText edt_category;
    private String date;
    private DatePickerDialog.OnDateSetListener datePick;
    private Calendar cal;
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);
        // set current date into textview
        date = String.valueOf(new StringBuilder()
                // Month is 0 based, just add 1
                .append(yy).append("-").append(mm + 1).append("-").append(dd));

        setTitle(date);

        edt_story = (EditText)findViewById(R.id.edt_story);
        edt_category= (EditText)findViewById(R.id.edt_category);

        FloatingActionButton fab_write = (FloatingActionButton) findViewById(R.id.fab_next);
        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Diary diary = new Diary();
                diary.setCategory(edt_category.getText().toString());
                diary.setStory(edt_story.getText().toString());
                diary.setDate(date);
                DialogInput dialogInput= new DialogInput(InputDiaryActivity.this,diary);
                dialogInput.show(fragmentManager,"Category Input");
            }
        });

        //datepicker
        datePick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(cal);
            }

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fragment_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
        }else if (item.getItemId() == R.id.action_select_date) {
            // TODO Auto-generated method stub
            new DatePickerDialog(this, datePick, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        }
        // other menu select events may be present here

        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(Calendar cal) {
        String myFormat = "yyyy-M-d"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date = sdf.format(cal.getTime());
        Log.i("infoirfan",date);
        this.setTitle(date);
    }

    class DialogInput extends DialogFragment {

        private Diary diary;
        private Context context;
        private DBAdapter myDb;

        public DialogInput(Context context, Diary diary) {
            this.context = context;
            this.diary = diary;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dialog_input,null);

            openDB();

            getDialog().setTitle("About today ?");

            final Button btn_good = (Button)rootView.findViewById(R.id.btn_good);
            btn_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String story = diary.getStory();
                    String date = diary.getDate();
                    String category = diary.getCategory();
                    String conclusion = btn_good.getText().toString();
                    insertIntoDiary(story, conclusion, date, category);
                    finish();
                }
            });

            final Button btn_bad = (Button)rootView.findViewById(R.id.btn_bad);
            btn_bad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String story = diary.getStory();
                    String date = diary.getDate();
                    String category = diary.getCategory();
                    String conclusion = btn_bad.getText().toString();
                    insertIntoDiary(story, conclusion, date, category);
                    finish();
                }
            });

            return rootView;
        }

        private void openDB(){
            myDb = new DBAdapter(context);
            myDb.open();
        }

        private void insertIntoDiary(String story, String conclusion, String date, String category){
            //insert into diary
            openDB();
            myDb.insertDiary(story, conclusion, date, category);
            Toast.makeText(context, "Data Saved", Toast.LENGTH_LONG).show();
            myDb.close();
        }

    }

}

