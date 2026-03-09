SUMMARY = "Kaitai Struct Compiler"
DESCRIPTION = "Kaitai Struct is a declarative language used to describe various binary data structures."
HOMEPAGE = "https://kaitai.io/"
LICENSE = "GPL-3.0-or-later"

# Using the common license directory since the universal zip doesn't include a standalone LICENSE file
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-or-later;md5=1c76c4cc354acaac30ed4d5eefea7245"

SRC_URI = "https://github.com/kaitai-io/kaitai_struct_compiler/releases/download/${PV}/kaitai-struct-compiler-${PV}.zip"
SRC_URI[sha256sum] = "ff89389d9dc9e770d78a24af328763cb1f8e7b31ce7766c9edf10669a060f2a2"

inherit native
DEPENDS = "openjdk-bin-native"

S = "${UNPACKDIR}/kaitai-struct-compiler-${PV}"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${datadir}/kaitai-struct-compiler

    cp -r ${S}/bin ${D}${datadir}/kaitai-struct-compiler/
    cp -r ${S}/lib ${D}${datadir}/kaitai-struct-compiler/

    chmod +x ${D}${datadir}/kaitai-struct-compiler/bin/kaitai-struct-compiler

    install -d ${D}${bindir}
    ln -sf ../share/kaitai-struct-compiler/bin/kaitai-struct-compiler ${D}${bindir}/kaitai-struct-compiler
}
