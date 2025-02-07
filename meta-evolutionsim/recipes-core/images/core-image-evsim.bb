#image that includes the evolution sim program and auto runs it
include recipes-core/images/core-image-base.bb

# Only compatible with the raspberry pi at this point
COMPATIBLE_MACHINE = "^rpi$"

# Manually add which additional recipes we should use
IMAGE_INSTALL:append = " evolution-sim"

LICENSE_FLAGS_ACCEPTED = "synaptics-killswitch"