SUMMARY = "rpi-rgb-led-matrix"
HOMEPAGE = "https://github.com/hzeller/rpi-rgb-led-matrix"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/hzeller/rpi-rgb-led-matrix.git;protocol=https;branch=master"
SRCREV="552ef44edf80fd56955c6e87ac322eda44b5f07c"

# Don't checksum the entire source tree. This changes every time due to git lfs (I think)
# BB_STRICT_CHECKSUM = "0"

# Set source
S = "${WORKDIR}/git"

do_compile() {
    cd lib
    make
}

do_install() {
	install -d ${D}${libdir}
	install -m 0755 librgbmatrix.a ${D}${libdir}
}
