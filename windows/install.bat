set svcName=MyService
set svcFullName="My Super Service"
set svcDesc="My super special service"
set svcJAR=de.taimos.myservice.jar
set svcStarter=de.taimos.myservice.Starter

@rem DO NOT EDIT BELOW

%svcName%.exe delete
%svcName%.exe install --DisplayName=%svcFullName% --Description=%svcDesc% --Install="%~dp0%svcName%.exe" --JvmOptions=-DdevelopmentMode=false --Classpath %svcJAR% --Jvm=auto --StartMode=jvm --StopMode=jvm --StartClass=%svcStarter% --StopClass=de.taimos.daemon.DaemonStopper
