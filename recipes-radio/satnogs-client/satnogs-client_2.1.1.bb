SUMMARY = "SatNOGS Client"
HOMEPAGE = "https://gitlab.com/librespacefoundation/satnogs/satnogs-client"
LICENSE = "AGPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

# Add the local configuration and service files to the URI
SRC_URI = "gitsm://gitlab.com/librespacefoundation/satnogs/satnogs-client.git;protocol=https;branch=master \
"

SRC_URI += " \
    file://satnogs-client \
    file://satnogs-client.service \
    file://satnogs-rigctld.service \
    file://satnogs-rotctld.service \
    file://99-xtrx.rules \
"

PV = "2.1.1+git"
SRCREV = "2.1.1"

# Add systemd and useradd to manage the daemon and permissions automatically
inherit setuptools3 systemd useradd

DEPENDS += "python3-versioneer-native"

PACKAGES += "${PN}-rigctld ${PN}-rotctld"
# -----------------------------------------------------------------
# Runtime Dependencies
# -----------------------------------------------------------------
# -----------------------------------------------------------------
# Runtime Dependencies (Mapped strictly from requirements.txt)
# -----------------------------------------------------------------
RDEPENDS:${PN} += "\
    python3-apscheduler \
    python3-certifi \
    python3-charset-normalizer \
    python3-contourpy \
    python3-cycler \
    python3-decorator \
    python3-pyephem \
    python3-fonttools \
    python3-h5py \
    python3-idna \
    python3-kiwisolver \
    python3-matplotlib \
    python3-numpy \
    python3-packaging \
    python3-pillow \
    python3-pyparsing \
    python3-dateutil \
    python3-python-dotenv \
    python3-pytz \
    python3-requests \
    python3-sentry-sdk \
    python3-six \
    python3-tzlocal \
    python3-urllib3 \
    python3-validators \
    \
    python3-core \
    python3-datetime \
    python3-io \
    python3-json \
    python3-logging \
    python3-netclient \
    python3-shell \
    python3-unittest \
    python3-pkg-resources \
    \
    hamlib \
    python3-hamlib \
    satnogs-flowgraphs \
    soapysdr \
"

# -----------------------------------------------------------------
# User Creation (Replicating Ansible)
# -----------------------------------------------------------------
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r satnogs"
# Creates the 'satnogs' user and adds them to dialout, audio, video, and plugdev
# This is critical so the client can access the USB SDR hardware without being root.
USERADD_PARAM:${PN} = "-r -g satnogs -G dialout,audio,video,plugdev -d /var/lib/satnogs -s /bin/false satnogs"

# -----------------------------------------------------------------
# Systemd Configuration
# -----------------------------------------------------------------
SYSTEMD_PACKAGES = "${PN} ${PN}-rigctld ${PN}-rotctld"
# Register all three services to be enabled on boot

SYSTEMD_SERVICE:${PN} = " \
    satnogs-client.service \
"

SYSTEMD_AUTO_ENABLE = "enable"

SYSTEMD_SERVICE:${PN}-rigctld = " \
    satnogs-rigctld.service \
"
SYSTEMD_SERVICE:${PN}-rotctld = " \
    satnogs-rotctld.service \
"

# -----------------------------------------------------------------
# Installation
# -----------------------------------------------------------------
do_install:append() {
    install -d ${D}${systemd_system_unitdir}

    # Install all three systemd services
    install -m 0644 ${UNPACKDIR}/satnogs-client.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${UNPACKDIR}/satnogs-rigctld.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${UNPACKDIR}/satnogs-rotctld.service ${D}${systemd_system_unitdir}/

    # Install the master environment configuration file
    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/satnogs-client ${D}${sysconfdir}/default/satnogs-client

    # Install udev rules
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-xtrx.rules ${D}${sysconfdir}/udev/rules.d/

    # Create the persistent data directories
    install -d ${D}${localstatedir}/lib/satnogs/data
    install -d ${D}${localstatedir}/lib/satnogs/active_data

    # Explicitly change ownership to the satnogs user
    chown -R satnogs:satnogs ${D}${localstatedir}/lib/satnogs
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/satnogs-client.service \
    ${systemd_system_unitdir}/satnogs-rotctld.service \
    ${sysconfdir}/default/satnogs-client \
    ${localstatedir}/lib/satnogs \
    ${localstatedir}/lib/satnogs/data \
    ${localstatedir}/lib/satnogs/active_data \
    ${sysconfdir}/udev/rules.d/99-xtrx.rules \
"
FILES:${PN}-rigctld += " \
    ${systemd_system_unitdir}/satnogs-rigctld.service \
"
FILES:${PN}-rotctld += " \
    ${systemd_system_unitdir}/satnogs-rotctld.service \
"
