package com.example.tope0_000.tope_resume2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SkillDB {

    //database constants
    public static final String DB_NAME = "skill.db";
    public static final int DB_VERSION = 1;

    //skill table constants
    public static final String SKILL_TABLE = "skill";

    public static final String SKILL_ID = "_id";
    public static final int SKILL_ID_COL = 0;

    public static final String SKILL_NAME = "name";
    public static final int SKILL_NAME_COL = 1;

    public static final String SKILL_DESCRIPTION = "description";
    public static final int SKILL_DESCRIPTION_COL = 2;

    // CREATE and DROP TABLE statements
    public static final String CREATE_SKILL_TABLE =
            "CREATE TABLE " + SKILL_TABLE + " (" +
            SKILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SKILL_NAME + " TEXT     NOT NULL UNIQUE, " +
            SKILL_DESCRIPTION + " TEXT);";

    public static final String DROP_SKILL_TABLE =
            "DROP TABLE IF EXISTS " + SKILL_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super (context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_SKILL_TABLE); }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("skill", "Upgrading db from version " + oldVersion + "to " + newVersion);
            db.execSQL(SkillDB.DROP_SKILL_TABLE);
            onCreate(db);
        }
    }

    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //constructor
    public SkillDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() { db = dbHelper.getReadableDatabase(); }

    private void openWriteableDB() { db = dbHelper.getWritableDatabase(); }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<Skill> getSkills() {
        this.openReadableDB();
        Cursor cursor = db.query(SKILL_TABLE, null, null, null, null, null, null);
        ArrayList<Skill> skills = new ArrayList<Skill>();
        while (cursor.moveToNext()) {
            skills.add(getSkillFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();
        return skills;
    }

    public Skill getSkill(int id) {
        String where = SKILL_ID + "= ?";
        String[] whereArgs = { Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(SKILL_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Skill skill = getSkillFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();
        return skill;
    }

    private static Skill getSkillFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                Skill skill = new Skill(
                        cursor.getInt(SKILL_ID_COL),
                        cursor.getString(SKILL_NAME_COL),
                        cursor.getString(SKILL_DESCRIPTION_COL));
                return skill;
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    public long insertSkill(Skill skill) {
        ContentValues cv = new ContentValues();
        cv.put(SKILL_NAME, skill.getName());
        cv.put(SKILL_DESCRIPTION, skill.getDescription());

        this.openWriteableDB();
        long rowID = db.insert(SKILL_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateSkill (Skill skill) {
        ContentValues cv = new ContentValues();
        cv.put(SKILL_NAME, skill.getName());
        cv.put(SKILL_DESCRIPTION, skill.getDescription());

        String where = SKILL_ID + "= ?";
        String[] whereArgs = { String.valueOf(skill.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(SKILL_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteSkill(long id) {
        String where = SKILL_ID + "= ?";
        String[] whereArgs = { String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(SKILL_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Methods for content provider
    public Cursor querySkills(String[] columns, String where, String[] whereArgs, String orderBy) {
        this.openReadableDB();
        return db.query(SKILL_TABLE, columns, where, whereArgs, null, null, orderBy);
    }

    public long insertSkill(ContentValues values) {
        this.openWriteableDB();
        return db.insert(SKILL_TABLE, null, values);
    }

    public int updateSkill(ContentValues values, String where, String[] whereArgs) {
        this.openWriteableDB();
        return db.update(SKILL_TABLE, values, where, whereArgs);
    }

    public int deleteSkill(String where, String[] whereArgs) {
        this.openWriteableDB();
        return db.delete(SKILL_TABLE, where, whereArgs);
    }

}
