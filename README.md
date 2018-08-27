# Avito Android UI testing library

## Should I use it?

Not now. Work in progress.

## Roadmap

See [Milestones](https://github.com/avito-tech/android-ui-testing/milestones)

## Configure

```groovy
repositories {
    jcenter()
}

dependencies {
    androidTestImplementation 'com.avito.ui-testing:ui-testing-core:$uiTestingVersion'
    androidTestImplementation 'com.avito.ui-testing:ui-testing-maps:$uiTestingVersion'
}
```

See [Releases](https://github.com/avito-tech/android-ui-testing/releases) for latest `$uiTestingVersion`.

### UITestConfig

Use UITestConfig to tune library's parameters for your project.
Custom instrumentation test runner is the best place for it.

## Core features

### Interceptors

Functions that are invoked before every action and assertion with all information about target and intention.

#### Use cases

 - Log human readable walk-through of your tests (greatly increases report clarity).
 
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

Push `vX.X.X` tag to master