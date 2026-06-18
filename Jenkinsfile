pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }
    
    stages {

        stage('Checkout') {
            steps {
                echo 'Fetching source code from GitHub'
            }
        }
        stage('Environment Check') {
            steps {
               sh 'java -version'
               sh 'javac -version'
               sh 'mvn -version'
             }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build Successful!'
        }

        failure {
            echo 'Build Failed!'
        }
    }
}
