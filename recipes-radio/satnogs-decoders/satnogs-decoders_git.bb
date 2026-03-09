SUMMARY = "SatNOGS telemetry decoders"
DESCRIPTION = "Telemetry decoders for the SatNOGS network generated from Kaitai Struct definitions."
HOMEPAGE = "https://gitlab.com/librespacefoundation/satnogs/satnogs-decoders"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

SRC_URI = "git://gitlab.com/librespacefoundation/satnogs/satnogs-decoders.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

inherit python_setuptools_build_meta

DEPENDS += "kaitai-struct-compiler-native python3-versioneer-native"

do_configure:prepend() {
    bbnote "Relaxing strict versioneer dependency..."

    # Search and replace the strict versioning in all possible config files
    for f in requirements.txt setup.cfg pyproject.toml setup.py; do
        if [ -f ${S}/$f ]; then
            # Replace 'versioneer==0.28' with just 'versioneer'
            sed -i 's/versioneer==0.28/versioneer/g' ${S}/$f
            sed -i 's/versioneer == 0.28/versioneer/g' ${S}/$f
        fi
    done
}

# Generate the Python decoders natively before the setuptools build starts
do_compile:prepend() {
    bbnote "Compiling KSY files natively ..."

    # Run the exact same command the old Docker script used, but natively
    kaitai-struct-compiler --target python --outdir ${S}/satnogsdecoders/decoder ${S}/ksy/*.ksy
}

RDEPENDS:${PN} = " \
    python3-core \
    python3-kaitai-struct \
    python3-construct \
"
