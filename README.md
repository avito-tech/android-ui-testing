# Avito Android UI testing library

## Should I use it

Not now. Work in progress.

######Roadmap: TBD

One does not simply say WIP. 
We should announce what the DoD is. 

## Features

Handy checks and actions for common Android UI elements! 

`TBD: Simple samples? ` 

```
view.click()

swipeRefreshElement.checks.isRefreshing()

list.cellAt(position = 1).title.checks.displayedWithText("2")
```

More examples at `test-app/androidTest`


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