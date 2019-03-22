pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    stages {
        stage 'Clone the project'
        git 'https://github.com/drackows/example-a.git'

        dir('example-a-pipeline') {

            stage('Back-end') {
                agent {
                    docker { image 'maven:3-alpine' }
                }
                steps {
                    sh 'mvn --version'
                }
            }

            stage('Front-end') {
                agent {
                    docker { image 'node:7-alpine' }
                }
                steps {
                    sh 'node --version'
                }
            }

            stage("Compilation and Analysis") {
                parallel 'Compilation': {
                    sh "mvn clean install -DskipTests"
                }, 'Static Analysis': {
                    stage("Checkstyle") {
                        sh "mvn checkstyle:checkstyle"

                        step([$class: 'CheckStylePublisher',
                          canRunOnFailed: true,
                          defaultEncoding: '',
                          healthy: '100',
                          pattern: '**/target/checkstyle-result.xml',
                          unHealthy: '90',
                          useStableBuildAsReference: true
                        ])
                    }
                }
            }

            stage("Tests and Deployment") {
                parallel 'Unit tests': {
                    stage("Runing unit tests") {
                        try {
                            sh "./mvnw test -Punit"
                        } catch(err) {
                            step([$class: 'JUnitResultArchiver', testResults:
                              '**/target/surefire-reports/TEST-*UnitTest.xml'])
                            throw err
                        }
                       step([$class: 'JUnitResultArchiver', testResults:
                         '**/target/surefire-reports/TEST-*UnitTest.xml'])
                    }
                }, 'Integration tests': {
                    stage("Runing integration tests") {
                        try {
                            sh "./mvnw test -Pintegration"
                        } catch(err) {
                            step([$class: 'JUnitResultArchiver', testResults:
                              '**/target/surefire-reports/TEST-'
                                + '*IntegrationTest.xml'])
                            throw err
                        }
                        step([$class: 'JUnitResultArchiver', testResults:
                          '**/target/surefire-reports/TEST-'
                            + '*IntegrationTest.xml'])
                    }
                }

                stage("Staging") {
                    sh "echo FINISH HI!!"
                }
            }
        }
    }
}
