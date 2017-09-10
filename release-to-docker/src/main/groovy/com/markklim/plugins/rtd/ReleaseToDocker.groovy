package com.markklim.plugins.rtd

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReleaseToDocker implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('releaseToDocker', ReleaseToDockerExtension)
        project.task('releaseToDocker') {
            doLast {
                def registryUrl = project.releaseToDocker.rtdRegistryUrl
                def imageName = project.releaseToDocker.rtdImageName
                def dockerFileLocation = project.releaseToDocker.rtdDockerFileLocation
                def warFileName = project.releaseToDocker.rtdWarFileName
                def projectVersion = project.releaseToDocker.rtdProjectVersion
                def dockerUsername = project.releaseToDocker.rtdDockerUsername
                def dockerPassword = project.releaseToDocker.rtdDockerPassword
                def rmiAfterPush = project.releaseToDocker.rtdRmiAfterPush

                copy {
                    from "${dockerFileLocation}/Dockerfile"
                    from "build/libs/${warFileName}"
                    into 'build/docker'
                }
                exec {
                    workingDir 'build/docker'
                    commandLine 'docker', 'build', '-t', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}", '.'
                }
                exec { commandLine 'docker', 'login', registryUrl, "-u=${dockerUsername}", "-p=${dockerPassword}" }
                exec { commandLine 'docker', 'push', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}" }

                if(rmiAfterPush) {
                    exec { commandLine 'docker', 'rmi', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}" }
                }

            }
        }
    }
    static def getMajorVersion(projectVersion) {
        (projectVersion =~ /^.{3}/)[0]
    }
}

class ReleaseToDockerExtension {
    String rtdRegistryUrl
    String rtdImageName
    String rtdDockerFileLocation
    String rtdWarFileName
    String rtdProjectVersion
    String rtdDockerUsername
    String rtdDockerPassword
    Boolean rtdRmiAfterPush = false
}
