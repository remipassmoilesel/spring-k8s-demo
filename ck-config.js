module.exports = {
    applicationStructure: 'deployment',
    scripts: {
        localDev: 'cd app && ./launch-dev.sh',
        buildDev: 'docker build . -t spring-k8s-demo-dev',
        dev: 'ck run buildDev && docker run spring-k8s-demo-dev',
    },
    docker: {
        imageName: 'spring-k8s-demo',
        tag: '0.1',
        push: false,
        pushBySSH: true,
        buildDirectory: '.',
    },
    hooks: {
        preBuild: 'cd app && ./package.sh',
    }
};