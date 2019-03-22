package org.cognitor.cassandra.migration.spring;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("test_table")
public class TestTable {

    @PrimaryKey
    String string;
}

