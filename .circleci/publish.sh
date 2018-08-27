#!/usr/bin/env bash

set -e

CURRENT_TAG=$(git tag -l --points-at HEAD)

if [[ ! "$CURRENT_TAG" =~ v* ]]; then
    echo "publish can be run only on v* tag"
    exit 1
fi

CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [[ "$CURRENT_BRANCH" != "master" ]]; then
    echo "publish can be run only on master branch"
    exit 1
fi

./gradlew bintrayUpload --info -PuiTestingVersion=${CURRENT_TAG}
