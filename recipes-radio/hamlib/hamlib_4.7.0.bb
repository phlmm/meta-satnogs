# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "GPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE;md5=13f98e5a534d38ded794f1bc9a121832"

SRC_URI = "git://github.com/Hamlib/Hamlib.git;protocol=https;branch=Hamlib-4.7"

# Modify these as desired
PV = "4.7.0+git"
SRCREV = "4.7.0"

# NOTE: the following prog dependencies are unknown, ignoring: pytest
# NOTE: unable to map the following pkg-config dependencies: usrp
#       (this is based on recipes that have previously been built and packaged)
# NOTE: the following library dependencies are unknown, ignoring: winmm gd ws2_32
#       (this is based on recipes that have previously been built and packaged)
DEPENDS += "libusb1 tcl swig-native libxml2 readline python3 libxcrypt"
PACKAGES += "python3-${PN}"
# NOTE: if this software is not capable of being built in a separate build directory
# from the source, you should replace autotools with autotools-brokensep in the
# inherit line
inherit pkgconfig autotools
inherit python3-dir python3native

EXTRA_OECONF += "\
    --with-python-binding \
    PYTHON=${PYTHON} \
    PYTHON_SITE_PKG=${STAGING_DIR}${PYTHON_SITEPACKAGES_DIR} \
    PYTHON_CPPFLAGS=-I${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI} \
    PYTHON_LIBS=-L${STAGING_LIBDIR}/lib \
    CFLAGS=-l${PYTHON_DIR} \
"

EXTRA_OECONF_native += "\
    --with-python-binding \
    PYTHON=${PYTHON} \
    PYTHON_SITE_PKG=${STAGING_DIR_NATIVE}${PYTHON_SITEPACKAGES_DIR} \
    PYTHON_CPPFLAGS=-I${STAGING_INCDIR_NATIVE}/${PYTHON_DIR}${PYTHON_ABI} \
    PYTHON_LIBS=-L${STAGING_LIBDIR_NATIVE}/lib \
    CFLAGS=-l${PYTHON_DIR} \
"

FILES:python3-${PN} += "${PYTHON_SITEPACKAGES_DIR}"
