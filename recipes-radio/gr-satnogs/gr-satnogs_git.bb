SUMMARY = "gr-satnogs is a GNU Radio out-of-tree module provided by SATNOGS"
HOMEPAGE = "https://gitlab.com/librespacefoundation/satnogs/gr-satnogs.git"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit gnuradio-oot qemu python3native

# -----------------------------------------------------------------
# DEPENDENCIES
# -----------------------------------------------------------------
# By using standard DEPENDS, BBCLASSEXTEND will automatically require
# itpp-native, hamlib-native, openblas-native, etc., for the host build.
DEPENDS += "itpp hamlib libvorbis libpng openblas nlohmann-json pngpp python3-pybind11 castxml python3-pygccxml-native"

# qemu-native is strictly for cross-compiling the target ARM build
DEPENDS:append:class-target = " qemu-native python3-pygccxml-native"

RDEPENDS:${PN} = "gnuradio python3-click python3-construct python3-requests orc python3-hamlib"

PV = "3.1.0.1+git${SRCPV}"
SRC_URI = "gitsm://gitlab.com/librespacefoundation/satnogs/gr-satnogs.git;branch=master;protocol=https"
SRCREV = "4defcfc445c1f94450c48ad348689e7986ebe6f7"

PACKAGECONFIG ??= "python"
PACKAGECONFIG[python] = "-DENABLE_PYTHON=ON,-DENABLE_PYTHON=OFF,python3,"

# -----------------------------------------------------------------
# TARGET CLASS OVERRIDES: QEMU Wrapper for libfec
# -----------------------------------------------------------------
# This only runs for the target device build. The native build will
# naturally run the gen_ccsds binary natively without emulation.
do_configure:prepend:class-target() {
    bbnote "Generating QEMU wrapper for host-execution of target binaries..."

    qemu_binary="${@qemu_target_binary(d)}"

    cat << EOF > ${WORKDIR}/qemuwrapper
#!/bin/sh
$qemu_binary -L ${RECIPE_SYSROOT} "\$@"
EOF
    chmod +x ${WORKDIR}/qemuwrapper

    bbnote "Forcing broken CMake custom commands to use the emulator..."

    sed -i 's|COMMAND ${CMAKE_CURRENT_BINARY_DIR}/gen_ccsds >|COMMAND ${CMAKE_CROSSCOMPILING_EMULATOR} ${CMAKE_CURRENT_BINARY_DIR}/gen_ccsds >|g' ${S}/lib/libfec/CMakeLists.txt
    sed -i 's|COMMAND ${CMAKE_CURRENT_BINARY_DIR}/gen_ccsds_tal >|COMMAND ${CMAKE_CROSSCOMPILING_EMULATOR} ${CMAKE_CURRENT_BINARY_DIR}/gen_ccsds_tal >|g' ${S}/lib/libfec/CMakeLists.txt
}

# -----------------------------------------------------------------
# NATIVE CLASS OVERRIDES: Fix CMake Math Library Search
# -----------------------------------------------------------------
do_configure:prepend:class-native() {
    bbnote "Bypassing broken find_library(M_LIB) in native build..."

    # 1. Delete the find_library call entirely so it stops searching
    sed -i 's/find_library(M_LIB m REQUIRED)//g' ${S}/lib/libfec/CMakeLists.txt

    # 2. Hardcode 'm' directly into target_link_libraries so CMake handles it natively
    sed -i 's/\${M_LIB}/m/g' ${S}/lib/libfec/CMakeLists.txt
}
# Scope the emulator variable to the target build only
#EXTRA_OECMAKE:append:class-native = " -DM_LIB=m"
#EXTRA_OECMAKE:append:class-native = " -DM_LIB=-lm"
EXTRA_OECMAKE:append:class-target = " -DCMAKE_CROSSCOMPILING_EMULATOR=${WORKDIR}/qemuwrapper"

do_install:append() {
    bbnote "Manually installing missing code and fec sub-libraries skipped by CMake..."

    # Ensure the target library directory exists
    install -d ${D}${libdir}

    # Use cp -P to preserve the symlink structure (so, so.3.1.0git, so.v3.1.0.1)
    # Standard 'install' command dereferences symlinks, which inflates package size
    cp -P ${B}/lib/libgnuradio-satnogs.so* ${D}${libdir}/
    cp -P ${B}/lib/code/libgnuradio-satnogs-code.so* ${D}${libdir}/
    cp -P ${B}/lib/libfec/libgnuradio-satnogs-fec.so* ${D}${libdir}/
}
# -----------------------------------------------------------------
# NATIVE CLASS EXTENSION
# -----------------------------------------------------------------
BBCLASSEXTEND = "native"
# Ensure Python variables are available
do_install:append() {
    # ... (Keep your previous cp -P commands for the .so files here) ...

    bbnote "Spoofing missing pkg-config and CMake target files for downstream recipes..."

    # 1. Generate the missing pkg-config file
    install -d ${D}${libdir}/pkgconfig
    cat << EOF > ${D}${libdir}/pkgconfig/gnuradio-satnogs.pc
prefix=${prefix}
exec_prefix=${exec_prefix}
libdir=${libdir}
includedir=${includedir}

Name: gnuradio-satnogs
Description: GNU Radio blocks for SATNOGS
Version: 3.1.0
Libs: -L\${libdir} -lgnuradio-satnogs -lgnuradio-satnogs-code -lgnuradio-satnogs-fec
Cflags: -I\${includedir}
EOF

    # 2. Remove the broken 'include' line from the Config file
    sed -i '/gnuradio-satnogsTarget.cmake/d' ${D}${libdir}/cmake/gnuradio-satnogs/gnuradio-satnogsConfig.cmake

    # 3. Inject the target definition directly so satnogs-flowgraphs can link to it
    cat << 'EOF' >> ${D}${libdir}/cmake/gnuradio-satnogs/gnuradio-satnogsConfig.cmake

# --- YOCTO INJECTED TARGET ---
if(NOT TARGET gnuradio::gnuradio-satnogs)
    add_library(gnuradio::gnuradio-satnogs SHARED IMPORTED)
    set_target_properties(gnuradio::gnuradio-satnogs PROPERTIES
        IMPORTED_LOCATION "${CMAKE_CURRENT_LIST_DIR}/../../libgnuradio-satnogs.so"
    )
endif()
EOF
}
