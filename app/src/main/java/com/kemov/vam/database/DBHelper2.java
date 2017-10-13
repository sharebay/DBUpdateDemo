package com.kemov.vam.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 功    能：
 * 程序员： 胡艳娇
 * 日    期：2017.08.04
 */
public class DBHelper2 extends OrmLiteSqliteOpenHelper {

    private static final int version = 2;//数据库版本

    /**
     * 用来存放DAO
     */
    private Map<String,Dao> daos = new HashMap<String,Dao>();

    public DBHelper2(Context context) {
        super(context, "default.db", null, version);
    }
    public DBHelper2(Context context, String name) {
        super(context, name, null, version);
        // java.sql.SQLException: Unable to run insert stmt on object ExcelBean
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }
    /**
     * 通过类来获得指定的DAO
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {


        Dao dao = null;
        String className = clazz.getSimpleName();
        if (!daos.containsKey(className)) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }else {
            dao = daos.get(className);
        }


        return dao;
    }

    public void deleteItemData(String tableName, Integer id) {
        getWritableDatabase()
                .execSQL("delete from " + tableName + " where id=?",
                        new Object[] { id });
    }

    public int getDataCount(String tableName) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "select count(*) from " + tableName, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    public void closeDatabase() {
        getWritableDatabase().close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
    public void deleteTable (String tableName)
    {
        getWritableDatabase().execSQL("delete from table");

    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

    /**
     * 判断某张表是否存在
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
}