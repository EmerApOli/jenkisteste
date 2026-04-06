pipeline {
    agent any

    environment {
        IMAGE_REPOSITORY = 'local'
        APP_ONE_NAME = 'app-one'
        APP_TWO_NAME = 'app-two'
        K8S_NAMESPACE = 'demo-java'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'ls -la'
            }
        }

        stage('Build app-one') {
            steps {
                dir('app-one') {
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('Build app-two') {
            steps {
                dir('app-two') {
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('Build Docker images') {
            steps {
                script {
                    env.APP_ONE_IMAGE = "${IMAGE_REPOSITORY}/${APP_ONE_NAME}:${BUILD_NUMBER}"
                    env.APP_TWO_IMAGE = "${IMAGE_REPOSITORY}/${APP_TWO_NAME}:${BUILD_NUMBER}"
                    env.APP_ONE_IMAGE_LATEST = "${IMAGE_REPOSITORY}/${APP_ONE_NAME}:latest"
                    env.APP_TWO_IMAGE_LATEST = "${IMAGE_REPOSITORY}/${APP_TWO_NAME}:latest"
                }

                sh "docker build -t ${env.APP_ONE_IMAGE} -t ${env.APP_ONE_IMAGE_LATEST} ./app-one"
                sh "docker build -t ${env.APP_TWO_IMAGE} -t ${env.APP_TWO_IMAGE_LATEST} ./app-two"
            }
        }

        stage('Show Docker images') {
            steps {
                sh 'docker images | head -30'
            }
        }

        stage('Deploy Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/infra.yaml'
                sh "kubectl set image deployment/${APP_ONE_NAME} ${APP_ONE_NAME}=${env.APP_ONE_IMAGE} -n ${K8S_NAMESPACE}"
                sh "kubectl set image deployment/${APP_TWO_NAME} ${APP_TWO_NAME}=${env.APP_TWO_IMAGE} -n ${K8S_NAMESPACE}"
                sh "kubectl rollout status deployment/${APP_ONE_NAME} -n ${K8S_NAMESPACE} --timeout=180s"
                sh "kubectl rollout status deployment/${APP_TWO_NAME} -n ${K8S_NAMESPACE} --timeout=180s"
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'app-one/target/*.jar, app-two/target/*.jar', fingerprint: true
            echo 'Build, imagem Docker e deploy concluídos com sucesso.'
        }
        failure {
            echo 'Falha no pipeline. Verifique Maven, Docker e kubectl no agente do Jenkins.'
        }
    }
}
