def call(Map args) {
    def url = args['forUrl']
    Closure doIfExists = args['doIfExists'] as Closure
    Closure doIfDoesntExist = args['doIfDoesntExist'] as Closure

    def statusCode = sh returnStdout: true, script: "curl -I $url | head -n 1 | cut -d ' ' -f2"
    def releaseExists = "200" == statusCode.trim()
    if (releaseExists) {
        doIfExists?.call()
    } else {
        doIfDoesntExist?.call()
    }

    return releaseExists
}