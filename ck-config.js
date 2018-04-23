
// see: http://github.com/remipassmoilesel/companion-kube

module.exports = {
    "name": "spring-k8s-demo",
    "applicationStructure": "deployment",
    "defaultEnvironment": "dev",
    "scripts": {
        "kubectlDebug": "kubectl create -f . --dry-run"
    },
    "docker": {
        "imageName": "docker.remi-pace.fr:443/spring-k8s-demo",
        "tag": "0.1",
        "push": true,
        "buildDirectory": "."
    },
    "deployment": {
        "roots": [
            "./kubernetes"
        ]
    },
    "hooks": {
        "preBuild": "./scripts/package-app.sh"
    }
};
