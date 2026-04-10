FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://61-satnogs-data-mount.rules \
            file://var-lib-satnogs-data.mount \
"

SYSTEMD_SERVICE:${PN} += "var-lib-satnogs-data.mount"

do_install:append () {
    install -D -m 0644  ${S}/var-lib-satnogs-data.mount ${D}${sysconfdir}/systemd/system/var-lib-satnogs-data.mount
    install -D -m 0644  ${S}/61-satnogs-data-mount.rules ${D}${sysconfdir}/udev/rules.d/61-satnogs-data-mount.rules
}

FILES:${PN} += "${sysconfdir}/systemd/*"
FILES:${PN} += "${sysconfdir}/udev/*"
