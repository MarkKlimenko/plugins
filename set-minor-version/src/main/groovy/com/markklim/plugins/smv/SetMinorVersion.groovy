package com.markklim.plugins.smv

import org.gradle.api.Plugin
import org.gradle.api.Project

class SetMinorVersion implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('setMinorVersion', SetMinorVersionExtension)
        project.task('setMinorVersion') {
            doLast {
                def minorVersion = project.setMinorVersion.mVersion
                def ciBranchName = project.setMinorVersion.branchName
                def ciCredentials = project.setMinorVersion.credentials
                def ciProjectPath = project.setMinorVersion.projectPath
                def ciCommitMessage = project.setMinorVersion.commitMessage

                ["git", "checkout", ciBranchName].execute().text

                def sourceFile = new File('gradle.properties')
                def versionField = (sourceFile.text =~ /projectVersion.+\b/)[0]
                def rawVersionField = versionField.split('\\.')

                if (minorVersion.size() != 0) {
                    if (rawVersionField.size() == 2) {
                        versionField = "${versionField}.${minorVersion}"
                    } else {
                        versionField = "${rawVersionField[0]}.${rawVersionField[1]}.${minorVersion}"
                    }
                    sourceFile.write(sourceFile.text.replaceAll(/projectVersion.+\b/, versionField))
                }

                ["git", "add", "gradle.properties"].execute().text
                ["git", "commit", "-m","${ciCommitMessage} [skip ci]"].execute().text
                ["git", "push", "http://${ciCredentials}@${ciProjectPath}", "HEAD:" + ciBranchName].execute().text
            }
        }
    }
}

class SetMinorVersionExtension {
    String mVersion
    String branchName
    String credentials
    String projectPath
    String commitMessage = 'Set minor version'
}
