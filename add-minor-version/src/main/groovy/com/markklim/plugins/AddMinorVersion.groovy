package com.markklim.plugins

import org.apache.tools.ant.Project
import org.gradle.api.Plugin

class AddMinorVersion implements Plugin<Project> {
    void apply(Project project) {
        project.task('hello') {
            doLast {
                println "Hello from the GreetingPlugin"
            }
        }
    }
}
