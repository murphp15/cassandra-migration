package org.cognitor.cassandra.migration.spring;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.TableMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.cql.generator.CreateTableCqlGenerator;
import org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentEntity;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AllTablesAreAdded {

    private final Cluster cluster;
    private final String keyspace;
    private final CassandraMappingContext cassandraMappingContext;

    @Autowired
    public AllTablesAreAdded(Cluster cluster,
                             CassandraMigrationConfigurationProperties properties,
                             @Value("${spring.data.cassandra.keyspace-name:null}") String keyspaceName,
                             CassandraMappingContext cassandraMappingContext) {
        this.cluster = cluster;
        this.keyspace = properties.getKeyspaceName() == null ? keyspaceName : properties.getKeyspaceName();
        this.cassandraMappingContext = cassandraMappingContext;
    }

    public void confirm() {
        cassandraMappingContext.getTableEntities().stream()
                .filter(this::doesNotExist)
                .map(cassandraMappingContext::getCreateTableSpecificationFor)
                .findFirst()
                .ifPresent(a -> {
                    throw new RuntimeException("after migration the table " + a.getName() + " was not created. " +
                            "Create a migration script in the folder cassandra/migration with the line " + CreateTableCqlGenerator.toCql(a));
                });

        cassandraMappingContext.getTableEntities().stream()
                .filter(this::doesNotHaveSameNumberOfColumns)
                .findFirst()
                .ifPresent(a -> {
                    throw new RuntimeException("table class does not match schema");
                });
    }

    private boolean doesNotExist(BasicCassandraPersistentEntity<?> entity) {
        return cluster.getMetadata().getKeyspace(keyspace).getTable(entity.getTableName().toCql()) == null;
    }


    private boolean doesNotHaveSameNumberOfColumns(BasicCassandraPersistentEntity<?> entity) {
        final TableMetadata table = cluster.getMetadata().getKeyspace(keyspace).getTable(entity.getTableName().toCql());
        List<String> pKeys = table.getPrimaryKey().stream().map(ColumnMetadata::getName).collect(Collectors.toList());

        final List<String> collect = table.getColumns().stream()
                .map(ColumnMetadata::getName)
                .filter(a -> !pKeys.contains(a)).sorted().collect(Collectors.toList());
        final List<String> collect1 = cassandraMappingContext.getCreateTableSpecificationFor(entity).getNonKeyColumns().stream().map(a -> a.getName().toCql())
                .sorted().collect(Collectors.toList());

        return !collect.equals(collect1);


    }

}
