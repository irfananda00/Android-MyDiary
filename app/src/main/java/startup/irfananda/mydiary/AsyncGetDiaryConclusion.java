package startup.irfananda.mydiary;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncGetDiaryConclusion extends AsyncTask<Void,Void,Integer> {

    private Context context;
    private Dialog loadingDialog;
    private DBAdapter myDb;
    private String conclusion;

    public AsyncGetDiaryConclusion(Context context, String conclusion) {
        this.context = context;
        this.conclusion = conclusion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = ProgressDialog.show(context, "Retrieving data", "Loading...");
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int num = 0;

        openDB();

            Cursor cursor = myDb.getAllRowsDiary(conclusion);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        num++;
                        cursor.moveToNext();
                    }
                }
            } else {
                Log.i("DBAdapter", "failed while retrieving data getRowDiary conclusion");
            }

        myDb.close();

        return num;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    private void openDB(){
        myDb = new DBAdapter(context);
        myDb.open();
    }
}
