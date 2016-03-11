package startup.irfananda.mydiary;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditDiaryActivity extends AppCompatActivity{
    private EditText edt_story;
    private EditText edt_category;
    private String date;
    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_diary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edt_story = (EditText)findViewById(R.id.edt_story);
        edt_category= (EditText)findViewById(R.id.edt_category);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        diary= (Diary) getIntent().getSerializableExtra("Diary");

        setTitle(diary.getDate());
        date = getTitle().toString();
        edt_category.setText(diary.getCategory());
        edt_story.setText(diary.getStory());

        FloatingActionButton fab_write = (FloatingActionButton) findViewById(R.id.fab_next);
        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                diary.setCategory(edt_category.getText().toString());
                diary.setStory(edt_story.getText().toString());
                diary.setDate(date);
                DialogInput dialogInput= new DialogInput(EditDiaryActivity.this,diary);
                dialogInput.show(fragmentManager,"Category Update");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
        }
        // other menu select events may be present here

        return super.onOptionsItemSelected(item);
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
                    int id = diary.getId_story();
                    String story = diary.getStory();
                    String date = diary.getDate();
                    String category = diary.getCategory();
                    String conclusion = btn_good.getText().toString();
                    insertIntoDiary(id, story, conclusion, date, category);
                    finish();
                }
            });

            final Button btn_bad = (Button)rootView.findViewById(R.id.btn_bad);
            btn_bad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = diary.getId_story();
                    String story = diary.getStory();
                    String date = diary.getDate();
                    String category = diary.getCategory();
                    String conclusion = btn_bad.getText().toString();
                    insertIntoDiary(id, story, conclusion, date, category);
                    finish();
                }
            });

            return rootView;
        }

        private void openDB(){
            myDb = new DBAdapter(context);
            myDb.open();
        }

        private void insertIntoDiary(int id, String story, String conclusion, String date, String category){
            //insert into diary
            openDB();
            myDb.updateDiary(id, story, conclusion, date, category);
            Toast.makeText(context, "Data Saved", Toast.LENGTH_LONG).show();
            myDb.close();
            Intent intent = new Intent(EditDiaryActivity.this, DetailDiaryActivity.class);
            intent.putExtra("Diary", diary);
            startActivity(intent);
        }
    }

}

