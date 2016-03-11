package startup.irfananda.mydiary;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    //Column Names for Table Diary
    public static final String id_story= "id_story";
    public static final String story = "story";
    public static final String conclusion = "conclusion";
    public static final String date = "date";
    public static final String category = "category";
    //Column Names for Table Category
    public static final String id_category= "id_category";
    public static final String name = "name";
    public static final String id_icon = "id_icon";

    public static final String[] DIARY_FIELD = new String[] {
            id_story,
            story,
            conclusion,
            date,
            category
    };

    public static final String[] CATEGORY_FIELD = new String[] {
            id_category,
            name,
            id_icon
    };

    //DataBase Info:
    public static final String DATABASE_NAME = "dataUser";
    public static final String DATABASE_TABLE_DIARY = "Diary";
    public static final String DATABASE_TABLE_CATEGORY = "Category";
    public static final int DATABASE_VERSION = 4; //The version number must be incremented each time a change to DB structure occurs

    //SQL statement to create table Diary
    private static final String DATABASE_CREATE_DIARY =
            "CREATE TABLE "+DATABASE_TABLE_DIARY+" ("
                    +id_story+" INTEGER PRIMARY KEY AUTOINCREMENT, "//0
                    +story+" TEXT,"//1
                    +conclusion+" TEXT,"//2
                    +date+" DATETIME,"//3
                    +category+" TEXT"//3
                    + " ); ";

    //SQL statement to create table Category
    private static final String DATABASE_CREATE_CATEGORY=
            "CREATE TABLE "+DATABASE_TABLE_CATEGORY+" ("
                    +id_category+" INTEGER PRIMARY KEY AUTOINCREMENT,"//0
                    +name+" TEXT,"//1
                    +id_icon+" TEXT"//2
                    + " ); ";

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        myDBHelper = new DatabaseHelper(ctx);
    }

    //Open the database connection
    public DBAdapter open(){
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    //Close the database connection
    public void close(){
        myDBHelper.close();
    }

    //Add a new set of values to be inserted into the table Diary
    public long insertDiary(String story, String conclusion, String date, String category){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBAdapter.story,story);
        initialValues.put(DBAdapter.conclusion,conclusion);
        initialValues.put(DBAdapter.date,date);
        initialValues.put(DBAdapter.category,category);

        //Insert the data into the table
        return db.insert(DATABASE_TABLE_DIARY, null, initialValues);
    }

    //Add a new set of values to be inserted into the table Category
    public long insertCategory(String name, String id_icon){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBAdapter.name,name);
        initialValues.put(DBAdapter.id_icon,id_icon);

        //Insert the data into the table
        return db.insert(DATABASE_TABLE_CATEGORY, null, initialValues);
    }

    //Delete a row from the table Diary, by rowId (primary key)
    public boolean deleteDiary(String rowId){
        return db.delete(DATABASE_TABLE_DIARY, id_story + "=" + rowId, null) > 0;
    }

    //Delete a row from the table Category, by rowId (primary key)
    public boolean deleteCategory(String rowId){
        return db.delete(DATABASE_TABLE_CATEGORY, id_category + "=" + rowId, null) > 0;
    }

    public void deleteAllDiary(){
        Cursor c = getAllRowsDiary();
        long rowId= c.getColumnIndexOrThrow(id_story);
        if (c.moveToFirst()){
            do{
                deleteDiary(c.getString((int) rowId));
            }while (c.moveToNext());
        }
        c.close();
    }

    public void deleteAllCategory(){
        Cursor c = getAllRowsCategory();
        long rowId= c.getColumnIndexOrThrow(id_category);
        if (c.moveToFirst()){
            do{
                deleteCategory(c.getString((int) rowId));
            }while (c.moveToNext());
        }
        c.close();
    }

    //Return all data in the table diary.
    public Cursor getAllRowsDiary() {
        return db.query(DATABASE_TABLE_DIARY, DIARY_FIELD,
                null, null, null, null, id_story+" DESC", null);
    }

    //Return all data in the table diary.
    public Cursor getAllRowsDiary(String conclusion) {
        return db.query(DATABASE_TABLE_DIARY, DIARY_FIELD,
                DBAdapter.conclusion +"=?", new String[]{conclusion}, null, null, date+" DESC", null);
    }

    //Return all data in the table category.
    public Cursor getAllRowsCategory() {
        return db.query(DATABASE_TABLE_CATEGORY, CATEGORY_FIELD,
                null, null, null, null, null, null);
    }

    public Cursor getRowDiary(int rowId){
        Cursor c = db.query(true, DATABASE_TABLE_DIARY, DIARY_FIELD,
                id_story + "=" + rowId, null, null, null, null, null);
        if(c.getCount() > 0 ){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowDiary(String date){
        return db.query(DATABASE_TABLE_DIARY, DIARY_FIELD,
                DBAdapter.date + "=?", new String[]{date}, null, null, null, null);
    }

    public Cursor getRowCategory(int rowId){
        Cursor c = db.query(true, DATABASE_TABLE_CATEGORY, CATEGORY_FIELD,
                id_category + "=" + rowId, null, null, null, null, null);
        if(c.getCount() > 0 ){
            c.moveToFirst();
        }
        return c;
    }

    //change an existing row to be equal to new data
    public boolean updateDiary(int id_story, String story, String conclusion, String date, String category){
        ContentValues newValues = new ContentValues();
        newValues.put(DBAdapter.story, story);
        newValues.put(DBAdapter.conclusion, conclusion);
        newValues.put(DBAdapter.date, date);
        newValues.put(DBAdapter.category, category);

        //insert it into the database
        return db.update(DATABASE_TABLE_DIARY, newValues, DBAdapter.id_story + "=" + id_story, null) > 0;
    }

    //change an existing row to be equal to new data
    public boolean updateCategory(int id_category, String name, String id_icon){
        ContentValues newValues = new ContentValues();
        newValues.put(DBAdapter.name, name);
        newValues.put(DBAdapter.id_icon, id_icon);

        //insert it into the database
        return db.update(DATABASE_TABLE_CATEGORY, newValues, DBAdapter.id_category+ "=" + id_category, null) > 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db){
            _db.execSQL(DATABASE_CREATE_DIARY);
            _db.execSQL(DATABASE_CREATE_CATEGORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion){
            //Destroy old database;
            _db.execSQL("DROP TABLE IF EXISTs " + DATABASE_TABLE_DIARY);
            _db.execSQL("DROP TABLE IF EXISTs " + DATABASE_TABLE_CATEGORY);

            //Rescreate new database;
            onCreate(_db);
        }
    }
}

