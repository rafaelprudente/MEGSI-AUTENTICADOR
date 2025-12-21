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
      command: ["cat"]
      tty: true

    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      command: ["/busybox/sh"]
      args: ["-c", "sleep 999999"]
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

  parameters {
    booleanParam(
      name: 'APPLY_MIGRATIONS',
      defaultValue: false,
      description: 'Apply migrations to database?'
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
          sh 'mvn -ntp clean package -DskipTests'
        }
      }
    }

    stage('Build & Push Image (Kaniko)') {
      steps {
        container('kaniko') {
          sh '''
            /kaniko/executor \
              --context $WORKSPACE \
              --dockerfile Dockerfile \
              --destination $REGISTRY/$PROJECT/$IMAGE_NAME:$IMAGE_TAG \
              --destination $REGISTRY/$PROJECT/$IMAGE_NAME:latest \
              --insecure \
              --skip-tls-verify
          '''
        }
      }
    }

    stage('Apply Migrations') {
      when {
        expression { params.APPLY_MIGRATIONS }
      }
      steps {
        container('maven') {
          sh 'mvn -ntp flyway:migrate -Dflyway.configFiles=flywayConfig.conf'
        }
      }
    }

    stage('Restart Application') {
      steps {
        container('maven') {
          sh '''
            curl -LO https://dl.k8s.io/release/v1.31.4/bin/linux/amd64/kubectl
            install -m 0755 kubectl /usr/local/bin/kubectl

            kubectl version --client
            kubectl scale deployment megsi-authenticator -n uminho --replicas=0
            sleep 10
            kubectl scale deployment megsi-authenticator -n uminho --replicas=1
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
