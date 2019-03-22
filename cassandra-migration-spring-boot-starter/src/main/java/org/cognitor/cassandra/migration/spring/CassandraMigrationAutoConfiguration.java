package org.cognitor.cassandra.migration.spring;

import com.datastax.driver.core.Cluster;
import org.cognitor.cassandra.migration.Database;
import org.cognitor.cassandra.migration.MigrationRepository;
import org.cognitor.cassandra.migration.MigrationTask;
import org.cognitor.cassandra.migration.collector.FailOnDuplicatesCollector;
import org.cognitor.cassandra.migration.collector.IgnoreDuplicatesCollector;
import org.cognitor.cassandra.migration.scanner.ScannerRegistry;
import org.cognitor.cassandra.migration.spring.scanner.SpringBootLocationScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

/**
 * @author Patrick Kranz
 */
@Configuration
@EnableConfigurationProperties(CassandraMigrationConfigurationProperties.class)
@AutoConfigureAfter(CassandraAutoConfiguration.class)
@ConditionalOnClass(Cluster.class)
@Import(AllTablesAreAdded.class)
public class CassandraMigrationAutoConfiguration {
    private final CassandraMigrationConfigurationProperties properties;
    private AllTablesAreAdded allTablesAreAdded;

    @Autowired
    public CassandraMigrationAutoConfiguration(CassandraMigrationConfigurationProperties properties,
                                               @Value("${spring.data.cassandra.keyspace-name}") String keyspaceName,
                                               AllTablesAreAdded allTablesAreAdded) {
        this.properties = properties;
        if (this.properties.getKeyspaceName() == null) {
            this.properties.setKeyspaceName(keyspaceName);
        }
        this.allTablesAreAdded = allTablesAreAdded;
    }


    @Bean
    @DependsOn("migrationTask")
    public CassandraConverter cassandraConverter(CassandraMappingContext mapping,
                                                 CassandraCustomConversions conversions) {
        MappingCassandraConverter converter = new MappingCassandraConverter(mapping);
        converter.setCustomConversions(conversions);
        return converter;
    }

    @Bean(initMethod = "migrate")
    @ConditionalOnBean(Cluster.class)
    @ConditionalOnMissingBean(MigrationTask.class)
    public MigrationTaskWithAssertion migrationTask(Cluster cluster) {
        if (!properties.hasKeyspaceName()) {
            throw new IllegalStateException("Please specify ['cassandra.migration.keyspace-name'] in" +
                    " order to migrate your database");
        }
        cluster.newSession().execute("create KEYSPACE if NOT EXISTS " + properties.getKeyspaceName()
                + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}; ");

        MigrationRepository migrationRepository = createRepository();
        final MigrationTask migrationTask = new MigrationTask(new Database(cluster, properties.getKeyspaceName(), properties.getTablePrefix())
                .setConsistencyLevel(properties.getConsistencyLevel()),
                migrationRepository);
        return new MigrationTaskWithAssertion(migrationTask, allTablesAreAdded);
    }

    private MigrationRepository createRepository() {
        ScannerRegistry registry = new ScannerRegistry();
        registry.register(ScannerRegistry.JAR_SCHEME, new SpringBootLocationScanner());
        if (properties.getStrategy() == ScriptCollectorStrategy.FAIL_ON_DUPLICATES) {
            return new MigrationRepository(properties.getScriptLocation(), new FailOnDuplicatesCollector(), registry);
        }
        return new MigrationRepository(properties.getScriptLocation(), new IgnoreDuplicatesCollector(), registry);
    }
}
