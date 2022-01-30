package com.worldbeers.beerista.utils

import timber.log.Timber

class LineNumberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }
}
