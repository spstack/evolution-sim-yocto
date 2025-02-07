SUMMARY = "evolution-sim"
HOMEPAGE = "https://github.com/spstack/evolution-sim"
LICENSE = "CLOSED"

inherit cargo_bin

# Enable network for the compile task allowing cargo to download dependencies
do_compile[network] = "1"
do_configure[network] = "1"

SRC_URI = "git://github.com/spstack/evolution-sim.git;protocol=https;branch=main"
SRCREV="d797c9e613798620ed67e2b8891074c0f96afcb1"

SRC_URI += "file://evsim.sh"

# Don't checksum the entire source tree. This changes every time due to git lfs (I think)
BB_STRICT_CHECKSUM = "0"

S = "${WORKDIR}/git"
LIC_FILES_CHKSUM = ""


# This is necessary to get submodules to work (*rolls eyes*)
do_configure:prepend() {
  cd ${WORKDIR}/git
  git submodule update --init --recursive
}


do_install:append() {
  install -d ${D}${sysconfdir}/init.d

  # Create directories:
  #   ${D}${sysconfdir}/init.d - will hold the scripts
  #   ${D}${sysconfdir}/rcS.d  - will contain a link to the script that runs at startup
  #   ${D}${sysconfdir}/rc5.d  - will contain a link to the script that runs at runlevel=5
  #   ${D}${sbindir}           - scripts called by the above
  #
  # ${D} is effectively the root directory of the target system.
  # ${D}${sysconfdir} is where system configuration files are to be stored (e.g. /etc).
  # ${D}${sbindir} is where executable files are to be stored (e.g. /sbin).
  #
  install -d ${D}${sysconfdir}/init.d
  install -d ${D}${sysconfdir}/rcS.d
  install -d ${D}${sysconfdir}/rc1.d
  install -d ${D}${sysconfdir}/rc2.d
  install -d ${D}${sysconfdir}/rc3.d
  install -d ${D}${sysconfdir}/rc4.d
  install -d ${D}${sysconfdir}/rc5.d

  #
  # Install files in to the image
  #
  # The files fetched via SRC_URI (above) will be in ${WORKDIR}.
  #
  install -m 0755 ${WORKDIR}/evsim.sh         ${D}${sysconfdir}/init.d/

  #
  # Create symbolic links from the runlevel directories to the script files.
  # Links of the form S... and K... mean the script when be called when
  # entering / exiting the runlevel designated by the containing directory.
  # For example:
  #   rc5.d/S90run-script will be called (with %1='start') when entering runlevel 5.
  #   rc5.d/K90run-script will be called (with %1='stop') when exiting runlevel 5.
  #
  ln -sf ../init.d/evsim.sh      ${D}${sysconfdir}/rc1.d/S11evsim.sh
  ln -sf ../init.d/evsim.sh      ${D}${sysconfdir}/rc2.d/S11evsim.sh
  ln -sf ../init.d/evsim.sh      ${D}${sysconfdir}/rc3.d/S11evsim.sh
  ln -sf ../init.d/evsim.sh      ${D}${sysconfdir}/rc4.d/S11evsim.sh
  ln -sf ../init.d/evsim.sh      ${D}${sysconfdir}/rc5.d/S11evsim.sh 
}