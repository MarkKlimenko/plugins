package com.markklim.plugins.mfc.tasks

import com.markklim.plugins.mfc.configuration.Migrator
import groovy.io.FileType

import static com.markklim.plugins.mfc.utils.ScriptUtilities.*

class MigratorExecuteTask extends AbstractMigratorTask {
    MigratorExecuteTask() {
        super()
        setDescription('Execute .cql script')
    }

    @Override
    protected void run(Map parameters) {
        try {
            File source = new File(parameters.script as String)
            if (source.isDirectory()) {
                source.listFiles()
                        .sort { it.name }
                        .each { executeFile(parameters, it) }
            } else {
                executeFile(parameters, source)
            }
        } finally {
            Migrator.closeSession()
        }
    }

    static void executeFile(Map parameters, File source) {
        print "Script executed: file ${source.getName()}"
        getScriptFromFile(source)
                .with { implementPlaceholders(parameters, it) }
                .with { scriptToList(it) }
                .each { executeLine(parameters, it) }
        println ' OK'
    }

    static void executeLine(Map parameters, String scriptLine) {
        Migrator.openSession(parameters).execute(scriptLine)
    }
}
