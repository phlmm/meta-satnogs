SUMMARY = "SatNOGS Client Addons (kng/satnogs-client-docker)"
DESCRIPTION = "Native installation of community addons, scripts, and utilities \
for SatNOGS Client, originally packaged as a Docker addon image."
HOMEPAGE = "https://github.com/kng/satnogs-client-docker"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4ae09d45eac4aa08d013b5f2e01c67f6"

SRC_URI = "gitsm://github.com/kng/satnogs-client-docker.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

# Define where the addons live in the cloned repo
ADDON_DIR = "${S}/addons"

inherit python3native
DEPENDS += "satnogs-client"
inherit useradd
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-r satnogs"
# --- Package Dependencies ---
# These map the contents of packages.builder, packages.client, and packages.rust
# from Debian names to standard Yocto OpenEmbedded layer names.
# You MUST ensure meta-oe, meta-python, and potentially meta-sdr are in your bblayers.conf
RDEPENDS:${PN} = " \
    bash \
    python3-core \
    python3-json \
    python3-logging \
    jq \
    coreutils \
    netcat \
    socat \
    curl \
    wget \
    zlib \
    xz \
    satdump \
    gr-satellites \
    direwolf \
    soapysdr \
    hamlib \
"

# If your board requires specific Python libraries used in the scripts (like imagedecode.py)
RDEPENDS:${PN} += " \
    python3-numpy \
    python3-requests \
    python3-construct \
    python3-construct-classes \
"

do_install() {
    # 1. Install Scripts
    # The addons/scripts directory contains wrappers like satnogs-pre, satdump.sh, etc.
    install -d ${D}${bindir}

    # Iterate through all scripts and install them
    for script in ${ADDON_DIR}/scripts/*; do
        if [ -f "$script" ]; then
            install -m 0755 "$script" ${D}${bindir}/
        fi
    done


    # 3. Install Configuration Files
    # The Dockerfile places sat.cfg in the .gr_satellites config folder
    install -d ${D}${localstatedir}/lib/satnogs/.gr_satellites
    install -m 0644 ${ADDON_DIR}/sat.cfg ${D}${localstatedir}/lib/satnogs/.gr_satellites/

    # Direwolf config
    install -d ${D}${sysconfdir}
    if [ -f "${ADDON_DIR}/direwolf.conf" ]; then
        install -m 0644 ${ADDON_DIR}/direwolf.conf ${D}${sysconfdir}/
    fi

    # Fix ownership of the var/lib/satnogs configuration files
    chown -R satnogs:satnogs ${D}${localstatedir}/lib/satnogs/.gr_satellites
}

# Ensure the python site-packages and var paths are packaged
FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/satellites/satyaml/* \
    ${localstatedir}/lib/satnogs/.gr_satellites/sat.cfg \
"

# Prevent QA errors regarding absolute paths in scripts if any exist
INSANE_SKIP:${PN} += "file-rdeps"
