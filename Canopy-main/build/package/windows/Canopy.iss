;This file will be executed next to the application bundle image
;I.e. current directory will contain folder Canopy with application files
[Setup]
AppId={{fxApplication}}
AppName=Canopy
AppVersion=1.0
AppVerName=Canopy 1.0
AppPublisher=com.gratech
AppComments=CanopyV0.995
AppCopyright=Copyright (C) 2020
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\Canopy
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=com.gratech
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=Canopy_Installer
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=Canopy\Canopy.ico
UninstallDisplayIcon={app}\Canopy.ico
UninstallDisplayName=Canopy
WizardImageStretch=No
WizardSmallImageFile=Canopy-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "Canopy\Canopy.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "Canopy\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Canopy"; Filename: "{app}\Canopy.exe"; IconFilename: "{app}\Canopy.ico"; Check: returnTrue()
Name: "{commondesktop}\Canopy"; Filename: "{app}\Canopy.exe";  IconFilename: "{app}\Canopy.ico"; Check: returnFalse()


[Run]
Filename: "{app}\Canopy.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\Canopy.exe"; Description: "{cm:LaunchProgram,Canopy}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\Canopy.exe"; Parameters: "-install -svcName ""Canopy"" -svcDesc ""Canopy"" -mainExe ""Canopy.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\Canopy.exe "; Parameters: "-uninstall -svcName Canopy -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
