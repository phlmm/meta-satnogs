SUMMARY = "Hamlib radio control library"
LICENSE = "GPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE;md5=13f98e5a534d38ded794f1bc9a121832"

SRC_URI = "git://github.com/Hamlib/Hamlib.git;protocol=https;branch=Hamlib-4.7"
PV = "4.7.0+git"
SRCREV = "4.7.0"

DEPENDS += "libusb1 tcl swig-native libxml2 readline python3 libxcrypt"

# Inherit python3targetconfig so the target build gets the correct Python headers
inherit pkgconfig autotools python3native python3targetconfig

PACKAGES += "python3-${PN}"

EXTRA_OECONF += "--with-python-binding"

# -----------------------------------------------------------------
# Target Python Linking
# -----------------------------------------------------------------
export PYTHON_CPPFLAGS = "-I${STAGING_INCDIR}/${PYTHON_DIR}"
export PYTHON_LIBS = "-L${STAGING_LIBDIR} -l${PYTHON_DIR}"

# -----------------------------------------------------------------
# Native Python Linking (Overrides for the host build)
# -----------------------------------------------------------------
export PYTHON_CPPFLAGS:class-native = "-I${STAGING_INCDIR_NATIVE}/${PYTHON_DIR}"
export PYTHON_LIBS:class-native = "-L${STAGING_LIBDIR_NATIVE} -l${PYTHON_DIR}"

FILES:python3-${PN} += "${PYTHON_SITEPACKAGES_DIR}"

BBCLASSEXTEND += "native"
