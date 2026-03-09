SUMMARY = "Pre-compiled OpenJDK JRE Binary (Native)"
DESCRIPTION = "Provides a Java runtime to the build environment without compiling OpenJDK from source."
HOMEPAGE = "https://adoptium.net/"
LICENSE = "GPL-2.0-with-classpath-exception"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-with-classpath-exception;md5=b988fceba7f01704604e223b03650228"

inherit native

# Download a pre-compiled Linux x64 JRE tarball (Eclipse Temurin 21)
# Note: Ensure the URL points to a valid release if this specific version is rotated out
SRC_URI = "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.2%2B13/OpenJDK21U-jre_x64_linux_hotspot_21.0.2_13.tar.gz;subdir=jre-bin"
SRC_URI[sha256sum] = "51141204fe01a9f9dd74eab621d5eca7511eac67315c9975dbde5f2625bdca55"

# It's a pre-compiled binary, so skip the configure and compile steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # 1. Create the target directory in the native sysroot
    install -d ${D}${libdir}/jvm/jre

    # 2. Copy the unpacked JRE contents (Whinlatter syntax uses UNPACKDIR)
    cp -r ${UNPACKDIR}/jre-bin/jdk-21.0.2+13-jre/* ${D}${libdir}/jvm/jre/

    # 3. Symlink the java binary into the native bin directory so it is in PATH
    install -d ${D}${bindir}
    ln -sf ../lib/jvm/jre/bin/java ${D}${bindir}/java
}

# Ignore QA warnings about pre-stripped binaries, since we didn't compile them
INSANE_SKIP:${PN} = "already-stripped"
