#!/bin/bash
############################################
# XDO Client Batch Process Shell Script
#############################################
java -cp \
./bin:\
./xdoclient-@build.version@.jar \
-Dfile.encoding=UTF8 \
symbolthree.oracle.xdo.XDOAction $*