pipeline {
    environment {        
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // dockerhub : jenkins에 등록해 놓은 docker hub credentials 이름
        TARGET_HOST = "ubuntu@43.203.226.104" // @ 뒤에는 web-server public ip
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
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                    exit 1
                }
            }    
        }

         //clone 받은 프로젝트 안의 Spring10_Security_Thymeleaf_Jenkins 디렉토리에서 stage 실행
        stage('Build') {
            steps {    
                // dir("Spring10_Security_Thymeleaf_Jenkins"){   //var/jenkins_home/workspace/pipeline_item/Spring10_Security_Thymeleaf_Jenkins
                    sh "mvn -DskipTests clean compile package"
                // }  
            }
            post {
                success {
                    echo 'maven build success'
                }

                failure {
                    echo 'maven build failed'
                    exit 1
                }
            } 
        }

        stage('Dockerizing'){
            steps{
                // dir("Spring10_Security_Thymeleaf_Jenkins"){   ///var/jenkins_home/workspace/pipeline_item/Spring10_Security_Thymeleaf_Jenkins/Dockerfile 위치
                    sh 'echo " Image Bulid Start"'

                     //예) 빌드 번호가 10번인 경우      도커허브_username/security:10  이름의 이미지 생성
                    sh 'docker build . -t $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER'

                    // 도커허브_username/security:latest 이름의 이미지 생성
                    sh 'docker build . -t $DOCKERHUB_CREDENTIALS_USR/jabibim_admin'
                // }
            }
                post {
                    success {
                        sh 'echo "Bulid Docker Image Success"'
                    }

                    failure {
                        sh 'echo "Bulid Docker Image Fail"'
                        exit 1
                    }
                }
        }
    
        stage('Login'){
            steps{
                 sh 'docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW'  
            }
            
            post {
                success {
                    sh 'echo "Docker Login Success"'
                }

                failure {
                    sh 'echo "Docker Login Fail"'
                    exit 1
                }
            }
        }

        stage('Docker hub push'){
            steps{
                sh 'echo " docker hub push"'
                sh 'docker push  $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER'
                sh 'docker push  $DOCKERHUB_CREDENTIALS_USR/jabibim_admin'
            }
            post {
                success {
                    sh 'echo "Docker hub push Success"'
                }

                failure {
                    sh 'echo "Docker hub push Fail"'
                    exit 1
                }
            }
        }    

        stage('Cleaning up'){
            steps{
                sh 'echo " docker image rmi"'
                sh "docker rmi $DOCKERHUB_CREDENTIALS_USR/jabibim_admin:$BUILD_NUMBER" // docker image 제거
                sh "docker rmi $DOCKERHUB_CREDENTIALS_USR/jabibim_admin"              // docker image 제거
            }
            post {
                success {
                     sh 'echo " image rmi Success"'
                }

                failure {
                    sh 'echo " image rmi Fail"'
                    exit 1
                }
            }
        }

        stage('multiline ssh') {
            steps {
                sshagent (credentials: ['deploy-ec2']) {
                sh """
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} '
                    sudo docker-compose down --rmi all
                    sudo docker-compose up -d
                    '
                """
                }
            }
        }
   }//stages
}//pipeline
