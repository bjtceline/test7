package com.example.test7;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 内容提供者
 */
public class PersonContentProvider extends ContentProvider {
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PERSONS = 1;
    private static final int PERSON = 2;
    private DBOpenHandler dbOpenHandler;

    // 静态初始化 Uri 匹配模式
    static {
        // content://wjh.android.provider.personprovider/person : 表示对 person 表所有数据进行操作
        MATCHER.addURI("wjh.android.provider.personprovider", "person", PERSONS);

        // content://wjh.android.provider.personprovider/person/3 : 表示对 Perosn 表 Id 为 3 的单体记录进行操作
        MATCHER.addURI("wjh.android.provider.personprovider", "person/#", PERSON);
    }

    /**
     * 其它应用可以通过此方法对内容提供者删除数据
     */
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        int num = 0;
        switch (MATCHER.match(uri)) {
            case PERSONS :
                num = db.delete("person", selection, selectionArgs);
                break;
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where ="personid=" + id;
                if(selection != null && !"".equals(selection)) {
                    where = where + "and" + selection;
                }
                num = db.delete("person", where, selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("Unkown Uri :" + uri);
        }
        return num;
    }

    /**
     * 获取内容提供者内容的类型
     */
    @Override
    public String getType(Uri uri) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PERSONS :
                // vnd.android.cursor.dir : 集合类型
                return "vnd.android.cursor.dir/person";
            case PERSON:
                // vnd.android.cursor.item : 单条记录
                return "vnd.android.cursor.item/person";
            default :
                throw new IllegalArgumentException("Unkown Uri :" + uri);
        }
    }
    /**
     * 其它应用可以通过此方法对内容提供者添加数据
     */
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PERSONS :
                long rowid = db.insert("person", "name", contentValues);
                return ContentUris.withAppendedId(uri, rowid);
            default :
                throw new IllegalArgumentException("Unkown Uri :" + uri);
        }
    }
    public boolean onCreate() {
        dbOpenHandler = new DBOpenHandler(getContext());
        return true;
    }
    /**
     * 其它应用可以通过此方法对内容提供者查询数据
     */
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PERSONS :
                return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where ="personid=" + id;
                if(selection != null && !"".equals(selection)) {
                    where = where + "and" + selection;
                }
                return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
            default :
                throw new IllegalArgumentException("Unkown Uri :" + uri);
        }
    }
    /**
     * 其它应用可以通过此方法对内容提供者更新数据
     */
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        int num = 0;
        switch (MATCHER.match(uri)) {
            case PERSONS :
                num = db.update("person", values, selection, selectionArgs);
                break;
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where ="personid=" + id;
                if(selection != null && !"".equals(selection)) {
                    where = where + "and" + selection;
                }
                num = db.update("person", values, where, selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("Unkown Uri :" + uri);
        }
        return num;
    }
}