This is the default directory to put your custom fonts.

If you want to specific another directory, add a JVM
parameter XDO_FONT_DIR in the batch file / shell script 
to start XDOClient or XDOAction.

e.g. for Windows platform:
     -Dfile.encoding=UTF8 ^
     -DXDO_FONT_DIR=C:\Windows\Fonts ^    <--- add this line
     symbolthree.oracle.xdo.XDOClient %*
 