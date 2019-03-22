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
public @interface Table {

    String keyspace() default "";

    /**
     * The name of the table; must be a valid CQL identifier or quoted identifier.
     */
    String value() default "";

    /**
     * Whether to cause the table name to be force-quoted.
     */
    boolean forceQuote() default false;


}
