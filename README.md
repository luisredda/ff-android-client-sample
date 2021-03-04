# Example App using Harness Android SDK (ff-android-client-sdk) for Demo purposes

This Demo App will demonstrate the usage and capabilities of Android client SDK.

### Setup
Add following snippet to root project's `build.gradle` file:
```
buildscript {
    repositories {
        mavenCentral()
    }
```

In app `build.gradle` file add dependency for Harness's SDK
`implementation 'io.harness:ff-android-client-sdk:0.0.2'`


### Accounts used:
| No. | Account |
| ---- | ---------- |
| 1 | `Aptiv` |
| 2 | `Experian` |
| 3 | `Fiserv` |
| 4 | `Harness` |
| 5 | `Palo Alto Networks` |


During initialization, the target is one of the above accounts, selected from the list, on the first screen of the Demo App.

 In order to change the account, you would need to go back to the initial screen and select a different account to use.