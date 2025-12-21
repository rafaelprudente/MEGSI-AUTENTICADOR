pipeline {
  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
spec:
  restartPolicy: Never
  containers:
      - name: maven
        image: maven:3.9.9-eclipse-temurin-21
        command:
          - cat
        tty: true

      - name: kaniko
        image: gcr.io/kaniko-project/executor:debug
        command:
          - /busybox/sh
        args:
          - -c
          - sleep 999999
        volumeMounts:
          - name: docker-config
            mountPath: /kaniko/.docker
  volumes:
    - name: docker-config
      secret:
        secretName: onedev-registry
        items:
          - key: .dockerconfigjson
            path: config.json
"""
    }
  }

  /* ✅ CHECKBOX */
  parameters {
    booleanParam(
      name: 'BUILD_AND_PUSH_IMAGE',
      defaultValue: true,
      description: 'Buildar e publicar a imagem Docker?'
    )
  }

  environment {
    REGISTRY   = "192.168.56.214:30610"
    PROJECT    = "uminho"
    IMAGE_NAME = "megsi-authenticator"
    IMAGE_TAG  = "${env.BUILD_NUMBER}"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build JAR') {
      steps {
        container('maven') {
          sh 'mvn clean package -DskipTests'
        }
      }
    }

    stage('Build & Push Image (Kaniko)') {
      /* ✅ CONDICIONAL */
      when {
        expression { params.BUILD_AND_PUSH_IMAGE }
      }
      steps {
        container('kaniko') {
          sh '''
            /kaniko/executor \
              --context $WORKSPACE \
              --dockerfile Dockerfile \
              --destination $REGISTRY/$PROJECT/$IMAGE_NAME:latest \
              --insecure \
              --skip-tls-verify
          '''
        }
      }
    }
  }

  post {
    success {
      echo "Pipeline executado com sucesso."
      echo "Imagem: $REGISTRY/$PROJECT/$IMAGE_NAME:$IMAGE_TAG"
    }
    failure {
      echo "Falha no pipeline."
    }
  }
}
