
class DockerImage:

    def __init__(self, name, location, registry):
        self.name = name
        self.location = location
        self.registry = registry

    def getFullName(self, tag):
        return self.registry + "/" + self.name + ":" + tag

