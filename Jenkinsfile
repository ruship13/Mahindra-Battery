pipeline {
    agent any

    tools {
        maven 'mymaven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ruship13/Mahindra-Battery.git'
            }
        }
        stage('Build') 
        { steps 
        { sh 'mvn clean compile' 
        
        } 
        }
        stage('clean & install') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=sonar
                    '''
                }
            }
        }


   stage('Deployment') {
    steps {
        deploy adapters: [
            tomcat9(
                credentialsId: 'TomcatCreds',
                path: '',
                url: 'http://192.168.11.76:8088/'
               //url: 'http://15.206.211.214:8080/'
            )
        ],
        contextPath: null,
        war: 'target/*.war'
    }
}
        
    }

    post {
        success {
            echo 'Deployment Successful!'
        }
        failure {
            echo 'Build or Deployment Failed!'
        }
    }
}
