package com.tony.db.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by tony on 16/3/30.
 */
public final class SerializeUtil {

    public static final Object deserializ(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            LogUtils.db(" deserialize obj failed : " + e.toString());
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {

                }
            }
        }
    }


    public static final byte[] serialize(Object obj) {
        if (obj == null) return null;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (Exception e) {
            e.toString();
            LogUtils.db(" object serialize failed : " + e.toString());
            return null;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
