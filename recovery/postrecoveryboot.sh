#!/sbin/sh

sleep 5

# clear boot-recovery flag
dd if=/dev/zero of=/dev/block/platform/omap/omap_hsmmc.1/by-name/misc seek=0 count=13 bs=1

#mount external_sd

EXTSD_PART=/dev/block/platform/omap/omap_hsmmc.0/by-num/p1

busybox mount -t vfat -o noatime $EXTSD_PART /external_sd

## Restart adbd, just in case
killall -9 adbd

