## Set minor version plugin
- designed for setting minor version from CI managers or manual launching from gradle command
- set version to gradle.properties file of root project
- update version number, commit and push changes in properties file to git 

#### Requirements to gradle.properties file

**Example (if -PminorVersion=22):**

Spaces before and after '=' mark
- projectVersion = 0.1 -> projectVersion = 0.1.22
- projectVersion=0.1 -> projectVersion=0.1.22
- projectVersion= 0.1 -> projectVersion= 0.1.22

Version number
- projectVersion = 0.1 -> projectVersion = 0.1.22
- projectVersion = 0.1.11 -> projectVersion = 0.1.22

#### Required variables:
- minorVersion - minor version from CI manager or manual command invocation _(1234, 12, 014)_
- ciBranchName - targeted branch name _(master, versions/0.1, branches/0.1/Test_branch)_
- ciCredentials - credentials for pushing into git branch _(user:password)_
- ciProjectPath - project path in git _(192.168.1.38/backoffice/ui-provider-service.git/)_

#### Usage:
**build.gradle - install plugin**
<pre>
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath 'gradle.plugin.com.markklim.plugins:set-minor-version:0.8'
    }
}

apply plugin: 'com.markklim.plugins.smv'
</pre>

**build.gradle - settings**
<pre>
setMinorVersion {
    setSmvMinorVersion minorVersion
    setSmvCiBranchName ciBranchName
    setSmvCiCredentials ciCredentials
    setSmvCiProjectPath ciProjectPath
}

OR

setMinorVersion {
    smvMinorVersion = minorVersion
    smvCiBranchName = ciBranchName
    smvCiCredentials = ciCredentials
    smvCiProjectPath = ciProjectPath
}
</pre>

**gradle.properties**
<pre>
minorVersion =
ciBranchName =
ciCredentials =
ciProjectPath =
</pre>

**Task invocation**
<pre>
gradlew setMinorVersion -PminorVersion=$TRAVIS_BUILD_NUMBER -PciBranchName=$TRAVIS_BRANCH -PciCredentials=$CREDENTIALS -PciProjectPath=$PROJECT_PATH
</pre>