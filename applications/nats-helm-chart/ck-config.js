
module.exports = {
  "name": "nats-broker",
  "applicationStructure": "helm-chart",
  "defaultEnvironment": "test",
  "scripts": {
    "helmDebug": "helm install --dry-run --debug .",
  },
  "helm": {
    "releaseName": "test-nats"
  },
}
