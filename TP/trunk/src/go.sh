#/bin/bash
SIZE=$1
WHITE="java gtp/Motor"
BLACK="java proxy/Proxy 6000"
TWOGTP="gogui-twogtp -black \"$BLACK\" -white \"$WHITE\" -games 1 \
-size "$SIZE" -alternate -sgffile gnugo5"
java -jar /home/pablo/workspace/goGui/lib/gogui.jar -size "$SIZE" -program "$TWOGTP" -computer-both -auto
