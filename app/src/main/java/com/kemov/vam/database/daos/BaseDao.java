package com.kemov.vam.database.daos;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.Where;
import com.kemov.vam.database.DBHelper;
import com.kemov.vam.database.DBHelper2;
import com.kemov.vam.utils.LogUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RuanJian-GuoYong on 2017/8/1.
 */

public abstract class BaseDao<T, Integer>{
    //public Dao<T,Integer> daoList;
    public DBHelper dbHelper ;
    public DBHelper2 dbHelper2 ;//add by hyj 2017.08.07
    protected Context mContext;

    public abstract Dao<T, Integer> getDao() throws SQLException;

    private static final String TAG = "BaseDao";
    public BaseDao(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null!");
        }

        //避免产生内存泄露，使用getApplicationContext()
        if (context instanceof Activity){
            mContext = context.getApplicationContext();
        } else {
            mContext =context;
        }

        //获得单例helper
        dbHelper = DBHelper.getHelper(mContext);
    }

    //add by hyj 2017.08.07
    public BaseDao(Context context , DBHelper2 argDBHelper2) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null!");
        }

        //避免产生内存泄露，使用getApplicationContext()
        mContext = context.getApplicationContext();

        //获得单例helper
        dbHelper2 = argDBHelper2;
    }


    //获取所有的数据
    public List<T> getAllDatas() throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = null;
        try {
            list =  dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /*
    offset跳过指定的行数
    limit限制获取指定行数
     */
    public List<T> getDatasoffset(long offset) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = null;
        try {
            list =  dao.queryBuilder().offset(offset).limit(1000l).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<T> getDatasoffset(long offset, long limit) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = null;
        try {
            list =  dao.queryBuilder().offset(offset).limit(limit).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //获取所有的数据
    public T queryForId(Integer id) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T ret = null;
        try {
            ret =  dao.queryForId(id);
            dao.closeLastIterator();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    //获取id的数据
    public T get(Integer id) throws SQLException {
        return queryForId(id);
    }

    //获取等值列表
    public List<T> getListForEq( String name, Object value) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = new ArrayList<>();
        try {
            list =  dao.queryBuilder().orderBy("id", true).where().eq(name, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<T> getListForEq( String name, Object value, long offset) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = new ArrayList<>();
        try {
            list =  dao.queryBuilder().offset(offset).orderBy("id", true).where().eq(name, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<T> getListForEqOrder( String name, Object value,String orderName, boolean bASC) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = new ArrayList<>();
        try {
            list =  dao.queryBuilder().orderBy(orderName, bASC).where().eq(name, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<T> getListForIn( String name, Iterable<?> objects) throws SQLException {
        Dao<T, Integer> dao = getDao();
        List<T> list = new ArrayList<>();
        try {
            list =  dao.queryBuilder().where().in(name, objects).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //查询所有
    public List<T>  queryLike(String name, Object value) {
        List<T> list = new ArrayList<>();
        try {
            list = (List<T>) getDao().queryBuilder().where().like(name, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list ;
    }
    //获取等值的第一条数据
    public T getFirstForEq( String name, Object value) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T t = null;
        try {
            t =  dao.queryBuilder().where().eq(name, value).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    public T getFirstForEq( String name, Object value, String name2, Object value2) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T t = null;
        try {
            t =  dao.queryBuilder().where().eq(name, value).and().eq(name2, value2).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    public T getFirstForEq( String name, Object value, String name2, Object value2,String name3, Object value3) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T t = null;
        try {
            t =  dao.queryBuilder().where().eq(name, value).and().eq(name2, value2)
                    .and().eq(name3, value3)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    public T getFirstForEq( String name, Object value, String name2, Object value2,String name3, Object value3, String name4, Object value4) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T t = null;
        try {
            t =  dao.queryBuilder().where().eq(name, value).and().eq(name2, value2)
                    .and().eq(name3, value3).and().eq(name4, value4)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    public T getFirstForEq(List<String> nameList, List<Object> valueList) throws SQLException {
        Dao<T, Integer> dao = getDao();
        T t = null;

        if (nameList.isEmpty() || valueList.isEmpty()){
            return t;
        }

        try {
            int i = 0;
            Where where = dao.queryBuilder().where().eq(nameList.get(i), valueList.get(i));

            int k = nameList.size() <= valueList.size()?nameList.size() :valueList.size();
            for (i = 1;i < k;i++)
            {
                where = where.and().eq(nameList.get(i), valueList.get(i));
            }
            t = (T) where.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
    //删除值相等的所有条目{验证发现这个方法是错误的 ，无法删除数据库}
    public boolean deleteDataEq(String name, Object value){

        try {
            Dao<T, Integer> dao = getDao();
            dao.deleteBuilder().where().eq(name, value);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //get by id
    public T getById(Integer id){
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //delete by id
    public boolean deleteById(Integer id){
        try {
            getDao().deleteById(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(T bean){
        try {
            getDao().delete(bean);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deleteDatabase()
    {
        try {
            getDao().executeRaw("drop database mydatabase; ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean addItem(T bean){
        try {
            getDao().create(bean);

            LogUtil.e(TAG, "addItem create suc");
        } catch (SQLException e) {
            e.printStackTrace();

            LogUtil.e(TAG, e.toString());

            return false;
        }

        return true;
    }

    public T createIfNotExists(T bean){

        T ret = null;
        try {
            ret = getDao().createIfNotExists(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public Dao.CreateOrUpdateStatus createOrUpdate(T bean){

        Dao.CreateOrUpdateStatus ret = null;
        try {
            ret = getDao().createOrUpdate(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /*
    添加对象的同时，更新其ID为id
     */
    public void addItem(T bean, Integer id){
        try {
            getDao().create(bean);
            getDao().updateId(bean, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItems(List<T> list){

        if (list == null || list.isEmpty()){
            return;
        }
        try {
            getDao().create(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateItem(T bean){
        try {
            getDao().update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public boolean update(T bean){
        return updateItem(bean);
    }

    public boolean updateID(T bean, Integer ID){
        try {
            getDao().updateId(bean, ID);
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
    public void refreshItem(T bean){
        try {
            getDao().refresh(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  int count(){
        int ret = 0;
        try {
            ret = (int) getDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }
    public  long countl(){
        long ret = 0l;
        try {
            ret =   getDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }
    //得到所有的TABLE
    public void ListTables ()
    {
        GenericRawResults<String[]> results =
                null;
        try {
            results = getDao().queryRaw("SELECT name FROM sqlite_master WHERE type = 'table'");
            for (String[] result : results) {
                Log.e("TAG","One table is: " + result[0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    //得到所有的TABLE
    public Map<String,String> ListTablesCount ()
    {
        Map<String,String> map = new HashMap();
        GenericRawResults<String[]> results = null;
        try {
            results = getDao().queryRaw("SELECT name  FROM sqlite_master WHERE type = 'table'");
            int i = 0;
            for (String[] result : results) {
                Log.e("TAG",i+ ": table is  " +"----------"+ result[0]);
                i++;
                String tableName = result[0];
                GenericRawResults<String[]> resultscnt = getDao().queryRaw("SELECT COUNT(*) FROM " + tableName);
                if(resultscnt!=null) {

                    for (String[] result2 : resultscnt) {
                        Log.e("TAG",tableName+ " cnt = " + result2[0]);
                        map.put(result[0], result2[0]);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }


    //得到所有的TABLE
    public Map<String,String> ListTablesMaxRowID ()
    {
        Map<String,String> map = new HashMap();
        GenericRawResults<String[]> results = null;
        try {
            results = getDao().queryRaw("SELECT *  FROM sqlite_sequence");
            int i = 0;
            for (String[] result : results) {

                i++;
                int k = result.length;
                for (int j = 0;j < k;j++){
                    Log.e("TAG",i+ ":  " +"----------"+ result[j]);
                }
                map.put(result[0], result[1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public void deleteTable (String tableName)
    {
        try {
            getDao().queryRaw("delete from  "+tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
