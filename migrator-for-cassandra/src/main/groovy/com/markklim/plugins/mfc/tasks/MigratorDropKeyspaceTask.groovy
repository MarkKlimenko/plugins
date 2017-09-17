package com.markklim.plugins.mfc.tasks

import com.markklim.plugins.mfc.configuration.Migrator

class MigratorDropKeyspaceTask extends AbstractMigratorTask {
    MigratorDropKeyspaceTask() {
        super()
        setDescription('Migrator drop keyspace')
    }

    @Override
    protected void run(Map parameters) {
        try {
            Migrator.openSession(parameters)
                    .with { execute("DROP KEYSPACE ${parameters.keyspace};") }

            println "${parameters.keyspace} successfully dropped"
        } finally {
            Migrator.closeSession()
        }
    }
}
