echo off
:begin
cls
echo Choose one of the following user :
echo DEV 
echo  1) VEEL2334
echo  2) TEMO0001
echo  8) TOVI7266
echo PROD
echo  3) TOVI4040
echo  4) TOVI4047
echo  5) TOVI4257
echo  6) TOVI5675
echo  7) TOVI7292
set /p choice=Number : 
goto :%choice%

:1
set user=VEEL2334
set pwd=123456
set serial=00106696651858843258
goto :getotp

:2
set user=TEMO0001
set pwd=123456
set serial=VDP1915591
goto :getotp

:3
set user=TOVI4040
set pwd=123456
set serial=VDP1915590
goto :getotp

:4
set user=TOVI4047
set pwd=123456
set serial=VDP1915591
goto :getotp

:5
set user=TOVI4257
set pwd=123456
set serial=VDP1915592
goto :getotp

:6
set user=TOVI5675
set pwd=123456
set serial=VDP1915593
goto :getotp

:7
set user=TOVI7292
set pwd=123456
set serial=VDP1915594
goto :getotp

:8
set user=TOVI7266
set pwd=123456
set serial=VDP4005662
goto :getotp


:getotp
echo;
echo USER   : %user%
echo PWD    : %pwd%
echo SERIAL : %serial%
echo ====================
"C:/Program Files/Java/jre1.8.0_181/bin/java" -Djava.library.path=./ -jar ./OTPGenerator.jar ./2013-08-08_5_it-dev.dpx CA9603E6AEA5D05E0FA214B0F29385F7 MONITORING %serial%
echo;
echo ====================
echo;
pause
goto :begin