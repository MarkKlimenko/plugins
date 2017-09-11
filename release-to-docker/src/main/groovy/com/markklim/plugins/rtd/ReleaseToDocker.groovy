package com.markklim.plugins.rtd

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReleaseToDocker implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('releaseToDocker', ReleaseToDockerExtension)
        project.task('releaseToDocker') {
            doLast {
                def dockerHub = project.releaseToDocker.dockerHub
                def registryUrl = project.releaseToDocker.registryUrl
                def imageName = project.releaseToDocker.imageName
                def dockerFileLocation = project.releaseToDocker.dockerFileLocation
                def warFileName = project.releaseToDocker.imageFileName
                def projectVersion = project.releaseToDocker.imageVersion
                def dockerUsername = project.releaseToDocker.username
                def dockerPassword = project.releaseToDocker.password
                def rmiAfterPush = project.releaseToDocker.rmiAfterPush

                project.copy {
                    from "${dockerFileLocation}/Dockerfile"
                    from "build/libs/${warFileName}"
                    into 'build/docker'
                }
                project.exec {
                    workingDir 'build/docker'
                    commandLine 'docker', 'build', '-t', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}", '.'
                }

                if(dockerHub) {
                    project.exec { commandLine 'docker', 'login', "-u=${dockerUsername}", "-p=${dockerPassword}" }
                } else {
                    project.exec { commandLine 'docker', 'login', registryUrl, "-u=${dockerUsername}", "-p=${dockerPassword}" }
                }

                project.exec { commandLine 'docker', 'push', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}" }
                if(rmiAfterPush) {
                    project.exec { commandLine 'docker', 'rmi', "${registryUrl}/${imageName}:${getMajorVersion(projectVersion)}" }
                }
            }
        }
    }
    static def getMajorVersion(projectVersion) {
        (projectVersion =~ /^.{3}/)[0]
    }
}

class ReleaseToDockerExtension {
    Boolean dockerHub = false
    String registryUrl
    String imageName
    String dockerFileLocation
    String imageFileName
    String imageVersion
    String username
    String password
    Boolean rmiAfterPush = false
}
