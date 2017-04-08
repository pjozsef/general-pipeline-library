def call(Map args){
    def slackURL = args['webhook'] ?: env.SLACK_WEBHOOK
    def message = args['text']
    def channel = args['channel']
    def username = args['username'] ?: 'jenkins'
    def iconEmoji = args['iconEmoji']

    if(slackURL && message && channel){
        def payload = [
                text : message,
                channel: channel,
                username: username
        ]
        if(iconEmoji){
            payload['icon_emoji'] = iconEmoji
        }
        def json = JsonOutput.toJson(payload)
        sh "curl -X POST --data-urlencode \'payload=${json}\' ${slackURL}"
    } else {
        echo 'Slack message configuration error, either slackURL, message or channel is missing!'
        echo 'Supplied arguments: ' + margs.toString()
    }
}