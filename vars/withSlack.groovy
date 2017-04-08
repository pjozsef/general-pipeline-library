def call(Map args, Closure body) {
    def prev = currentBuild.rawBuild.previousBuild?.result
    def text
    def attachments = []
    try {
        body()

        if (prev && prev == hudson.model.Result.FAILURE) {
            text = "$env.JOB_NAME is back to stable!"
            attachments << [fallback: text,
                            color   : "good",
                            text: "Yay! \\(^_^)/",
                            thumb_url: 'https://itisatechiesworld.files.wordpress.com/2015/01/cool-jenkins2x3.png']
        }
    } catch (e) {
        def name = env.JOB_NAME + " " + env.BUILD_DISPLAY_NAME
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
                       short: false]]
        if(e.message){
            fields << [title: "Error message",
                       value: e.message,
                       short: false]
        }
        if(recursiveCause(e)){
            fields << [title: "Error cause",
                       value: recursiveCause(e),
                       short: false]
        }
        text = (prev && prev == hudson.model.Result.FAILURE) ? "$name is still failing" : "$name is failing"
        attachments << [fallback: text,
                        color   : "danger",
                        fields  : fields,
                        thumb_url: 'https://www.cloudbees.com/sites/default/files/blog/butler-devil.png']
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

private recursiveCause(e){
    def cause = e?.cause
    while(cause?.cause){
        cause = cause.cause
    }
    return (cause ?: e)?.class?.toString()
}
