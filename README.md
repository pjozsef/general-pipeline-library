# general-pipeline-library
Public repository for general Jenkins pipeline steps.

## How to use in your own Jenkins setup
If you are not sure about how to use shared libraries, take a look at the [documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries).

## Pipeline steps
All steps can be found under the vars folder.

### slackMessage
Sends a custom message to Slack. The webhook, channel and text arguments must be supplied in order to be able to send the message.
```groovy
//webhook is set by SLACK_WEBHOOK environment variable
slackMessage channel: 'your-slack-channel', text: 'test message'
```
| argument      | description                                    | type      | mandatory |
| :-:           | :-:                                            | :-:       | :-:       |
| webhook       | the URL you got from Slack when integrating Incoming WebHooks. If not set, the step looks for it under SLACK_WEBHOOK environment variable. If there is no value specified at either places, the message wont be sent. | String    | ✔         |
| channel       | the Slack channel to post the message to. If not set, the message wont be sent.       | String    | ✔ |
| text          | the content of the message.  If not set, the message wont be sent.  | String | ✔ |
| username      | the username of the bot posting the message. Defaults to 'jenkins' | String | ✘ |
| iconEmoji     | Slack emoji that is shown as the icon for the message | String | ✘ |
| attachments   | Slack attachment, see the [documentation](https://api.slack.com/docs/message-attachments) for more info. | Map<String, Object> | ✘ |

### withSlack
A wrapper around your pipeline steps that notifies the channel if the build fails, or if it becomes stable again. If both the current and previous builds passed, there will be no messages generated, in order to avoid spamming the channel.
```groovy
node {
    withSlack channel:'your-slack-channel', {
        stage("build"){
            //can possibly fail
        }

        stage("test"){
            //can possibly fail
        }
    }
}
```
| argument      | description                                    | type      | mandatory |
| :-:           | :-:                                            | :-:       | :-:       |
| webhook       | the URL you got from Slack when integrating Incoming WebHooks. If not set, the step looks for it under SLACK_WEBHOOK environment variable. If there is no value specified at either places, the message wont be sent. | String    | ✔         |
| channel       | the Slack channel to post the message to. If not set, the message wont be sent.       | String    | ✔ |
| body          | The closure which is wrapped by the withSlack step. If the body is failing, or failed previously and now passes, then a Slack message will be generated automatically. | CLosure () -> () |  ✔
| username      | the username of the bot posting the message. Defaults to 'jenkins' | String | ✘ |
| iconEmoji     | Slack emoji that is shown as the icon for the message | String | ✘ |


## Other project
[android-pipeline-library](https://github.com/pjozsef/android-pipeline-library), a Jenkins Pipeline library that focuses on Android device specific tasks, like building, testing, rebooting devices, etc.


## Real life example
For those who'd like to see something more complex than code snippets, [this Jenkinsfile](https://github.com/emartech/android-mobile-engage-sdk/blob/master/Jenkinsfile) is a real life usage of this library, utilizing both slack steps.


## Legal stuff
This library is provided as-is, under the MIT license, without any warranties, what so ever.<br>
Pull requests are welcome, if you find something.
