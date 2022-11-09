import hus.erd.Command

class DockerPublishCommand extends Command {
    public static final String TAG = "TAG"
    public static final String SNAPSHOT = 'SNAPSHOT'
    public static final String STATIC_VERSION = 'STATIC_VERSION'
    public static final String BUILD_DIRECTORY = 'BUILD_DIRECTORY'
    public static final String DOCKERFILE = 'DOCKERFILE'
    public static final String BUILD_ARGS = 'BUILD_ARGS'
    public static final String RESULT = 'RESULT'

    public static final String DEFAULT_BUILD_DIRECTORY = '.'
    public static final String DEFAULT_DOCKERFILE = ''
    public static final String DEFAULT_STATIC_VERSION = ''
    public static final String DEFAULT_RESULT = ''
    

    static DockerPublishCommand builder() {
        return new DockerPublishCommand()
    }

    private DockerPublishCommand() {
        this.metadata[TAG] = ''
        this.metadata[SNAPSHOT] = false
        this.metadata[STATIC_VERSION] = DEFAULT_STATIC_VERSION
        this.metadata[BUILD_DIRECTORY] = DEFAULT_BUILD_DIRECTORY
        this.metadata[DOCKERFILE] = DEFAULT_DOCKERFILE
        this.metadata[BUILD_ARGS] = []
        this.metadata[RESULT] = DEFAULT_RESULT
    }


    DockerPublishCommand withTag(String tag) {
        this.metadata[TAG] = tag
        return this
    }

    DockerPublishCommand withStaticVersion(String staticVersion) {
        this.metadata[STATIC_VERSION] = staticVersion
        return this
    }

    DockerPublishCommand withBuildDirectory(String buildDirectory) {
        this.metadata[BUILD_DIRECTORY] = buildDirectory
        return this
    }

    DockerPublishCommand withDockerfile(String dockerFile) {
        this.metadata[DOCKERFILE] = dockerFile
        return this
    }

    DockerPublishCommand asSnapshot() {
        this.metadata[SNAPSHOT] = true
        return this
    }

    DockerPublishCommand withBuildArg(String argName, String argValue) {
        this.metadata[BUILD_ARGS].add("${argName}=${argValue}")
        return this
    }

    DockerPublishCommand withBuildArg(String argName) {
        this.metadata[BUILD_ARGS].add(argName)
        return this
    }

    @Override
    Command build() {
        String dockerImageSubUrl = this.metadata.get(TAG)

        String staticVersion = this.metadata[STATIC_VERSION]
        String version = this.metadata.get(SNAPSHOT) ? "snapshot" : "latest"
        if (staticVersion ==  null ) {
            version = staticVersion
        }

        String dockerFileParam = ""
        String buildDirectory = this.metadata.get(BUILD_DIRECTORY)
        String dockerFile = this.metadata.get(DOCKERFILE)
        if (dockerFile != DEFAULT_DOCKERFILE) {
            if (buildDirectory != '') {
                dockerFileParam = " -f ${buildDirectory}/${dockerFile} "
            } else {
                dockerFileParam = " -f ${dockerFile} "
            }

        }

        // Docker build requires at least one directory to build. if empty use current directory.
        if (buildDirectory == '') {
            buildDirectory = '.'
        }

        def buildArgs = (List) this.metadata.get(BUILD_ARGS)
        String buildArgsStr = ""
        if (!buildArgs.isEmpty()){
            buildArgsStr = "--build-arg ${buildArgs.join(' --build-arg ')}"
        }


        // ex : gcr.io/softtech-rally/devops/new_ci_build
        String baseTag = "/${dockerImageSubUrl}"

        // ex : gcr.io/softtech-rally/devops/new_ci_build:12976
        // @FIXME : buildnumber may conflict. Must be changed!!!
        String dockerTag = "${baseTag}:ased"

        // examples
        // - gcr.io/softtech-rally/devops/new_ci_build:snapshot
        // - gcr.io/softtech-rally/devops/new_ci_build:latest
        String latestTag = "${baseTag}:${version}"

        this.metadata[RESULT] = "docker build --no-cache ${dockerFileParam} -t ${dockerTag} ${buildArgsStr} ${buildDirectory}"
        println(this.metadata[RESULT])
        return this
    }

    @Override
    String toString() {
        return this.metadata[RESULT]
    }

}
