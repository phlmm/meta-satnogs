SUMMARY = "C++ wrapper for libpng"
DESCRIPTION = "png++ aims to provide simple yet powerful C++ interface to libpng, the PNG reference implementation library."
HOMEPAGE = "https://www.nongnu.org/pngpp/"

# png++ uses a modified BSD license
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=22f1274bc9ba324d163e423c1baf0e19"

# We must depend on libpng since this is a wrapper for it
DEPENDS = "libpng"

SRC_URI = "https://download.savannah.nongnu.org/releases/pngpp/png++-${PV}.tar.gz"
SRC_URI[sha256sum] = "abbc6a0565122b6c402d61743451830b4faee6ece454601c5711e1c1b4238791"

S = "${UNPACKDIR}/png++-${PV}"

# Since it is a header-only library, there is nothing to configure or cross-compile.
# We tell BitBake to entirely skip these tasks to save build time.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    # The upstream Makefile has an 'install' target that respects the PREFIX variable.
    # We pass the sysroot destination path (${D}${prefix}) so the headers drop cleanly into /usr/include/png++
    oe_runmake install PREFIX=${D}${prefix}
}

# Yocto usually expects a recipe to produce a compiled binary package (${PN}).
# Since this only produces headers (which go into ${PN}-dev), we must tell Yocto
# it is okay for the main package to be empty, otherwise it will throw a QA error.
ALLOW_EMPTY:${PN} = "1"
