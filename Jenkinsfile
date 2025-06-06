pipeline {
    agent any
    environment {
        REGISTRY = "myrepo"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }
        stage('Build Docker Images') {
            steps {
                script {
                    def modules = ['price-service','customer-service','gateway-service','config-server','discovery-server']
                    for (m in modules) {
                        sh "docker build -t $REGISTRY/${m}:${BUILD_NUMBER} ${m}"
                    }
                }
            }
        }
        stage('Push Images') {
            steps {
                script {
                    def modules = ['price-service','customer-service','gateway-service','config-server','discovery-server']
                    for (m in modules) {
                        sh "docker push $REGISTRY/${m}:${BUILD_NUMBER}"
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/'
            }
        }
    }
    post {
        failure {
            echo 'Deployment failed - rolling back'
            sh 'kubectl rollout undo deployment/gateway-service || true'
        }
    }
}
