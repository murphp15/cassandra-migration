package org.cognitor.cassandra.migration.spring;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class MigrationTest {

    @Autowired
    MigrationTaskWithAssertion migrationTaskWithAssertion;

    @Test
    public void test() {
        System.out.println();
    }
}
