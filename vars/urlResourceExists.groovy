def call(Map args) {
    def url = args['forUrl']
    def acceptableStatusCodes = args['withAcceptableStatusCodes'] || ['200']
    Closure doIfExists = args['doIfExists'] as Closure
    Closure doIfDoesntExist = args['doIfDoesntExist'] as Closure

    def statusCode = sh returnStdout: true, script: "curl -I $url | head -n 1 | cut -d ' ' -f2"
    def resourceExists = statusCode.trim() in acceptableStatusCodes
    if (resourceExists) {
        doIfExists?.call()
    } else {
        doIfDoesntExist?.call()
    }

    return resourceExists
}