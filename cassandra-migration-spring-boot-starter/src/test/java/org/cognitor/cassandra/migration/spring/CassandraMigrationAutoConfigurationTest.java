package org.cognitor.cassandra.migration.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Patrick Kranz
 */
@TestPropertySource(properties = {"cassandra.migration.keyspace-name:test_keyspace",
        "cassandra.migration.table-prefix:test_"})
@SpringBootTest
public class CassandraMigrationAutoConfigurationTest {
//
//    @Test
//    public void shouldMigrateDatabaseWhenClusterGiven() {
//        context.getBean(MigrationTask.class);
//        try (Session session = cluster.connect(TEST_KEYSPACE)) {
//            List<Row> rows = session.execute("SELECT * FROM schema_migration").all();
//            assertThat(rows.size(), is(equalTo(1)));
//            Row migration = rows.get(0);
//            assertThat(migration.getBool("applied_successful"), is(true));
//            assertThat(migration.getTimestamp("executed_at"), is(not(nullValue())));
//            assertThat(migration.getString("script_name"), is(CoreMatchers.equalTo("001_create_person_table.cql")));
//            assertThat(migration.getString("script"), startsWith("CREATE TABLE"));
//        }
//    }
//
//    @Test
//    public void shouldMigrateDatabaseWhenClusterGivenWithPrefix() {
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext();
//        addEnvironment(context, "cassandra.migration.keyspace-name:test_keyspace");
//        addEnvironment(context, );
//        context.register(ClusterConfig.class, xx.class);
//        context.refresh();
//        Cluster cluster = context.getBean(Cluster.class);
//        context.getBean(MigrationTask.class);
//        try (Session session = cluster.connect(TEST_KEYSPACE)) {
//            List<Row> rows = session.execute("SELECT * FROM test_schema_migration").all();
//            assertThat(rows.size(), is(equalTo(1)));
//            Row migration = rows.get(0);
//            assertThat(migration.getBool("applied_successful"), is(true));
//            assertThat(migration.getTimestamp("executed_at"), is(not(nullValue())));
//            assertThat(migration.getString("script_name"), is(CoreMatchers.equalTo("001_create_person_table.cql")));
//            assertThat(migration.getString("script"), startsWith("CREATE TABLE"));
//        }
//    }
//
//    @Configuration
//    static class ClusterConfig {
//        static final String TEST_KEYSPACE = "test_keyspace";
//
//        private static final String CASSANDRA_INIT_SCRIPT = "cassandraTestInit.cql";
//        private static final String LOCALHOST = "127.0.0.1";
//
//        private static final String YML_FILE_LOCATION = "cassandra.yml";
//        private ClassPathCQLDataSet dataSet;
//        private Cluster cluster;
//
//        @Bean
//        public Cluster cluster() throws Exception {
//            dataSet = new ClassPathCQLDataSet(CASSANDRA_INIT_SCRIPT, TEST_KEYSPACE);
//            cluster = new Cluster.Builder().addContactPoints(LOCALHOST).withPort(9142).build();
//            init();
//            return cluster;
//        }
//
//        private void init() throws Exception {
//            EmbeddedCassandraServerHelper.startEmbeddedCassandra(YML_FILE_LOCATION, 30 * 1000L);
//            loadTestData();
//        }
//
//        private void loadTestData() {
//            Session session = cluster.connect();
//            CQLDataLoader dataLoader = new CQLDataLoader(session);
//            dataLoader.load(dataSet);
//            session.close();
//        }
//
//    }
}