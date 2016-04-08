package com.tony.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tony.db.bean.Employee;
import com.tony.db.bean.Skill;
import com.tony.db.util.DBUtil;

import java.util.HashMap;

/**
 * Created by tony on 16/4/5.
 */
public final class DBManager {
    
    ///////////////////////////////////////////////////////////////////////////
    // fields area
    ///////////////////////////////////////////////////////////////////////////
    private static volatile DBManager mInstance;
    private  DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private HashMap<String, BaseDao> mCachedDaos;
    private Context ctx;

    ///////////////////////////////////////////////////////////////////////////
    // constructor area
    ///////////////////////////////////////////////////////////////////////////
    private DBManager(Context ctx) {
        this.ctx = ctx;
        mHelper = new DatabaseHelper(ctx);
        mDatabase = mHelper.getWritableDatabase();
        mCachedDaos = new HashMap<>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // public methods area
    ///////////////////////////////////////////////////////////////////////////
    public static DBManager getInstance(Context ctx) {
        if (mInstance == null){
            synchronized (DBManager.class){
                if (mInstance == null){
                    mInstance = new DBManager(ctx);
                }
            }
        }
        return mInstance;
    }


    public  <T> BaseDao<T> getDao(Class<T> clz){
        String key = clz.getSimpleName();
        if (mCachedDaos.containsKey(key)) {
            return mCachedDaos.get(key);
        }else {
            BaseDao<T> dao = new BaseDao<T>(ctx, clz, mDatabase);
            mCachedDaos.put(key, dao);
            return dao;
        }
    }


    public void release() {
        mDatabase.close();
        mInstance = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // inner class area
    ///////////////////////////////////////////////////////////////////////////
    class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "bingo.db";
        public static final int DB_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            DBUtil.createTable(db, Employee.class);
            DBUtil.createTable(db, Skill.class);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            DBUtil.dropTable(db, Employee.class);
            DBUtil.dropTable(db, Skill.class);
        }

    }
}
