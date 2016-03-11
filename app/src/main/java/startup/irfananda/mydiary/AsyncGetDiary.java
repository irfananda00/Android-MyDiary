package startup.irfananda.mydiary;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AsyncGetDiary extends AsyncTask<Void,Void,List<Diary>> {

    private Context context;
    private Dialog loadingDialog;
    private DBAdapter myDb;
    private String date;

    public AsyncGetDiary(Context context) {
        this.context = context;
    }

    public AsyncGetDiary(Context context,String date) {
        this.context = context;
        this.date = date;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = ProgressDialog.show(context, "Retrieving data", "Loading...");
    }

    @Override
    protected List<Diary> doInBackground(Void... params) {
        List<Diary> diaries = new ArrayList<>();

        openDB();

        if(date == null) {
            Cursor cursor = myDb.getAllRowsDiary();
            if (cursor.getCount() > 0 ) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Diary diary = new Diary();
                        diary.setId_story(cursor.getInt(cursor.getColumnIndex(DBAdapter.id_story)));
                        diary.setStory(cursor.getString(cursor.getColumnIndex(DBAdapter.story)));
                        diary.setConclusion(cursor.getString(cursor.getColumnIndex(DBAdapter.conclusion)));
                        diary.setDate(cursor.getString(cursor.getColumnIndex(DBAdapter.date)));
                        diary.setCategory(cursor.getString(cursor.getColumnIndex(DBAdapter.category)));

                        diaries.add(diary);
                        cursor.moveToNext();
                    }
                }
            } else {
                Log.i("DBAdapter", "failed while retrieving data getAllRowsDiary");
                return null;
            }
        }else {
            Cursor cursor = myDb.getRowDiary(date);
            if (cursor.getCount() > 0 ) {
                if (cursor.moveToFirst()) {
                    Diary diary = new Diary();
                    diary.setId_story(cursor.getInt(cursor.getColumnIndex(DBAdapter.id_story)));
                    diary.setStory(cursor.getString(cursor.getColumnIndex(DBAdapter.story)));
                    diary.setConclusion(cursor.getString(cursor.getColumnIndex(DBAdapter.conclusion)));
                    diary.setDate(cursor.getString(cursor.getColumnIndex(DBAdapter.date)));
                    diary.setCategory(cursor.getString(cursor.getColumnIndex(DBAdapter.category)));

                    diaries.add(diary);
                }
            } else {
                Log.i("DBAdapter", "failed while retrieving data getRowDiaryDate");
                return null;
            }
        }

        myDb.close();

        return diaries;
    }

    @Override
    protected void onPostExecute(List<Diary> diaries) {
        super.onPostExecute(diaries);
        if(loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    private void openDB(){
        myDb = new DBAdapter(context);
        myDb.open();
    }
}
