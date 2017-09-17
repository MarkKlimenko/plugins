package com.markklim.plugins.mfc

import com.markklim.plugins.mfc.tasks.MigratorDropKeyspaceTask
import com.markklim.plugins.mfc.tasks.MigratorExecuteTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Registers the plugin's tasks.
 */
class MigratorPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.getExtensions().create("migrator", MigratorExtension.class)
        project.getTasks().create('migratorDropKeyspace', MigratorDropKeyspaceTask.class)
        project.getTasks().create('migratorExecute', MigratorExecuteTask.class)
    }
}

