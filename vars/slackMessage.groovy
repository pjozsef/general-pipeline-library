def call(Map args){
    def slackURL = args['webhook'] ?: env.SLACK_WEBHOOK
    def message = args['text']
    def channel = args['channel']
    def username = args['username'] ?: 'jenkins'
    def iconEmoji = args['iconEmoji']
    def attachments = args['attachments']

    if(slackURL && message && channel){
        def payload = [
                text : message,
                channel: channel,
                username: username
        ]
        if(attachments){
            payload['attachments'] = attachments
        }
        if(iconEmoji){
            payload['icon_emoji'] = iconEmoji
        }
        def json = JsonOutput.toJson(payload)
        sh "curl -X POST --data-urlencode \'payload=${json}\' ${slackURL}"
    } else {
        echo 'Slack message configuration error, either slackURL, message or channel is missing!'
        echo 'Supplied arguments: ' + args.toString()
    }
}