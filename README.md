# Avito Android UI testing library

## Should I use it

Not now. Work in progress.

#####Roadmap:

- full test coverage of current features
- docs about all features
- more at [the issue](https://github.com/avito-tech/android-ui-testing/issues/11)

## Configure

```groovy
def uiTestingVersion = '0.1.1'

repositories {
    jcenter()
}

dependencies {
    androidTestImplementation 'com.avito.ui-testing:ui-testing-core:$uiTestingVersion'
    androidTestImplementation 'com.avito.ui-testing:ui-testing-maps:$uiTestingVersion'
}
```

## Features

Handy checks and actions for common Android UI elements! 

`TBD: Simple samples? ` 

```
view.click()

swipeRefreshElement.checks.isRefreshing()

list.cellAt(position = 1).title.checks.displayedWithText("2")
```

More examples at `test-app/androidTest`
