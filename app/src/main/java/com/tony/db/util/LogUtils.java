package com.tony.db.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文 件 名：LogUtils.java
 * 描    述：log管理工具
 * 作    者：鲁中需
 * 时    间：2015/10/17
 * 版    本：1.0
 */
public class LogUtils {

    public static String customTagPrefix = "TONY";

    public static final String DRINK_LOG_PATH = "/sdcard/robotmobile/drink.log";
    public static final String ANYCHAT_LOG_PATH = "/sdcard/robotmobile/anychat.log";
    public static final String DB_LOG_PATH = "/sdcard/robotmobile/db.log";

    private LogUtils() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;


    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static CustomLogger customLogger;



    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);


    }

    public static void d(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }


    public static void dd(String content) {
        if (!allowD) return;
        if (customLogger != null) {
            customLogger.d("qweasd", content);
        } else {
            Log.d("qweasd", content);
        }
    }

    public static void drinkD(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content);
        writeLog(tag, content, Log.DEBUG, DRINK_LOG_PATH);
    }
    public static void anychatD(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content);
        writeLog(tag, content, Log.DEBUG, ANYCHAT_LOG_PATH);
    }

    public static void db(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content);
//        writeLog(tag, content, Log.DEBUG, DB_LOG_PATH);
    }
    /**
     * 获取当前时间的显示字符
     *
     * @return
     */
    public static String getCurTimeStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss.SSS  ");
        Date curDate = new Date(System.currentTimeMillis());
        String timeStr = formatter.format(curDate);
        return timeStr;
    }


    private static void writeLog(String tag, String content, int level, final String logPath) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(tag)) return;

        StringBuilder sb = new StringBuilder();
        sb.append("[" + getFullDate() + "]");
        switch (level) {
            case Log.VERBOSE:
                sb.append("[V]-");
                break;
            case Log.DEBUG:
                sb.append("[D]-");
                break;
            case Log.INFO:
                sb.append("[I]-");
                break;
            case Log.WARN:
                sb.append("[W]-");
                break;
            case Log.ERROR:
                sb.append("[E]-");
                break;
        }
        //可以任意的访问文件的任何地方
        RandomAccessFile raf = null;
        File file = new File(logPath);
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            String result = sb.toString() + tag + content + "\r\n";
            raf.write(result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getFullDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String format = sdf.format(date);
        return format;
    }

}