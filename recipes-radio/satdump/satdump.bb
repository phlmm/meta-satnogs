SUMMARY = "A generic satellite data processing software"
DESCRIPTION = "SatDump is a generic satellite data processing software used \
to demodulate and decode data from various weather and earth observation satellites."
HOMEPAGE = "https://github.com/SatDump/SatDump"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

# Fetch the latest source from the main branch
SRC_URI = "git://github.com/SatDump/SatDump.git;protocol=https;branch=master"

# Use the latest commit (or pin a specific commit hash for stability)
SRCREV = "${AUTOREV}"

# SatDump uses CMake as its build system
inherit cmake pkgconfig

# -------------------------------------------------------------------------
# Dependencies
# -------------------------------------------------------------------------
# SatDump requires several DSP, math, and image processing libraries.
# We disable the GUI (GLFW) and OpenCL by default to keep the embedded build lightweight.

DEPENDS = " \
    fftw \
    volk \
    libpng \
    tiff \
    zstd \
    nng \
    curl \
    sqlite3 \
    nlohmann-json \
    jemalloc \
    opencl-headers \
    opencl-icd-loader \
"

# Optional SDR drivers if you intend to run SatDump in "Live" mode directly on the board
# DEPENDS += " rtl-sdr libhackrf soapy-sdr"
DEPENDS += " soapysdr"

# -------------------------------------------------------------------------
# CMake Configuration
# -------------------------------------------------------------------------
# By default, we disable the desktop GUI, OpenCL (unless your board supports it natively),
# and build the CLI-only version, which is ideal for embedded systems.

EXTRA_OECMAKE = " \
    -DBUILD_GUI=OFF \
    -DBUILD_OPENCL=ON \
    -DBUILD_ZSTD=ON \
    -DUSE_SYSTEM_JSON=ON \
    -DPLUGIN_SOAPY_SDR_SUPPORT=ON \
"

# -------------------------------------------------------------------------
# Packaging
# -------------------------------------------------------------------------
# Ensure all pipelines, resources, and compiled binaries are packaged correctly
# Tell Yocto that unversioned .so files are NOT just dev symlinks
FILES_SOLIBSDEV = ""

# Explicitly package the core library into the main package
FILES:${PN} += " \
    ${datadir}/satdump/* \
    ${libdir}/libsatdump_core.so \
"

# SatDump can take a while to compile; ensure parallel make is enabled
PARALLEL_MAKE = "-j 4"

do_configure:prepend() {
    # SatDump aggressively adds -march=native which completely breaks
    # Yocto's ARM cross-compilation. We recursively strip it out.
    find ${S} -type f -name "CMakeLists.txt" -exec sed -i 's/-march=native//g' {} +
    find ${S} -type f -name "*.cmake" -exec sed -i 's/-march=native//g' {} +
}
