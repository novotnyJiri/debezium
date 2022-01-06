pipelineJob('jenkins-credentials-test') {

    displayName('Jenkins-credentials-test')
    description('Testing new jenkins credentials binding')

    properties {
        githubProjectUrl('https://github.com/debezium/debezium')
    }

    logRotator {
        numToKeep(10)
    }

    parameters {
        //      CREDENTIALS
        stringParam('GITLAB_CREDENTIALS', 'gitlab-debeziumci-ssh', 'QE gitlab credentials id')
        stringParam('ANSIBLE_VAULT_PASSWORD', 'ansible-vault-password', 'Password for ansible vault in used ansible playbook')
        stringParam('QUAY_CREDENTIALS', 'rh-integration-quay-creds', 'Quay.io credentials id')
    }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins-jobs/pipelines/credentials-test-pipeline.groovy'))
            sandbox()
        }
    }

}