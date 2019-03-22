package org.cognitor.cassandra.migration.spring;

import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;

/**
 * Identifies a domain object to be persisted to Cassandra as a table.
 *
 * @author Alex Shvid
 * @author Matthew T. Adams
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Keyspace {
    String keyspace() default "";
}
