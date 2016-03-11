package startup.irfananda.mydiary;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class AsyncDeleteDiary extends AsyncTask<Void,Void,List<Diary>> {

    private Context context;
    private Dialog loadingDialog;
    private DBAdapter myDb;
    private Diary diary;

    public AsyncDeleteDiary(Context context,Diary diary) {
        this.context = context;
        this.diary = diary;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = ProgressDialog.show(context, "Deleting data", "Loading...");
    }

    @Override
    protected List<Diary> doInBackground(Void... params) {
        List<Diary> diaries = new ArrayList<>();

        openDB();

        myDb.deleteDiary(String.valueOf(diary.getId_story()));

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
