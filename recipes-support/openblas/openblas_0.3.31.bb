SUMMARY = "OpenBLAS: An optimized BLAS library based on GotoBLAS2"
DESCRIPTION = "OpenBLAS is an optimized BLAS library based on GotoBLAS2 1.13 BSD version."
HOMEPAGE = "http://www.openblas.net/"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5adf4792c949a00013ce25d476a2abc0"

DEPENDS = "make libgfortran patchelf-native"

SRC_URI = "git://github.com/xianyi/OpenBLAS.git;protocol=https;branch=develop"
SRCREV = "76f1be470c9b9f80dc6e27407e13b975df436489"
PV = "0.3.21+git${SRCPV}"


# -----------------------------------------------------------------
# Architecture Mapping
# -----------------------------------------------------------------
BLAS_X86_ARCH ?= "ATOM"
BLAS_AARCH32_ARCH ?= "CORTEXA9"
BLAS_AARCH64_ARCH ?= "ARMV8"
BLAS_ARM_ARCH ?= "ARMV7"

def map_arch(d):
    import re
    a = d.getVar('TARGET_ARCH') or ""
    if re.match('^i.86$', a) or re.match('^x86_64$', a):
        return d.getVar('BLAS_X86_ARCH')
    elif re.match('^aarch32$', a):
        return d.getVar('BLAS_AARCH32_ARCH')
    elif re.match('^aarch64$', a):
        return d.getVar('BLAS_AARCH64_ARCH')
    elif re.match('^arm$', a):
        return d.getVar('BLAS_ARM_ARCH')
    return a

def map_bits(d):
    import re
    a = d.getVar('TARGET_ARCH') or ""
    if re.match('^x86_64$', a) or re.match('^aarch64$', a):
        return "64"
    return "32"

# -----------------------------------------------------------------
# Configuration
# -----------------------------------------------------------------
PACKAGECONFIG ??= "dynarch lapack cblas"

PACKAGECONFIG[lapack] = "NO_LAPACK=0,NO_LAPACK=1,"
PACKAGECONFIG[lapacke] = "NO_LAPACKE=0,NO_LAPACKE=1,"
PACKAGECONFIG[cblas] = "NO_CBLAS=0,NO_CBLAS=1,"
PACKAGECONFIG[affinity] = "NO_AFFINITY=0,NO_AFFINITY=1,"
PACKAGECONFIG[openmp] = "USE_OPENMP=1,USE_OPENMP=0,gcc-runtime"
PACKAGECONFIG[dynarch] = "DYNAMIC_ARCH=1,DYNAMIC_ARCH=0,"

EXTRA_OEMAKE = "\
    HOSTCC='${BUILD_CC}' \
    CC='${CC}' \
    FC='${FC}' \
    PREFIX='${exec_prefix}' \
    CROSS=1 \
    CROSS_SUFFIX='${TARGET_PREFIX}' \
    NO_STATIC=1 \
    NO_UTEST=1 \
    NO_TEST=1 \
    BINARY='${@map_bits(d)}' \
    TARGET='${@map_arch(d)}' \
    ${PACKAGECONFIG_CONFARGS} \
"
# -----------------------------------------------------------------
# Native Build Make Variables
# -----------------------------------------------------------------
# When building for the host (x86_64), completely drop the CROSS flags
# and let OpenBLAS natively auto-detect the CPU architecture.
PACKAGECONFIG:class-native = "dynarch cblas"
EXTRA_OEMAKE:class-native = "\
    CC='${CC}' \
    FC='${FC}' \
    PREFIX='${exec_prefix}' \
    NO_STATIC=1 \
    NO_UTEST=1 \
    NO_TEST=1 \
    NOFORTRAN=1 \
    ${PACKAGECONFIG_CONFARGS} \
"
# -----------------------------------------------------------------
# Build & Install
# -----------------------------------------------------------------
do_compile () {
    bbnote "Building OpenBLAS libraries (bypassing broken utest/test directories)..."
    # 1. Build the base BLAS libraries (Parallel safe internally)
    oe_runmake ${EXTRA_OEMAKE} libs

    # 2. Build the LAPACK math libraries and append to the archive
    oe_runmake ${EXTRA_OEMAKE} netlib

    # 3. Link everything into the final .so shared library
    oe_runmake ${EXTRA_OEMAKE} shared
}


do_install() {
    oe_runmake ${EXTRA_OEMAKE} DESTDIR='${D}' install

    # Clean up binaries we don't need
    rm -rf ${D}${bindir}

    # Symlink and soname patch the libraries
    if ls ${D}${libdir}/libopenblas*r*.so 1> /dev/null 2>&1; then
        cp -ar ${D}${libdir}/libopenblas*r*.so ${D}${libdir}/libblas.so.3
        patchelf --set-soname libblas.so.3 ${D}${libdir}/libblas.so.3
        ln -sf libblas.so.3 ${D}${libdir}/libblas.so
    else
        bbfatal "OpenBLAS dynamic library was not generated correctly!"
    fi
}

FILES:${PN} = "${libdir}/lib*.so.* ${libdir}/lib*r*.so"

# The dev package should ONLY contain the headers, pkgconfig, and the unversioned symlinks.
FILES:${PN}-dev = "${includedir} ${libdir}/libopenblas.so ${libdir}/libblas.so ${libdir}/cmake ${libdir}/pkgconfig"

# Optional: If Yocto still complains about the main package containing a .so file, bypass the dev-so check:
INSANE_SKIP:${PN} += "dev-so"
DEPENDS:remove:class-native = "libgfortran"
BBCLASSEXTEND = "native"
TOOLCHAIN = "gcc"
