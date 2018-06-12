pipeline {
  agent none

  stages {
    stage('Build') {
      agent {
        docker { image 'govukverify/java8:latest' }
      }
      steps {
        sh './gradlew clean build'
      }
    }

    stage('Publish') {
      when { branch 'master' }
      agent {
        docker { image 'govukverify/java8:latest' }
      }
      environment {
        // Artifactory credentials
        ARTIUSER = "${env.ARTIFACTORYUSER}"
        ARTIPASSWORD = "${env.ARTIFACTORYPASSWORD}"
        // Bintray credentials
        BINTRAY_USER    = 'richardtowers'
        BINTRAY_API_KEY = credentials('bintray-api-key')
      }
      steps {
        sh './gradlew publish'
      }
    }
  }
}
