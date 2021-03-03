@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\java"



pushd %DIR% & %JAVA_EXEC% %CDS_JVM_OPTS% -Djdk.gtk.version=2 -p "%~dp0/../app" -m connnectfour/connnectfour.Main  %* & popd
