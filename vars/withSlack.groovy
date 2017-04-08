def call(Map args, Closure body) {
    def prev = currentBuild.rawBuild.previousBuild?.result
    def name = env.JOB_NAME + " " + env.BUILD_DISPLAY_NAME
    def text
    def attachments = []
    try {
        body()

        if (prev && prev == hudson.model.Result.FAILURE) {
            text = "$name is back to stable!"
            attachments << [fallback: text,
                            color   : "good"]
        }
    } catch (e) {
        def classic = env.BUILD_URL
        def console = "${env.BUILD_URL}console"
        def blueocean = "${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME}/detail/${env.JOB_NAME}/${env.BUILD_NUMBER}/pipeline"

        def fields = [[title: "Classic url",
                       value: classic,
                       short: false],
                      [title: "Console url",
                       value: console,
                       short: false],
                      [title: "Blue Ocean url",
                       value: blueocean,
                       short: false],
                      [title: "Cause",
                       value: e.cause,
                       short: false]]
        text = (prev && prev == hudson.model.Result.FAILURE) ? "$name is still failing" : "$name is failing"
        attachments << [fallback: text,
                        color   : "danger",
                        fields  : fields]
        currentBuild.result = "FAILED"
        throw e
    } finally {
        if (text && attachments) {
            args['text'] = text
            args['attachments'] = attachments
            slackMessage args
        }
    }
}
