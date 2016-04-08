package com.tony.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tony.db.annotation.Column;
import com.tony.db.annotation.Table;
import com.tony.db.util.DBUtil;
import com.tony.db.util.LogUtils;
import com.tony.db.util.SerializeUtil;
import com.tony.db.util.ValidateUtil;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 16/4/5.
 */
public class BaseDao<T> {

    ///////////////////////////////////////////////////////////////////////////
    // fields area
    ///////////////////////////////////////////////////////////////////////////
    private SQLiteDatabase mDatabase;
    private Class<T> clz;
    private String mTableName;
    private String mIdName;
    private Field[] mColumnFields;
    private Field mIdField;
    private Context ctx;
    private List<Field> mForeignFields = Collections.EMPTY_LIST;

    ///////////////////////////////////////////////////////////////////////////
    // constructor area
    ///////////////////////////////////////////////////////////////////////////
    public BaseDao(Context ctx, Class<T> clz, SQLiteDatabase mDatabase) {
        this.ctx = ctx;
        this.clz = clz;
        this.mDatabase = mDatabase;
        try {
            mTableName = DBUtil.getTableName(clz);
            mIdName = DBUtil.getIdColumnName(clz);
            mIdField = clz.getDeclaredField(mIdName);
            mIdField.setAccessible(true);
            mColumnFields = clz.getDeclaredFields();
            mForeignFields = DBUtil.getForeignFields(mColumnFields);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // public method area
    ///////////////////////////////////////////////////////////////////////////

    /**
     * new or update t
     *
     * @param t
     */
    public void newOrUpdate(T t) {
        ContentValues values = new ContentValues();

        try {
            for (Field field : mColumnFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    Class<?> columnTypeClz = field.getType();
                    String columnName = DBUtil.getColumnName(field);
                    if (columnTypeClz == String.class) {//String
                        String filedValue = (String) field.get(t);
                        if (ValidateUtil.isValidate(filedValue)) {
                            values.put(columnName, filedValue);
                        }
                    } else if (columnTypeClz == int.class || columnTypeClz == Integer.class) {//int
                        values.put(columnName, field.getInt(t));
                    } else if (columnTypeClz == long.class || columnTypeClz == Long.class) {//long
                        values.put(columnName, field.getLong(t));
                    } else if (columnTypeClz == short.class || columnTypeClz == Short.class) {//short
                        values.put(columnName, field.getShort(t));
                    } else if (columnTypeClz == boolean.class || columnTypeClz == Boolean.class) {//boolean
                        values.put(columnName, field.getBoolean(t));
                    } else if (columnTypeClz == byte.class || columnTypeClz == Byte.class) {//byte
                        values.put(columnName, field.getByte(t));
                    } else if (columnTypeClz == double.class || columnTypeClz == Double.class) {//double
                        values.put(columnName, field.getDouble(t));
                    } else if (columnTypeClz == float.class || columnTypeClz == Float.class) {//float
                        values.put(columnName, field.getFloat(t));
                    } else {
                        Column column = field.getAnnotation(Column.class);
                        Column.ColumnType columnAnnotationType = column.type();
                        if (columnAnnotationType == Column.ColumnType.SERIALIZABLE) {//SERIALIZABLE
                            values.put(columnName,
                                    SerializeUtil.serialize(field.get(t)));
                        } else if (columnAnnotationType == Column.ColumnType.ONE2ONE) {//one to one
                            Object one2oneObj = field.get(t);
                            Class one2oneObjClz = one2oneObj.getClass();
                            if (one2oneObjClz.isAnnotationPresent(Table.class)) {//将外键写入到该表中
                                String one2oneIdColumnName = DBUtil.getIdColumnName(one2oneObjClz);
                                Field one2oneIdField = one2oneObjClz.getDeclaredField(one2oneIdColumnName);
                                one2oneIdField.setAccessible(true);
                                String one2oneIdFieldValue = (String) one2oneIdField.get(one2oneObj);
                                values.put(columnName, one2oneIdFieldValue);
                            }
                            //写入另外一张表中
                            DBManager.getInstance(ctx).getDao(one2oneObjClz).newOrUpdate(one2oneObj);
                        } else if (columnAnnotationType == Column.ColumnType.ONE2MANY) {//one to many
                            //获取id value
                            String idValue = (String) mIdField.get(t);
                            //获取list数组
                            List<Object> one2manyListObj = (List<Object>) field.get(t);

                            if (!ValidateUtil.isValidate(one2manyListObj)) continue;

                            String associationTable = DBUtil.getAssociationTableName(t.getClass(), field.getName());

                            //先删除表中pk1为idvalue的条目然后再添加
                            delete(associationTable, DBUtil.PK1 + "=?", new String[]{idValue});

                            ContentValues associationValues = new ContentValues();
                            for (Object obj : one2manyListObj) {
                                //将obj写入数据库中
                                Class objClz = (Class) obj.getClass();
                                DBManager.getInstance(ctx).getDao(objClz).newOrUpdate(obj);

                                //写入关联表
                                associationValues.clear();
                                associationValues.put(DBUtil.PK1, idValue);//pk1

                                String idColumnName = DBUtil.getIdColumnName(objClz);
                                Field idColumnField = objClz.getDeclaredField(idColumnName);
                                idColumnField.setAccessible(true);
                                String idValue2 = (String) idColumnField.get(obj);

                                associationValues.put(DBUtil.PK2, idValue2);//pk2

                                newOrUpdate(associationTable, associationValues);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.db(" newOrUpdate exception : " + e.toString());
        }
        newOrUpdate(mTableName, values);
    }


    public void delete(T t) {
        try {
            String id = (String) mIdField.get(t);
            delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.db(" delete(T t) exception : " + e.toString());
        }
    }

    public void delete(String id) {
        try {
            delete(mTableName, mIdName + "=?", new String[]{id});
            //delete releated association item
            for (Field field : mForeignFields) {
                delete(DBUtil.getAssociationTableName(clz, field.getName()), DBUtil.PK1 + " =? ", new String[]{id});
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.db(" delete(String id)  exception : " + e.toString());
        }
    }


    public T queryById(String id) {
        Cursor cursor = rawQuery(mTableName, mIdName + " = ? ", new String[]{id});

        if (cursor.moveToNext()) {
            return assembleObjFromCursor(cursor, id);
        } else {
            return null;
        }
    }

    /**
     * query all
     *
     * @return
     */
    public List<T> queryAll() {
        Cursor cursor = rawQuery(mTableName, null, null);
        List<T> tList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(mIdField)));
            T t = assembleObjFromCursor(cursor, id);
            if (t != null) {
                tList.add(t);
            }
        }
        if (ValidateUtil.isValidate(tList)) {
            return tList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // private methods area
    ///////////////////////////////////////////////////////////////////////////
    private Cursor rawQuery(String table, String where, String[] selectionArgs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from " + table);
        if (ValidateUtil.isValidate(where)) {
            sql.append(" where " + where);
        }
        Cursor cursor = mDatabase.rawQuery(sql.toString(),
                selectionArgs);
        LogUtils.d(" raw query sql: " + sql.toString() + " selectionArgs: " + selectionArgs);
        return cursor;
    }

    /**
     * assemble object form cursor
     *
     * @param cursor
     * @return
     */
    private T assembleObjFromCursor(Cursor cursor, final String id) {
        try {
            T t = clz.newInstance();
            for (Field field : mColumnFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    Class<?> columnTypeClz = field.getType();
                    int columnIndex = cursor.getColumnIndex(DBUtil.getColumnName(field));
                    if (columnTypeClz == String.class) {//String
                        field.set(t, cursor.getString(columnIndex));
                    } else if (columnTypeClz == int.class || columnTypeClz == Integer.class) {  //int
                        field.setInt(t, cursor.getInt(columnIndex));
                    } else if (columnTypeClz == long.class || columnTypeClz == Long.class) {  //long
                        field.setLong(t, cursor.getLong(columnIndex));
                    } else if (columnTypeClz == short.class || columnTypeClz == Short.class) { //short
                        field.setShort(t, cursor.getShort(columnIndex));
                    } else if (columnTypeClz == float.class || columnTypeClz == Float.class) { //float
                        field.setFloat(t, cursor.getFloat(columnIndex));
                    } else if (columnTypeClz == double.class || columnTypeClz == Double.class) { //double
                        field.setDouble(t, cursor.getDouble(columnIndex));
                    } else if (columnTypeClz == boolean.class || columnTypeClz == Boolean.class) { //boolean
                        field.setBoolean(t, Boolean.valueOf(cursor.getString(columnIndex)));
                    } else {
                        Column column = field.getAnnotation(Column.class);
                        Column.ColumnType columnAnnotationType = column.type();
                        if (columnAnnotationType == Column.ColumnType.SERIALIZABLE) {
                            field.set(t, SerializeUtil.deserializ(cursor.getBlob(columnIndex)));
                        } else if (columnAnnotationType == Column.ColumnType.ONE2ONE) {//one to one
                            //one to one obj id
                            String one2oneForgineId = cursor.getString(columnIndex);
                            Object one2oneObj = DBManager.getInstance(ctx).getDao(field.getType()).queryById(one2oneForgineId);
                            field.set(t, one2oneObj);
                        } else if (columnAnnotationType == Column.ColumnType.ONE2MANY) {//one to many
                            //该字段是一个list集合，只有一个泛型类型，所以获取第0个即可
                            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                            Class manyPartClz = (Class) parameterizedType.getActualTypeArguments()[0];
                            //从关联表中获取cursor
                            String associationTableName = DBUtil.getAssociationTableName(clz, field.getName());
                            Cursor associationCursor = rawQuery(associationTableName, DBUtil.PK1 + "=?", new String[]{id});
                            String manyPartIdValue = null;
                            Object manyPartItemObj = null;

                            List manyList = new ArrayList<>();
                            while (associationCursor.moveToNext()) {
                                //获取manyPartId，然后去manyPart表中获取该数据
                                manyPartIdValue = associationCursor.getString(associationCursor.getColumnIndex(DBUtil.PK2));
                                manyPartItemObj = DBManager.getInstance(ctx).getDao(manyPartClz).queryById(manyPartIdValue);
                                manyList.add(manyPartItemObj);
                            }
                            if (!ValidateUtil.isValidate(manyList)) continue;
                            field.set(t, manyList);
                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.db(" assembleObjFromCursor exception : " + e.toString());
        }
        return null;
    }

    private void delete(String table, String whereClause, String[] whereArgs) {
        mDatabase.delete(table, whereClause, whereArgs);
    }

    private void newOrUpdate(String tableName, ContentValues values) {
        mDatabase.replace(tableName, null, values);
    }


}
