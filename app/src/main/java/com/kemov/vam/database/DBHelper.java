package com.kemov.vam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kemov.vam.database.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//        DBHelper dbHelper = new DBHelper(new DatabaseContext(
//                MainActivity.this, ""), "dddd.db");


/**
 * Created by PENG on 2017/3/22.
 *
 * mdf:
 * 2017.06.05:检验...任务的状态“异常”删除
 *
 *
 ORDER BY
 根据指定列名排序，降序，升序
 使用示范：mDao.queryBuilder().orderBy("id", false).query(); //参数false表示降序，true表示升序。
 对应SQL：SELECT * FROM `t_person` ORDER BY `id` DESC（降序）


 　常见标签：
 DatabaseTable：通过其中的tableName属性指定数据库名称
 DatabaseField：代表数据表中的一个字段
 ForeignCollectionField：一对多关联，表示一个UserBean关联着多个ArticleBean（必须使用ForeignCollection集合）


 　常见属性：
 id：当前字段是不是id字段（一个实体类中只能设置一个id字段）
 columnName：表示当前属性在表中代表哪个字段
 generatedId：设置属性值在数据表中的数据是否自增
 useGetSet：是否使用Getter/Setter方法来访问这个字段
 canBeNull：字段是否可以为空，默认值是true
 unique：是否唯一
 defaultValue：设置这个字段的默认值
 foreignColumnName：外键约束指向的类中的属性名
 foreign：当前字段是否是外键
 foreignAutoRefresh：如果这个属性设置为true，在关联查询的时候就不需要再调用refresh()方法了
 */

//2017.07.03  创建验收总结表格，（外键：验收任务表格）  1.3 17070301
//2017.07.04  创建安全措施文件夹下5个表格. (T_JY_AQCSP	、T_JY_AQCSP_STATUS	、T_JY_AQCSP_CONTENT、T_JY_AQCSP_DOC_GROUP、T_JY_AQCSP_DOC)
//2017.07.05  增加验收、巡检的doc表格。

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "mydatabase.db";

    private String mDATABASE_NAME = "mydatabase.db";


    private static  int DATABASE_VERSION = 2;
    /*20170914 添加设备型号表和增加厂站表的字段 设置 DATABASE_VERSION = 2
    * 20170930 修改 T_XJ_RESULT_DOC表的外键resultRecordID为非唯一，否则一个文件只能在巡检表中用一次。ver=3
    *
    * */

    private static final int DROP_ALL = 0;
    private static final int DROP_ALL_TEST = 1;
    private static final  int DROP_JY = 2;
    private static final int DROP_XJ = 3;
    private static final int DROP_YS = 4;
    private static final int DROP_FAULT = 5;
    private static final int DROP_ALL_TEST_RECORD = 6;
    private int UPGRADE_TYPE = DROP_ALL;


    //
    private  String file_database_path = Environment.getExternalStorageDirectory() +"/Kemov/dm5800/database/filedb"; //文件数据库存放路径
    private  String sql_create_data = "CREATE TABLE testinfo (id TEXT PRIMARY KEY, message TEXT, offset INTEGER, timestamp INTEGER);";
    private  String sql_insert_data = "INSERT OR REPLACE INTO MAIN.testinfo VALUES('%s', '%s', %d, %d);";
    private  String sql_delete_data = "DELETE FROM MAIN.testinfo WHERE id = '%s'; DELETE FROM filedb.testinfo WHERE id = '%s';"; //删除数据库，需同时删除内存、文件数据库中的内容
    private  String sql_update_data = "UPDATE MAIN.testinfo SET message = '%s', offset = %d, timestamp = %d where id = '%s'; UPDATE filedb.testinfo SET message = '%s', offset = %d, timestamp = %d where id = '%s';";//更新数据库，需同时更新内存、文件数据库中的内容
    private  String sql_search_data = "SELECT * FROM MAIN.testinfo WHERE timestamp BETWEEN %d AND %d union SELECT * FROM testdb.testinfo WHERE timestamp BETWEEN %d AND %d;"; //查找数据库，将内存、文件数据库中查找出的内容合并
    private  String sql_transfer_data = "INSERT OR REPLACE INTO filedb.testinfo SELECT * FROM testinfo;";   //将内存数据库中的信息同步到文件数据库中
    private  String sql_delete_memory_table = "DELETE FROM testinfo;";  //内存数据库中的内容同步结束后，清空


    /**
     * 用来存放DAO
     */
    private Map<String,Dao> daos = new HashMap<String,Dao>();

    private static DBHelper instance ;

    private Context context ;


    private ConnectionSource connectionSource;

    public void addDATABASE_VERSION(){
        DATABASE_VERSION++;
    }


    public static synchronized  DBHelper getHelper (Context context)
    {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;

    }

    private DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
        mDATABASE_NAME = DATABASE_NAME;

    }


    public DBHelper(Context context, String name)
    {
        super(context,name,null,DATABASE_VERSION);
        this.context = context;
        mDATABASE_NAME = name;

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.e(TAG, "gy.onCreate: sqLiteDatabase.getVersion()="+sqLiteDatabase.getVersion());
        this.connectionSource = connectionSource;
        //
        sqLiteDatabase.setVersion(DATABASE_VERSION);
        Log.e(TAG, "gy.onCreate: sqLiteDatabase.getVersion()="+sqLiteDatabase.getVersion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        this.connectionSource = connectionSource;
        Log.e(TAG, "gy.onUpgrade: sqLiteDatabase.isReadOnly()? = "+sqLiteDatabase.isReadOnly());
        Log.e(TAG, "gy.onUpgrade: oldVersion="+oldVersion);

        String creatStation = "CREATE TABLE \"PL_Station\" (`Comment` VARCHAR , " +
                "`CompanyId` VARCHAR , `DCSystemName` VARCHAR , `DCType` VARCHAR , `EndDate` VARCHAR , " +
                "`GenNums` VARCHAR , `HighMVA` VARCHAR , `InService` VARCHAR , `IsDependence` VARCHAR , " +
                "`IsMonitor` VARCHAR , `IsRemote` VARCHAR , `LowMVA` VARCHAR , `MVA` VARCHAR , `MW` VARCHAR , " +
                "`MaxMW` VARCHAR , `MidMVA` VARCHAR , `Name` VARCHAR NOT NULL , `PropertyId` VARCHAR , " +
                "`SchUnitId` VARCHAR , `StartDate` VARCHAR , `SubType` VARCHAR NOT NULL , `Type` VARCHAR NOT NULL , " +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT , `VoltageLevel` INTEGER," +
                "`PositionId` VARCHAR default(''),`DCVoltageLevel` VARCHAR default('')," +
                "`ConnectedGrid` VARCHAR default(''),`OperDepartID` VARCHAR default(''));";

        // PositionId DCVoltageLevel ConnectedGrid OperDepartID
        if (oldVersion<2 && !sqLiteDatabase.isReadOnly()){
            String rename = "alter table PL_Station rename to _temp_PL_Station";
            /** 注意 '' (#默认值#) 是用来补充原来不存在的数据的，为默认值，表里边显示的默认值不会被写入的！！！*/
            String copyData = "insert into PL_Station select *,'','','','' from _temp_PL_Station;";
            String dropTemp = "DROP TABLE  if exists _temp_PL_Station;";

            try {
                //首先重命名数据库
                sqLiteDatabase.execSQL(rename);
                Log.e(TAG, "gy.onUpgrade: execSQL(rename)");

                //新增的设备信息表
                TableUtils.createTableIfNotExists(connectionSource, User.class);
                Log.e(TAG, "gy.onUpgrade: createTableIfNotExists(connectionSource, PL_BHXH.class)");
                //创建新的数据库
                TableUtils.createTable(connectionSource, User.class);
                //sqLiteDatabase.execSQL(creatStation);
                Log.e(TAG, "gy.onUpgrade: createTable(connectionSource, PL_Station.class)");
                /*//此处加这个是因为执行上一句SQL理应是不会备份临时表的数据的，可是他却备份了。故此处再清除下新表
                sqLiteDatabase.execSQL("delete from if exists PL_Station;");
                Log.e(TAG, "gy.onUpgrade: execSQL(\"delete from PL_Station;\")");
                //拷贝数据
                sqLiteDatabase.execSQL(copyData);
                Log.e(TAG, "gy.onUpgrade: execSQL(copeData)");*/
                //删除临时表
                sqLiteDatabase.execSQL(dropTemp);
                Log.e(TAG, "gy.onUpgrade: execSQL(dropTemp);");
                sqLiteDatabase.setVersion(2);
                Log.e(TAG, "gy.onUpgrade: setVersion(2),and sqLiteDatabase.getVersion()="+sqLiteDatabase.getVersion());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (oldVersion>=2 && oldVersion<3){
            
        }

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

        instance = null;
    }

    public static  String getDefaultDBFilePath(){
        return "data/data/com.kemov.elecmobiletools/databases/" + DATABASE_NAME;
    }

    public static String getDefaultDBName(){

        return  DATABASE_NAME;
    }


    /*
    delete from TableName;  //清空数据
    update sqlite_sequence SET seq = 0 where name ='TableName';//自增长ID为0
     */
    public void deleteTable (String tableName)
    {
        getWritableDatabase().execSQL("delete from "+tableName);

    }

}
