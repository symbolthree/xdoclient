; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{3E406E9C-7FCA-4F88-93E9-4147A8C9E2C3}
AppName=XDO Client @build.version@
AppVerName=XDO Client @build.version@ build @build.number@
AppPublisher=symbolthree.com
AppPublisherURL=symbolthree.com
AppSupportURL=www.symbolthree.com
AppUpdatesURL=www.symbolthree.com
AppCopyright=Copyright (C) 2023 symbolthree.com
DefaultDirName={pf}\symbolthree/XDOClient
DefaultGroupName=symbolthree\XDOClient
AllowNoIcons=false
LicenseFile=build_template\LICENSE.txt
OutputBaseFilename=xdoclient_@build.version@
Compression=lzma
SolidCompression=true
SetupLogging=false
OutputDir=setup_file
WizardImageFile=build_template\installer.bmp
;SetupIconFile=build_template\XDOClient.ico
WizardImageStretch=false
WizardImageBackColor=clWhite
AppMutex=XDOClient
;UninstallDisplayIcon={app}\XDOClient.ICO
PrivilegesRequired=none
VersionInfoTextVersion=@build.version@ build @build.number@
VersionInfoVersion=@build.version@.@build.number@.0

[Languages]
Name: english; MessagesFile: compiler:Default.isl
;Name: chineseTrad; MessagesFile: compiler:Languages\ChineseTrad.isl
;Name: chineseSimp; MessagesFile: compiler:Languages\chineseSimp.isl
;Name: Japanese; MessagesFile: compiler:Languages\Japanese.isl

[Tasks]
Name: desktopicon; Description: {cm:CreateDesktopIcon}; GroupDescription: {cm:AdditionalIcons}

[Files]
;Source: release\XDOClient_@build.version@\lib\*; DestDir: {app}\lib; Flags: recursesubdirs createallsubdirs
;Source: release\XDOClient_@build.version@\examples\*; DestDir: {app}\examples; Flags: recursesubdirs createallsubdirs
Source: release\XDOClient_@build.version@\fonts\*; DestDir: {app}\fonts; Flags: recursesubdirs createallsubdirs
Source: release\XDOClient_@build.version@\output\*; DestDir: {app}\output; Flags: recursesubdirs createallsubdirs
Source: release\XDOClient_@build.version@\tmp\*; DestDir: {app}\temp; Flags: recursesubdirs createallsubdirs
Source: release\XDOClient_@build.version@\bin\*; DestDir: {app}\bin; Flags: recursesubdirs createallsubdirs
Source: release\XDOClient_@build.version@\XDOClient.bat; DestDir: {app}
Source: release\XDOClient_@build.version@\XDOAction.bat; DestDir: {app}
;Source: release\XDOClient_@build.version@\XDOClient-@build.version@-build@build.number@_src.zip; DestDir: {app}
Source: release\XDOClient_@build.version@\XDOClient.exe; DestDir: {app}
Source: release\XDOClient_@build.version@\XDOClient.ICO; DestDir: {app}
Source: release\XDOClient_@build.version@\GPL.txt; DestDir: {app}
Source: release\XDOClient_@build.version@\README; DestDir: {app}
Source: release\XDOClient_@build.version@\LICENSE.txt; DestDir: {app}
Source: release\XDOClient_@build.version@\LICENSE_3RD_PARTY.txt; DestDir: {app}
Source: release\XDOClient_@build.version@\splash.gif; DestDir: {app}
Source: release\XDOClient_@build.version@\XDO.cfg; DestDir: {app}

[Registry]
Root: HKCR; Subkey: ".xdoc"; ValueType: string; ValueName: ""; ValueData: "XDOClient"; Flags: uninsdeletevalue
Root: HKCR; Subkey: "XDOClient"; ValueType: string; ValueName: ""; ValueData: "XDOClient Config File"; Flags: uninsdeletekey 
Root: HKCR; Subkey: "XDOClient\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\XDOClient.exe,0" 
Root: HKCR; Subkey: "XDOClient\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\XDOClient.exe"" ""%1"""

[Icons]
Name: {group}\{cm:UninstallProgram,XDOClient}; Filename: {uninstallexe}; Tasks: ; Languages: 
Name: {group}\XDOClient @build.version@; Filename: {app}\XDOClient.EXE; WorkingDir: {app}; IconIndex: 0
Name: {commondesktop}\XDOClient @build.version@; Filename: {app}\XDOClient.EXE; Tasks: desktopicon; WorkingDir: {app}; IconIndex: 0

[Run]
Filename: {app}\XDOClient.EXE; Description: {cm:LaunchProgram,SYMPLiK XDOClient}; Flags: nowait postinstall skipifsilent