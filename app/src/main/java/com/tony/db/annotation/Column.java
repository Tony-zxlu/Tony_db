package com.tony.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tony on 16/4/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    boolean id() default false;

    String name() default "";

    ColumnType type() default ColumnType.NROMAL;

    enum ColumnType {
        SERIALIZABLE, NROMAL, ONE2MANY, ONE2ONE
    }
}
