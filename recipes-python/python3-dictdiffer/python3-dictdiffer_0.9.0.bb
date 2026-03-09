# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Dictdiffer is a library that helps you to diff and patch dictionaries."
HOMEPAGE = "https://github.com/inveniosoftware/dictdiffer"
# NOTE: License in setup.py/PKGINFO is: UNKNOWN
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5319f20678c5972ead78588780fbd47a"

SRC_URI[sha256sum] = "17bacf5fbfe613ccf1b6d512bd766e6b21fb798822a133aa86098b8ac9997578"

inherit pypi setuptools3

# The following configs & dependencies are from setuptools extras_require.
# These dependencies are optional, hence can be controlled via PACKAGECONFIG.
# The upstream names may not correspond exactly to bitbake package names.
# The configs are might not correct, since PACKAGECONFIG does not support expressions as may used in requires.txt - they are just replaced by text.
#
# Uncomment this line to enable all the optional features.
#PACKAGECONFIG ?= "all allpython-version-smaller-3-dot-7 allpython-version-smaller-3-dot-8 allpython-version-smaller-3-dot-9 allpython-version-smaller--equals-3-dot-5 allpython-version-bigger-3-dot-5 allpython-version-bigger--equals-3-dot-9 docs numpypython-version-smaller-3-dot-7 numpypython-version-smaller-3-dot-8 numpypython-version-smaller-3-dot-9 numpypython-version-bigger--equals-3-dot-9 tests testspython-version-smaller--equals-3-dot-5 testspython-version-bigger-3-dot-5"
PACKAGECONFIG[all] = ",,,python3-sphinx python3-sphinx python3-check-manifest python3-check-manifest python3-mock python3-mock python3-pytest-cov python3-pytest-cov python3-pytest-isort python3-pytest-isort python3-sphinx python3-sphinx python3-sphinx-rtd-theme python3-sphinx-rtd-theme python3-tox python3-tox"
PACKAGECONFIG[allpython-version-smaller-3-dot-7] = ",,,python3-numpy python3-numpy"
PACKAGECONFIG[allpython-version-smaller-3-dot-8] = ",,,python3-numpy python3-numpy"
PACKAGECONFIG[allpython-version-smaller-3-dot-9] = ",,,python3-numpy python3-numpy"
PACKAGECONFIG[allpython-version-smaller--equals-3-dot-5] = ",,,python3-pytest python3-pytest python3-pytest-pycodestyle python3-pytest-pycodestyle python3-pytest-pydocstyle python3-pytest-pydocstyle"
PACKAGECONFIG[allpython-version-bigger-3-dot-5] = ",,,python3-pytest python3-pytest python3-pytest-pycodestyle python3-pytest-pycodestyle python3-pytest-pydocstyle python3-pytest-pydocstyle"
PACKAGECONFIG[allpython-version-bigger--equals-3-dot-9] = ",,,python3-numpy python3-numpy"
PACKAGECONFIG[docs] = ",,,python3-sphinx python3-sphinx-rtd-theme"
PACKAGECONFIG[numpypython-version-smaller-3-dot-7] = ",,,python3-numpy"
PACKAGECONFIG[numpypython-version-smaller-3-dot-8] = ",,,python3-numpy"
PACKAGECONFIG[numpypython-version-smaller-3-dot-9] = ",,,python3-numpy"
PACKAGECONFIG[numpypython-version-bigger--equals-3-dot-9] = ",,,python3-numpy"
PACKAGECONFIG[tests] = ",,,python3-check-manifest python3-mock python3-pytest-cov python3-pytest-isort python3-sphinx python3-tox"
PACKAGECONFIG[testspython-version-smaller--equals-3-dot-5] = ",,,python3-pytest python3-pytest-pycodestyle python3-pytest-pydocstyle"
PACKAGECONFIG[testspython-version-bigger-3-dot-5] = ",,,python3-pytest python3-pytest-pycodestyle python3-pytest-pydocstyle"


# WARNING: the following rdepends are determined through basic analysis of the
# python sources, and might not be 100% accurate.
RDEPENDS:${PN} += "python3-core python3-numpy python3-pprint"

# WARNING: We were unable to map the following python package/module
# dependencies to the bitbake packages which include them:
#    collections.abc

PYPI_PACKAGE = "dictdiffer"
