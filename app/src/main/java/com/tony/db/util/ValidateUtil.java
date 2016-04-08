package com.tony.db.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 16/3/30.
 */
public final class ValidateUtil {

    public static final boolean isValidate(String content) {
        return (content != null && !"".equals(content.trim()));
    }

    public static final <T> boolean isValidate(Collection<T> list) {
        return (list != null && list.size() > 0);
    }

    public static final <T> boolean isValidate(T... objs) {
        return (objs != null && objs.length > 0);
    }

    public static final <T> boolean isValidate(Map maps) {
        return (maps != null && maps.size() > 0);
    }
}
