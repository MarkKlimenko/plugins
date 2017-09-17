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
                source.eachFile(FileType.FILES) {
                    executeFile(parameters, it)
                }
            } else {
                executeFile(parameters, source)
            }
        } finally {
            Migrator.closeSession()
        }
    }

    static void executeFile(Map parameters, File source) {
        getScriptFromFile(source)
                .with { implementPlaceholders(it) }
                .with { scriptToList(it) }
                .each { executeLine(parameters, it) }
        println "Script executed: file ${source.getName()}"
    }

    static void executeLine(Map parameters, String scriptLine) {
        Migrator.openSession(parameters).execute(scriptLine)
    }
}
