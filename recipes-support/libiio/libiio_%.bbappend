FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

BBCLASSEXTEND += "native"
# The IIO Daemon and threading are completely unnecessary for a native build host
EXTRA_OECMAKE:append:class-native = " -DWITH_IIOD=OFF -DNO_THREADS=ON"
