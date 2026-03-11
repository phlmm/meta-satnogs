# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Python package for easy C++ declarations navigation."
HOMEPAGE = "https://github.com/CastXML/pygccxml"
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "BSL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=e4224ccaecb14d942c71d31bef20d78c"

SRC_URI = "git://github.com/CastXML/pygccxml;protocol=https;branch=master"

# Modify these as desired
PV = "3.0.2+git"
SRCREV = "62f600c98ec6a25fd3d264774c6fc811ec3c46e4"

inherit python_setuptools_build_meta


# WARNING: We were unable to map the following python package/module
# runtime dependencies to the bitbake packages which include them:
#    importlib-metadata
BBCLASSEXTEND += "native"
