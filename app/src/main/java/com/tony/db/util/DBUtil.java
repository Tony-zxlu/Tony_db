package com.tony.db.util;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tony.db.annotation.Column;
import com.tony.db.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 16/4/5.
 */
public final class DBUtil {

    public static final String PK1 = "pk1";
    public static final String PK2 = "pk2";


    /**
     * get the table name by clz
     *
     * @param clz
     * @return
     */
    public static final String getTableName(Class<?> clz) {
        if (clz.isAnnotationPresent(Table.class)) {
            String tableName = clz.getAnnotation(Table.class).name();
            if (ValidateUtil.isValidate(tableName)) {
                return tableName;
            } else {
                return clz.getSimpleName().toLowerCase();
            }
        } else {
            throw new IllegalArgumentException(" the class " + clz.getName() + " can't match the table");
        }
    }

    /**
     * get column name by field
     *
     * @param field
     * @return
     */
    public static final String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        String columnName = column.name();
        if (!ValidateUtil.isValidate(columnName)) {
            columnName = field.getName().toLowerCase();
        }
        return columnName;
    }


    /**
     * get id column name by clz
     *
     * @param clz
     * @return
     */
    public static final String getIdColumnName(Class<?> clz) {
        if (clz.isAnnotationPresent(Table.class)) {
            Field[] declaredFields = clz.getDeclaredFields();
            Column column = null;
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    column = field.getAnnotation(Column.class);
                    if (column.id()) {
                        String columnName = column.name();
                        if (!ValidateUtil.isValidate(columnName)) {
                            columnName = field.getName().toLowerCase();
                        }
                        return columnName;
                    }
                }
            }
        }
        return null;
    }

    /**
     * get column stmt by field
     *
     * @param field
     * @return
     */
    public static final String getOneColumnStmt(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            String columnName = getColumnName(field);
            String dataType;
            Class<?> clzType = field.getType();
            if (clzType == String.class) {
                dataType = " TEXT ";
            } else if (clzType == int.class || clzType == Integer.class) {
                dataType = " integer ";
            } else if (clzType == short.class || clzType == Short.class) {
                dataType = " short ";
            } else if (clzType == boolean.class || clzType == Boolean.class) {
                dataType = " boolean ";
            } else if (clzType == long.class || clzType == Long.class) {
                dataType = " long ";
            } else if (clzType == double.class || clzType == Double.class) {
                dataType = " double ";
            } else if (clzType == byte.class || clzType == Byte.class) {
                dataType = " byte ";
            } else if (clzType == float.class || clzType == Float.class) {
                dataType = " float ";
            } else {
                Column.ColumnType columnType = column.type();
                if (columnType == Column.ColumnType.SERIALIZABLE) {
                    dataType = " BLOB ";
                } else {
                    dataType = " TEXT ";
                }
            }
            StringBuilder stmt = new StringBuilder();

            stmt.append(columnName).append(dataType);
            if (column.id()) {
                stmt.append(" primary key ");
            }
            String stmtStr = stmt.toString();
            LogUtils.db(stmtStr);
            return stmtStr;
        } else {
            return "";
        }
    }

    /**
     * @param clz
     * @return
     */
    public static final List<String> getCreateTableStmt(Class<?> clz) {
        List<String> stmts = new ArrayList<>();
        StringBuilder mColumnStmts = new StringBuilder();
        if (clz.isAnnotationPresent(Table.class)) {
            Field[] declaredFields = clz.getDeclaredFields();
            int fieldLen = declaredFields.length;
            for (int i = 0; i < fieldLen; i++) {
                Field field = declaredFields[i];
                if (field.isAnnotationPresent(Column.class)) {
                    if (field.getAnnotation(Column.class).type() == Column.ColumnType.ONE2MANY) {
                        String associationSql = "create table if not exists " + getAssociationTableName(clz, field.getName()) + "(" + PK1 + " TEXT, " + PK2
                                + " TEXT)";
                        stmts.add(associationSql);
                        LogUtils.db(" create association table : " + associationSql);
                    } else {
                        mColumnStmts.append(getOneColumnStmt(field)).append(",");
                    }
                }
            }
        }
        //delete the last ","
        mColumnStmts.deleteCharAt(mColumnStmts.length() - 1);

        String stmt = "create table if not exists " + getTableName(clz) + " (" +
                mColumnStmts.toString() + ")";
        LogUtils.db(stmt);
        stmts.add(stmt);
        return stmts;
    }


    public static String getAssociationTableName(Class<?> clz, String name) {
        return getTableName(clz) + "_" + name;
    }

    /**
     * @param clz
     * @return
     */
    public static final List<String> getDropTableStmt(Class<?> clz) {
        List<String> dropStmts = new ArrayList<>();
        dropStmts.add("drop table if exists " + getTableName(clz));//drop oneself stmt

        //drop association table stmt
        if (clz.isAnnotationPresent(Table.class)) {
            Field[] declaredFields = clz.getDeclaredFields();
            int fieldLen = declaredFields.length;
            for (int i = 0; i < fieldLen; i++) {
                Field field = declaredFields[i];
                if (field.isAnnotationPresent(Column.class)) {
                    if (field.getAnnotation(Column.class).type() == Column.ColumnType.ONE2MANY) {
                        dropStmts.add("drop table if exists " + getAssociationTableName(clz, field.getName()));
                    }
                }
            }
        }
        return dropStmts;
    }


    /**
     * create table
     *
     * @param db
     * @param clz
     */
    public static final void createTable(SQLiteDatabase db, Class<?> clz) throws SQLException {
        List<String> stmts = getCreateTableStmt(clz);
        for (String stmt : stmts) {
            db.execSQL(stmt);
        }
    }

    /**
     * drop table
     *
     * @param db
     * @param clz
     */
    public static final void dropTable(SQLiteDatabase db, Class<?> clz) {
        List<String> stmts = getDropTableStmt(clz);
        for (String stmt : stmts) {
            db.execSQL(stmt);
        }
    }


    /**
     * get foreignFields
     *
     * @param columnFields
     * @return
     */
    public static final List<Field> getForeignFields(Field[] columnFields) {
        if (!ValidateUtil.isValidate(columnFields)) return null;
        List<Field> foregineFields = new ArrayList<>();
        Column column;
        for (Field field : columnFields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                if (column.type() == Column.ColumnType.ONE2MANY) {
                    foregineFields.add(field);
                }
            }
        }
        return foregineFields;
    }


}
