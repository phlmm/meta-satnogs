SUMMARY = "Parse your python dataclasses from/to binary data using construct"
HOMEPAGE = "https://github.com/matejcik/construct-classes"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f9b44edfc9bae6b5e8b8f6c69c8f4c5"

inherit pypi setuptools3

# The name of the package on PyPI
PYPI_PACKAGE = "construct-classes"

# Grab these SHA256 sums from the PyPI download page for the specific version
SRC_URI[md5sum] = "b4f854e9940445a56eb66d4090d28626"
SRC_URI[sha256sum] = "72ac1abbae5bddb4918688713f991f5a7fb6c9b593646a82f4bf3ac53de7eeb5"

# Define the runtime dependencies this package needs to work
RDEPENDS:${PN} += " \
    python3-core \
    python3-construct \
    python3-types-dataclasses \
"
