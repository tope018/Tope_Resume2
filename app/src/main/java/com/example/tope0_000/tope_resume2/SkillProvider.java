package com.example.tope0_000.tope_resume2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class SkillProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.tope0_000.tope_resume2.provider";

    public static final int NO_MATCH = -1;
    public static final int ALL_SKILLS_URI = 0;
    public static final int SINGLE_SKILL_URI = 1;

    private SkillDB db;
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        db = new SkillDB(getContext());

        uriMatcher = new UriMatcher(NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "skills", ALL_SKILLS_URI);
        uriMatcher.addURI(AUTHORITY, "skills/#", SINGLE_SKILL_URI);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String where, String[] whereArgs, String orderby) {
        switch(uriMatcher.match(uri)) {
            case ALL_SKILLS_URI:
                return db.querySkills(columns, where, whereArgs, orderby);
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_SKILLS_URI:
                return "vnd.android.cursor.dir/vnd.example.tope0_000.tope_resume2.tasks";
            case SINGLE_SKILL_URI:
                return "vnd.android.cursor.item/vnd.example.tope0_000.tope_resume2.tasks";
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case ALL_SKILLS_URI:
                long insertId = db.insertSkill(values);
                getContext().getContentResolver().notifyChange(uri, null);
                return uri.buildUpon().appendPath(Long.toString(insertId)).build();
            default:
                throw new UnsupportedOperationException("URI: " + uri + " is not supported.");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        int updateCount;
        switch (uriMatcher.match(uri)) {
            case SINGLE_SKILL_URI:
                String skillId = uri.getLastPathSegment();
                String where2 = "_id = ?";
                String[] whereArgs2 = { skillId };
                updateCount = db.updateSkill(values, where2, whereArgs2);
                getContext().getContentResolver().notifyChange(uri, null);
                return updateCount;
            case ALL_SKILLS_URI:
                updateCount = db.updateSkill(values, where, whereArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return updateCount;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int deleteCount;
        switch (uriMatcher.match(uri)) {
            case SINGLE_SKILL_URI:
                String skillId = uri.getLastPathSegment();
                String where2 = "_id = ?";
                String[] whereArgs2 = { skillId };
                deleteCount = db.deleteSkill(where2, whereArgs2);
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteCount;
            case ALL_SKILLS_URI:
                deleteCount = db.deleteSkill(where, whereArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteCount;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }
}
