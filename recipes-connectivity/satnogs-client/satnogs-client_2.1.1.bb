# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   .scm-ci-helpers/LICENSE
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "AGPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

SRC_URI = "gitsm://gitlab.com/librespacefoundation/satnogs/satnogs-client.git;protocol=https;branch=master"

# Modify these as desired
PV = "2.1.1+git"
SRCREV = "2.1.1"

inherit setuptools3
DEPENDS += "python3-versioneer-native"
# WARNING: the following rdepends are determined through basic analysis of the
# python sources, and might not be 100% accurate.
RDEPENDS:${PN} += "python3-core python3-datetime python3-dateutil python3-io python3-json python3-logging python3-matplotlib python3-netclient python3-numpy python3-pygps python3-pytz python3-requests python3-shell python3-unittest python3-python-dotenv python3-sentry-sdk python3-apscheduler python3-pyephem python3-h5py python3-validators python3-hamlib"

# WARNING: We were unable to map the following python package/module
# dependencies to the bitbake packages which include them:
#    Hamlib
#    apscheduler.executors.pool
#    apscheduler.jobstores.memory
#    apscheduler.schedulers.background
#    distutils.util
#    dotenv
#    ephem
#    h5py
#    imagedecode
#    sentry_sdk
#    urlparse
#    validators.url
