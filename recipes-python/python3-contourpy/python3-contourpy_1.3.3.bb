# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Python library for calculating contours of 2D quadrilateral grids"
HOMEPAGE = "https://github.com/contourpy/contourpy"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0186404b1452548f04e644440ce58e3c"

SRC_URI[sha256sum] = "083e12155b210502d0bca491432bb04d56dc3432f95a979b429f2848c3dbe880"

inherit pypi python_mesonpy

PYPI_PACKAGE = "contourpy"

# Include both the native build tools AND the target headers
DEPENDS = " \
    python3 \
    python3-pybind11 \
    python3-pybind11-native \
    pkgconfig-native \
    python3-meson-python-native \
"
RDEPENDS:${PN} = "python3-numpy"
