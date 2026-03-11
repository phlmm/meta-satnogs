SUMMARY = "SATNOGS GNU Radio flowgraphs"
LICENSE = "AGPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "gitsm://gitlab.com/librespacefoundation/satnogs/satnogs-flowgraphs.git;protocol=https;branch=master"

PV = "2.2"
SRCREV = "99e67302dfd48fcd6e90fbaedeff23b25333d6b1"

inherit gnuradio-oot python3native
PARALLEL_MAKE = ""
# -----------------------------------------------------------------
# Dependencies
# -----------------------------------------------------------------
# Added python3-pyyaml-native for grcc
DEPENDS += "\
    gr-satnogs \
    gr-satnogs-native \
    soapysdr \
    gnuradio-native \
    hamlib \
    hamlib-native \
    gnuradio \
    python3-pyyaml-native \
"

RDEPENDS:${PN} += "\
    gr-satnogs \
    soapysdr \
    gnuradio \
"

# -----------------------------------------------------------------
# CMake Configuration
# -----------------------------------------------------------------
# Needed to workaround "multiple definitions of target" error
OECMAKE_GENERATOR = "Unix Makefiles"

# Point CMake strictly to the NATIVE blocks so we don't cross-contaminate
EXTRA_OECMAKE += "-DGRC_BLOCKS_PATH=${STAGING_DATADIR_NATIVE}/gnuradio/grc/blocks:${STAGING_LIBDIR_NATIVE}/python3.13/site-packages/gnuradio/"

# -----------------------------------------------------------------
# Pre-Configure Hacks
# -----------------------------------------------------------------
do_configure:prepend() {
    bbnote "Hunting down and fixing upstream \${GRCC_EXECUTABLE}} typos..."
    find ${S} -name "CMakeLists.txt" -exec sed -i 's/\${GRCC_EXECUTABLE}}/\${GRCC_EXECUTABLE}/g' {} +

    bbnote "Fixing legacy python shebang in the native grcc tool..."
    if [ -f ${STAGING_BINDIR_NATIVE}/grcc ]; then
        sed -i 's|^#!/usr/bin/env python$|#!/usr/bin/env python3|g' ${STAGING_BINDIR_NATIVE}/grcc
    fi
}
FILES:${PN} += "${datadir}/flowgraph_dispatcher"
