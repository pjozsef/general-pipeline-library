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
        def classic = "$env.BUILD_URL"
        def console = "${env.BUILD_URL}console"
        def blueocean = "${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME}/detail/${env.JOB_NAME}/${env.BUILD_NUMBER}/pipeline"

        def fields = [field("Classic url", classic),
                      field("Console url", console),
                      field("Blue Ocean url", blueocean)]
        if(e.message){
            fields << field("Error message", e.message)
        }
        if(recursiveCause(e)){
            fields << field("Error cause", recursiveCause(e))
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

private static recursiveCause(e){
    def cause = e?.cause
    while(cause?.cause){
        cause = cause.cause
    }
    return (cause ?: e)?.class?.toString()
}

private static field(String title, String value){
    return [title: title,
            value: value,
            short: false]
}