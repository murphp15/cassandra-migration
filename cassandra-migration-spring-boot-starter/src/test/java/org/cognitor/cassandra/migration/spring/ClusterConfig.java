package org.cognitor.cassandra.migration.spring;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClusterConfig {
    static final String TEST_KEYSPACE = "test_keyspace";

    private static final String CASSANDRA_INIT_SCRIPT = "cassandraTestInit.cql";
    private static final String LOCALHOST = "127.0.0.1";

    private static final String YML_FILE_LOCATION = "cassandra.yml";
    private ClassPathCQLDataSet dataSet;
    private Cluster cluster;

    @Bean
    public Cluster cluster() throws Exception {
        dataSet = new ClassPathCQLDataSet(CASSANDRA_INIT_SCRIPT, TEST_KEYSPACE);
        cluster = new Cluster.Builder().addContactPoints(LOCALHOST).withPort(9142).build();
        init();
        return cluster;
    }

    private void init() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(YML_FILE_LOCATION, 30 * 1000L);
        loadTestData();
    }

    private void loadTestData() {
        Session session = cluster.connect();
        CQLDataLoader dataLoader = new CQLDataLoader(session);
        dataLoader.load(dataSet);
        session.close();
    }

}