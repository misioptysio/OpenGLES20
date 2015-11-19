mkdir mipmap-hdpi
mkdir mipmap-mdpi
mkdir mipmap-xhdpi
mkdir mipmap-xxhdpi
mkdir mipmap-xxxhdpi
alchlong ---n -o -+ --+ -Xd192p icon.png mipmap-xxxhdpi\ic_launcher.png
alchlong ---n -o -+ --+ -Xd144p icon.png mipmap-xxhdpi\ic_launcher.png
alchlong ---n -o -+ --+ -Xd96p icon.png mipmap-xhdpi\ic_launcher.png
alchlong ---n -o -+ --+ -Xd72p icon.png mipmap-hdpi\ic_launcher.png
alchlong ---n -o -+ --+ -Xd48p icon.png mipmap-mdpi\ic_launcher.png

copy mipmap-hdpi\*.* ..\app\src\main\res\mipmap-hdpi
copy mipmap-mdpi\*.* ..\app\src\main\res\mipmap-mdpi
copy mipmap-xhdpi\*.* ..\app\src\main\res\mipmap-xhdpi
copy mipmap-xxhdpi\*.* ..\app\src\main\res\mipmap-xxdpi
copy mipmap-xxxhdpi\*.* ..\app\src\main\res\mipmap-xxxhdpi

copy texture.png ..\app\src\main\res\raw