package com.github.pjozsef

import groovy.transform.TupleConstructor

@TupleConstructor()
class GitDescribe implements Serializable {
    def raw
    def mostRecentTag
    def isReleaseVersion

    @Override
    String toString(){
        return "GitDescribe(raw=$raw, mostRecentTag=$mostRecentTag, isReleaseVersion=$isReleaseVersion)"
    }
}