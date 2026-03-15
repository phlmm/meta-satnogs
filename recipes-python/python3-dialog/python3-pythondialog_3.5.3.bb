SUMMARY = "A Python wrapper for the UNIX dialog utility"
HOMEPAGE = "https://pythondialog.sourceforge.io/"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

inherit pypi setuptools3

SRC_URI[sha256sum] = "b2a34a8af0a6625ccbdf45cd343b854fc6c1a85231dadc80b8805db836756323"

RDEPENDS:${PN} += " \
    dialog \
    python3-core \
"
