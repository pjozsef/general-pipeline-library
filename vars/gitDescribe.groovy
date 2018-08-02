import com.github.pjozsef.GitDescribe

def call(Map args) {
    def commitish = args?.get('commitish') ?: 'HEAD'
    def rawDescribe = sh(script: "git describe $commitish", returnStdout: true).trim()

    def pattern = ~/\d+\.\d+\.\d+/
    def matcher = rawDescribe =~ pattern

    def mostRecentTag = matcher.count < 1 ? null : matcher[0]
    def isReleaseVersion = rawDescribe ==~ pattern

    return new GitDescribe(rawDescribe, mostRecentTag, isReleaseVersion)
}
