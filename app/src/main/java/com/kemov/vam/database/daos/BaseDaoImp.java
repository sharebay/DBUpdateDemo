package com.kemov.vam.database.daos;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kemov.vam.database.DBHelper2;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RuanJian-GuoYong on 2017/8/1.
 */

public class BaseDaoImp<T,Integer> extends BaseDao<T,Integer> {

    private Class<T> clazz;
    private Map<Class<T>,Dao<T,Integer>> mDaoMap=new HashMap<Class<T>,Dao<T,Integer>>();

    public BaseDaoImp(Context context, Class<T> clazz) {
        super(context);
        this.clazz = clazz;
        this.mDaoMap = new HashMap<Class<T>,Dao<T,Integer>>();
    }

    public BaseDaoImp(Context context, Class<T> clazz, DBHelper2 argDBHelper2) {
        super(context, argDBHelper2);
        this.clazz = clazz;
        this.mDaoMap = new HashMap<Class<T>,Dao<T,Integer>>();
    }
    @Override
    public Dao<T, Integer> getDao() throws SQLException {
        Dao<T,Integer> dao=mDaoMap.get(clazz);
        if (null==dao){

            if (dbHelper != null){
                dao=dbHelper.getDao(clazz);
            }
            else if (dbHelper2 != null){
                dao=dbHelper2.getDao(clazz);
            }

            mDaoMap.put(clazz,dao);
        }
        return dao;
    }

}
