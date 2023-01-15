#!/bin/bash
############################################
# XDO Client Shell Script
#############################################
java -cp \
./bin:\
./xdoclient-@build.version@.jar \
-Dfile.encoding=UTF8 \
symbolthree.oracle.xdo.XDOClient $*