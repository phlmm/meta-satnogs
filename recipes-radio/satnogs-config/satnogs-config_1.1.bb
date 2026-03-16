# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)
LICENSE = "AGPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

SRC_URI = "gitsm://gitlab.com/librespacefoundation/satnogs/satnogs-config.git;protocol=https;branch=master"

PV = "1.1+git"
SRCREV = "1.1"

inherit setuptools3

DEPENDS += "python3-versioneer-native"
RDEPENDS:${PN} += "python3-core python3-datetime python3-json python3-logging python3-pyyaml python3-pythondialog python3-psutil"
