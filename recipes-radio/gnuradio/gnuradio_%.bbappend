FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
BBCLASSEXTEND += "native"

DEPENDS += "python3-pygccxml python3-pygccxml-native libunwind python3-pyyaml python3-jsonschema"


DEPENDS:append:class-native = " \
    python3-pyyaml-native \
    python3-jsonschema-native \
    python3-pygccxml-native \
"

PACKAGECONFIG += "ctrlport zeromq orc logging"
PACKAGECONFIG:class-native = "grc ctrlport zeromq orc logging"


do_install:append:class-native () {
    # Notice the change from ${S} to ${B} here!
    install -D ${B}/grcc ${D}${bindir}/grcc

    # (And if you added the shebang fix from earlier, keep it here)
    if [ -d "${D}${datadir}/gnuradio/examples" ]; then
        find ${D}${datadir}/gnuradio/examples -type f -name "*.py" -exec sed -i '1s|^#!.*|#!/usr/bin/env python3|' {} +
    fi
}

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

INSANE_SKIP:${PN}-native += "shebang-size"
EXTRA_OECMAKE:append:class-native = " \
    -DENABLE_GR_QTGUI=OFF \
    -DENABLE_GR_QTGUI_OPENGL=OFF \
    -DCMAKE_DISABLE_FIND_PACKAGE_Qt5=TRUE \
    -DCMAKE_DISABLE_FIND_PACKAGE_Qt6=TRUE \
"
