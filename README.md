# Avito Android UI testing library

## Should I use it

Not now. Work in progress.

## Roadmap

- full test coverage of current features
- docs about all features
- [the issue](https://github.com/avito-tech/android-ui-testing/issues/11)

## Configure

```groovy
def uiTestingVersion = '0.2.2'

repositories {
    jcenter()
}

dependencies {
    androidTestImplementation 'com.avito.ui-testing:ui-testing-core:$uiTestingVersion'
    androidTestImplementation 'com.avito.ui-testing:ui-testing-maps:$uiTestingVersion'
}
```

### UITestConfig

Use UITestConfig to tune library's parameters for your project

Custom instrumentation test runner is the best place for it

## Core features

### Interceptors

Functions that invokes before every action and assertion with all information about target and intention.

#### Use cases

 - Log human readable walk-through of your tests (greatly increases report clarity)
 
#### Setup 

```
with(UITestConfig) {
    actionInterceptors += <your interceptor>
    assertionInterceptors += <your interceptor>
}
```

## Examples

See `test-app/androidTest`

## Contribution

### Release

To upload to bintray (mirrored to jcenter): `./gradlew bintrayUpload --info`

Use `--info` to debug `bintrayUpload` task, because if completes silently in case of error

Required environment variables:

 - BINTRAY_USER
 - BINTRAY_API_KEY
 - BINTRAY_GPG_PASSPHRASE