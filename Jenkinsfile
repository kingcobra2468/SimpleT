pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    
    stages {
        stage('Fetch Sources') {
            steps {
                cleanWs()
                git branch: 'master', url: 'git@github.com:kingcobra2468/SimpleT.git'
            }
        }
        
       stage('Build APK') {
            steps {
                sh '''#!/bin/bash
                docker run --rm -v `pwd`:/project mingc/android-build-box bash \\
                    -c "cd /project; ./gradlew build; chown -R \"$(id -u):$(id -g)\" app/build/"
                '''
                dir('app/build/outputs/apk/') {
                    sh '''#!/bin/bash
                    mv release/app-release-unsigned.apk release/simplet-release-unsigned.apk
                    mv debug/app-debug.apk debug/simplet-debug.apk
                    '''
                }
            }
        }
    }
    post {
        success {
            dir('app/build/outputs/apk/') {
                archiveArtifacts artifacts: '**/*.apk', fingerprint: true
            }
        }
    }
}
