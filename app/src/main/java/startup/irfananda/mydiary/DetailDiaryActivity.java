package startup.irfananda.mydiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DetailDiaryActivity extends AppCompatActivity {

    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        diary= (Diary) getIntent().getSerializableExtra("Diary");

        setTitle(diary.getDate());

        TextView txt_story = (TextView) findViewById(R.id.txt_story);
        txt_story.setText(diary.getStory());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            AsyncDeleteDiary deleteDiary = new AsyncDeleteDiary(DetailDiaryActivity.this,diary);
                            deleteDiary.execute();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailDiaryActivity.this);
            builder.setMessage("This diary will be deleted. Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }else if(id == R.id.action_edit){
            Intent intent = new Intent(DetailDiaryActivity.this, EditDiaryActivity.class);
            intent.putExtra("Diary", diary);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == android.R.id.home ) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
