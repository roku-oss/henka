package com.roku.henka

import org.gradle.internal.os.OperatingSystem;

enum OsType {
    DARWIN_AMD64, LINUX_AMD64, WINDOWS_AMD64

    static final getOsType() {
        if (OperatingSystem.current().isWindows()) {
            return WINDOWS_AMD64
        }
        if (OperatingSystem.current().isLinux()) {
            return LINUX_AMD64
        }
        if (OperatingSystem.current().isMacOsX()) {
            return DARWIN_AMD64
        }
    }
}