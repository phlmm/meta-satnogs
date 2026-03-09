LICENSE = "LGPL-2.0-only"
LIC_FILES_CHKSUM = "file://lesser.txt;md5=2cc41cbc9eccda3e7419e519635b7a32"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = " \
    git://github.com/quiet/libfec.git;protocol=https;branch=master \
    "

# need gen_ccsds binaries to build libfec for non-native target
BBCLASSEXTEND += "native"
DEPENDS += "libfec-native"

PV = "2.1.1+git${SRCPV}"
SRCREV = "9750ca0a6d0a786b506e44692776b541f90daa91"

inherit autotools

# 1. Force the Makefile to build the shared library as libfec.so.2
EXTRA_OEMAKE += "SHARED_LIB=libfec.so.2"

# 2. Fix the hardcoded build rule in the Makefile
do_configure:prepend() {
    bbnote "Fixing hardcoded shared library target..."

    # Change "libfec.so: $(LIBS)" to "$(SHARED_LIB): $(LIBS)"
    sed -i 's/^libfec.so:/$(SHARED_LIB):/g' ${S}/makefile.in
}

# 3. Clean manual install to bypass the broken upstream install script
do_install() {
    bbnote "Using custom do_install..."

    install -d ${D}${includedir}
    install -d ${D}${libdir}
    install -d ${D}${mandir}/man3

    # Headers from source (${S})
    install -m 0644 ${S}/fec.h ${D}${includedir}/

    # Compiled binaries from build dir (${B})
    install -m 0644 ${B}/libfec.a ${D}${libdir}/
    install -m 0755 ${B}/libfec.so.2 ${D}${libdir}/

    # The GNU compliant symlink
    ln -sf libfec.so.2 ${D}${libdir}/libfec.so

    # Man pages from source (${S})
    install -m 0644 ${S}/simd-viterbi.3 ${D}${mandir}/man3/
    install -m 0644 ${S}/rs.3 ${D}${mandir}/man3/
    install -m 0644 ${S}/dsp.3 ${D}${mandir}/man3/
}

do_compile:append() {
	cp ${S}/fec.h ${S}/*3 .
}

do_install:append:class-native () {
    install -d ${D}${bindir}
    install -m 755 -p ${B}/gen_ccsds ${B}/gen_ccsds_tal ${D}${bindir}/
}

FILES:${PN} += " \
    ${libdir}/libfec.so \
    ${datadir}/man/man3/dsp.3 \
    ${datadir}/man/man3/simd-viterbi.3 \
    ${datadir}/rs.3 \
    ${includedir}/fec.h \
"
