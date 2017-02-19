package com.roku.henka

import org.apache.tools.ant.taskdefs.condition.Os

enum OsType {
    DARWIN_AMD64, LINUX_AMD64, WINDOWS_AMD64

    public static final getOsType() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return WINDOWS_AMD64
        }
        if (Os.isFamily(Os.FAMILY_UNIX)) {
            return LINUX_AMD64
        }
        if (Os.isFamily(Os.FAMILY_MAC)) {
            return DARWIN_AMD64
        }
    }
}