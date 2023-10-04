pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Project') {
            steps {
                script {
                    sh './gradlew build'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t public.ecr.aws/p8x2g8s1/marisia/medicare:latest .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    // Push the Docker image to a AWS ECR
                    sh 'docker push public.ecr.aws/p8x2g8s1/marisia/medicare:latest'
                }
            }
        }
    }
}
