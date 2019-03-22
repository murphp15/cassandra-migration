package org.cognitor.cassandra.migration.spring;


import org.cognitor.cassandra.migration.MigrationTask;

public class MigrationTaskWithAssertion {
    private final MigrationTask migrationTask;
    private final AllTablesAreAdded allTablesAreAdded;

    public MigrationTaskWithAssertion(MigrationTask migrationTask, AllTablesAreAdded allTablesAreAdded) {
        this.migrationTask = migrationTask;
        this.allTablesAreAdded = allTablesAreAdded;
    }

    public void migrate() {
        migrationTask.migrate();
        allTablesAreAdded.confirm();
    }
}
