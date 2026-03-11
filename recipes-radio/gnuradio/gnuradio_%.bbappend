FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
PACKAGECONFIG += "ctrlport zeromq orc logging"
PACKAGECONFIG:class-native = "grc ctrlport zeromq orc logging"
BBCLASSEXTEND += "native"
# install this file so we can use it in other builds
do_install:append () {
	install -D ${S}/grc/scripts/grcc ${D}${bindir}/grcc
}
DEPENDS += "python3-pygccxml libunwind python3-pyyaml python3-jsonschema"
# 1. Provide the actual Python libraries grcc needs to parse flowgraphs
DEPENDS:append:class-native = " \
    python3-pyyaml-native \
    python3-jsonschema-native \
    python3-pygccxml-native \
"

# 2. Hack the GNU Radio CMake script to bypass the GUI requirements for native builds
do_configure:prepend:class-native() {
    bbnote "Amputating gr-qtgui and hardware drivers from native build..."
    sed -i '/add_subdirectory(gr-qtgui)/d' ${S}/CMakeLists.txt
    sed -i '/add_subdirectory(gr-uhd)/d' ${S}/CMakeLists.txt
    sed -i '/add_subdirectory(gr-iio)/d' ${S}/CMakeLists.txt

    bbnote "Spoofing GUI dependencies for native GRC..."
    # We let the macros run to avoid syntax errors, but we inject 'set(... TRUE)'
    # immediately before the GR_REGISTER_COMPONENT check to trick CMake.
    sed -i -e '/GR_REGISTER_COMPONENT(/i set(PYGI_FOUND TRUE)' \
           -e '/GR_REGISTER_COMPONENT(/i set(GTK_GI_FOUND TRUE)' \
           -e '/GR_REGISTER_COMPONENT(/i set(CAIRO_GI_FOUND TRUE)' \
           -e '/GR_REGISTER_COMPONENT(/i set(PANGOCAIRO_GI_FOUND TRUE)' \
           ${S}/grc/CMakeLists.txt
}
EXTRA_OECMAKE:append:class-native = " -DENABLE_GR_QTGUI=OFF"
