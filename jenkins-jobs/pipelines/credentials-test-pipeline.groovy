pipeline{
    agent {
        label 'Slave'
    }
    stages{
        stage('CleanWorkspace') {
            steps {
                cleanWs()
            }
        }

        stage('Test Credentials'){
            steps {
                withCredentials([
                        string(credentialsId: "${ANSIBLE_VAULT_PASSWORD}", variable: 'ANSIBLE_PASSWORD'),
                        sshUserPrivateKey(credentialsId: "${GITLAB_CREDENTIALS}", keyFileVariable: 'GITLAB_CREDS'),
                        usernamePassword(credentialsId: "${QUAY_CREDENTIALS}", usernameVariable: 'QUAY_USERNAME', passwordVariable: 'QUAY_PASSWORD')
                ]) {
                    sh '''
                    set -x  
                    touch credentials.txt
                    echo "${ANSIBLE_PASSWORD}" >> credentials.txt
                    cat "${GITLAB_CREDS}" >> credentials.txt
                    echo "${QUAY_USERNAME}" >> credentials.txt
                    echo "${QUAY_PASSWORD}" >> credentials.txt
                    echo "Testing credentials done" >> credentials.txt
                    '''
                }
                sh 'cat credentials.txt'
            }
        }
    }
    post {
        success {
            archiveArtifacts "**/credentials.txt"
        }
    }
}