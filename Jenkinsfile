pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // dockerhub : jenkins에 등록해 놓은 docker hub credentials 이름
        TARGET_HOST = "ubuntu@3.39.3.203" // @ 뒤에는 web-server public ip
        AWS_ACCESS_KEY = "${env.AWS_ACCESS_KEY}"
        AWS_SECRET_KEY = "${env.AWS_SECRET_KEY}"
        RDS_URL = "${env.RDS_URL}"
        RDS_USERNAME = "${env.RDS_USERNAME}"
        RDS_PASSWORD = "${env.RDS_PASSWORD}"
    }

    agent any

    tools {
        // 설치된 Maven의 이름
        maven "maven"
    }

    stages {
        stage('Git Pull') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'prod', credentialsId: 'github', url: 'https://github.com/JABibim/jabibim_admin.git'
            }
            post {
                success {
                    sh 'echo "레포지토리 클론 성공"'
                }
                failure {
                    sh 'echo "레포지토리 클론 실패"'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('Build') {
            steps {
                // dir("Spring10_Security_Thymeleaf_Jenkins") {
                sh 'sed -i "s#{AWS_ACCESS_KEY}#${AWS_ACCESS_KEY}#" "./src/main/resources/application.properties"'
                sh 'sed -i "s#{AWS_SECRET_KEY}#${AWS_SECRET_KEY}#" "./src/main/resources/application.properties"'
                sh 'sed -i "s#{RDS_URL}#${RDS_URL}#" "./src/main/resources/application.properties"'
                sh 'sed -i "s#{RDS_USERNAME}#${RDS_USERNAME}#" "./src/main/resources/application.properties"'
                sh 'sed -i "s#{RDS_PASSWORD}#${RDS_PASSWORD}#" "./src/main/resources/application.properties"'

                sh "mvn -DskipTests clean compile package"
                // }
            }
            post {
                success {
                    echo 'Maven 빌드 성공'
                }
                failure {
                    echo 'Maven 빌드 실패'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('Dockerizing') {
            steps {
                // dir("Spring10_Security_Thymeleaf_Jenkins") {
                sh 'echo "이미지 빌드 시작"'
                sh 'docker build . -t $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER'
                sh 'docker build . -t $DOCKERHUB_CREDENTIALS_USR/jabibim_admin'
                // }
            }
            post {
                success {
                    sh 'echo "Docker 이미지 빌드 성공"'
                }
                failure {
                    sh 'echo "Docker 이미지 빌드 실패"'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('Login') {
            steps {
                sh 'docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW'
            }
            post {
                success {
                    sh 'echo "Docker 로그인 성공"'
                }
                failure {
                    sh 'echo "Docker 로그인 실패"'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('Docker hub push') {
            steps {
                sh 'echo "Docker Hub에 푸시 시작"'
                sh 'docker push $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER'
                sh 'docker push $DOCKERHUB_CREDENTIALS_USR/jabibim_admin'
            }
            post {
                success {
                    sh 'echo "Docker Hub 푸시 성공"'
                }
                failure {
                    sh 'echo "Docker Hub 푸시 실패"'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('Cleaning up') {
            steps {
                sh 'echo "Docker 이미지 제거 시작"'
                sh "docker rmi $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER" // docker image 제거
                sh "docker rmi $DOCKERHUB_CREDENTIALS_USR/jabibim_admin" // docker image 제거
            }
            post {
                success {
                    sh 'echo "Docker 이미지 제거 성공"'
                }
                failure {
                    sh 'echo "Docker 이미지 제거 실패"'

                    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

                    exit 1
                }
            }
        }

        stage('multiline ssh') {
            steps {
                sshagent(credentials: ['deploy-ec2']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} '
                    sudo docker-compose down --rmi all
                    sudo docker-compose up -d
                    '
                    """
                }
            }
            post {
                success {
                    sh 'echo "multiline ssh 성공"'
                    slackSend(color: '#36a64f', message: "✅ Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' has completed successfully! (${env.BUILD_URL})")
                }
                failure {
                    sh 'echo "multiline ssh 실패"'
                    slackSend(color: '#FF0000', message: "❌ FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
                    exit 1
                }
            }
        }
    } // stages
} // pipeline
