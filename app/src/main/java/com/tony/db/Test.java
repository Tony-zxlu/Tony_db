package com.tony.db;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tony on 16/4/8.
 */
public class Test {
    public List<String> list = new LinkedList<String>();

    public static void main(String[] args) throws SecurityException, NoSuchFieldException {
        ParameterizedType pt = (ParameterizedType) Test.class.getField(
                "list").getGenericType();
        System.out.println(pt.getActualTypeArguments().length);
        System.out.println(pt.getActualTypeArguments()[0]);
    }

}
