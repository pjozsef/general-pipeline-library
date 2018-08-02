# general-pipeline-library
Public repository for general Jenkins pipeline steps.

## How to use in your own Jenkins setup
If you are not sure about how to use shared libraries, take a look at the [documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries).
It is advised to either use a specific commit/tag, instead of always the latest version.

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
| body          | The closure which is wrapped by the withSlack step. If the body is failing, or failed previously and now passes, then a Slack message will be generated automatically. | Closure () -> () |  ✔
| username      | the username of the bot posting the message. Defaults to 'jenkins' | String | ✘ |
| iconEmoji     | Slack emoji that is shown as the icon for the message | String | ✘ |


### gitDescribe
A convenience method to invoke the `git describe` command. Returns a GitDescribe object, wrapping the result. For more information, 
see the Utility Classes section.
```groovy
def describe = gitDescribe commitish: 'c65859d'
echo describe.raw
echo describe.mostRecentTag
echo describe.isReleaseVersion.toString()
```
| argument      | description                                    | type      | mandatory |
| :-:           | :-:                                            | :-:       | :-:       |
| commitish     | the commit-ish object to describe. Defaults to 'HEAD'  | String    | ✘         |


### urlResourceExists
Checks via a GET request whether an HTTP resource exists or not. 
Returns true if the response's statusCode is acceptable,
false otherwise. Either the return value can be used or the supported result Closures. 
By default, only HTTP status code 200 is considered acceptable.
```groovy
def url = "https://github.com"
def existsAction = {
    echo "Resource exists!"
}
def notExistsAction = {
    echo "Resource does not exist!"
}
def codes = [200, 201, 202]
urlResourceExists forUrl: url, withAcceptableStatusCodes: codes, doIfExists: existsAction, doIfDoesntExist: notExistsAction
```
| argument      | description                                    | type      | mandatory |
| :-:           | :-:                                            | :-:       | :-:       |
| forUrl        | the URL to check against                       | String    | ✔         |
| withAcceptableStatusCodes        | you can specify which HTTP response status codes are considered acceptable. Defaults to status code 200.  | List<Integer/String>    | ✘         |
| doIfExists    | the closure to invoke when the resource exists  | Closure () -> ()    | ✘         |
| doIfDoesntExist | the closure to invoke when the resource does not exist  | Closure () -> ()    | ✘         |


## Utility classes

### GitDescribe
This class represents the result of the `git describe` command with the following fields:
* raw: The raw git describe output
* mostRecentTag: The most recent tag that is reachable from the commit
* isReleaseVersion: True if the commit is tagged
[Located here](https://github.com/pjozsef/general-pipeline-library/blob/master/src/com/github/pjozsef/GitDescribe.groovy)

For the following `git describe` outputs: 
```shell
1.6.0-2-gc65859d
2.3.1
```
The corresponding GitDescribe objects are:
```
GitDescribe('1.6.0-2-gc65859d', '1.6.0', false)
GitDescribe('2.3.1', '2.3.1', true)
```

## Other project
[android-pipeline-library](https://github.com/pjozsef/android-pipeline-library), a Jenkins Pipeline library that focuses on Android device specific tasks, like building, testing, rebooting devices, etc.


## Real life example
For those who'd like to see something more complex than code snippets, [this Jenkinsfile](https://github.com/emartech/android-mobile-engage-sdk/blob/master/Jenkinsfile) is a real life usage of this library, utilizing both slack steps.


## Legal stuff
This library is provided as-is, under the MIT license, without any warranties, what so ever.<br>
Pull requests are welcome, if you find something.
