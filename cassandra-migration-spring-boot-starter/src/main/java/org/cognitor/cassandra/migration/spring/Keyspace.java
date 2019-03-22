package org.cognitor.cassandra.migration.spring;

import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;


@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Keyspace {
    String keyspace() default "";
}
