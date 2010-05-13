%define name    fachwerk3d
%define version 0.4.1 
%define release 1 

%define fachwerkjar  fachwerk3d041.jar
%define fachwerkzip  fachwerk3d041.zip
%define fachwerkman  fachwerk3d.1.gz

%define fullname     %{name}-%{version}-%{release}

Name:           %{name} 
Summary:        Calculates spatial strut-and-tie models used by structural engineers.
Summary(de):    Programm für Bauingenieure, um räumliche Fachwerkmodelle zu berechnen.
Version:        %{version} 
Release:        %{release} 
Source0:        http://download.berlios.de/fachwerk3d/%{fachwerkzip}
URL:            http://fachwerk.berlios.de/index3D_en.html

Group:          Applications/Engineering
Buildarch:      noarch
BuildRoot:      %{_tmppath}/%{fullname}-buildroot
Distribution:   none
Vendor:         none
Packager:       A. Vontobel <qwert2003@users.berlios.de>
License:        GPL
Requires:       java
Prefix:         /usr

%description
Fachwerk3D calculates spatial strut-and-tie models used by structural 
engineers for analysing and designing reinforced concrete structures. 
The program only uses equilibrium conditions, thus it does 
not assume elastic material behaviour.
%description -l de
Das Programm Fachwerk3D soll Bauingenieuren im Betonbau die 
Anwendung von Spannungsfeldern und Fachwerkmodellen erleichtern. 
Es ist ein einfach zu bedienendes Fachwerkprogramm, welches - im Unterschied zu 
den gängigen Programmen - einzig die Gleichgewichtsbedingungen anwendet.

# 
%prep 
cd $RPM_BUILD_DIR
rm -rf fachwerk3d
unzip $RPM_SOURCE_DIR/%{fachwerkzip} -d $RPM_BUILD_DIR
cd fachwerk3d

chmod -R a+rX,g-w,o-w .


%build 
cd $RPM_BUILD_DIR/fachwerk3d
echo '#! /bin/bash' > fachwerk3d
echo 'java -jar $(dirname $0)/../share/'%{name}/%{fachwerkjar}' "$@"' >> fachwerk3d

%install
cd $RPM_BUILD_DIR
mkdir -p $RPM_BUILD_ROOT%{_datadir}/%{name}
mkdir -p $RPM_BUILD_ROOT%{_bindir}
mkdir -p $RPM_BUILD_ROOT%{_mandir}/man1
mkdir -p $RPM_BUILD_ROOT%{_docdir}/%{name}
cp -p fachwerk3d/fachwerk3d*.jar $RPM_BUILD_ROOT%{_datadir}/%{name}
cp -p fachwerk3d/fachwerk3d $RPM_BUILD_ROOT%{_bindir}
cp -pr fachwerk3d/lib $RPM_BUILD_ROOT%{_datadir}/%{name}
cp -pr fachwerk3d/INFO $RPM_BUILD_ROOT%{_docdir}/%{name}
mv $RPM_BUILD_ROOT%{_docdir}/%{name}/INFO/manpage/%{fachwerkman} $RPM_BUILD_ROOT%{_mandir}/man1
cp -pr fachwerk3d/src $RPM_BUILD_ROOT%{_docdir}/%{name}
cp -pr fachwerk3d/examples $RPM_BUILD_ROOT%{_docdir}/%{name}

mkdir -p $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/scalable/apps
mkdir -p $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/scalable/mimetypes
mkdir -p $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/48x48/apps
mkdir -p $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/48x48/mimetypes
mkdir -p $RPM_BUILD_ROOT%{_datadir}/applications
mkdir -p $RPM_BUILD_ROOT%{_datadir}/mime/packages/
cp -p fachwerk3d/INFO/fachwerk.svg $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/scalable/apps/%{name}.svg
cp -p fachwerk3d/INFO/fachwerk.svg $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/scalable/mimetypes/application-x-%{name}.svg
cp -p fachwerk3d/INFO/fachwerk.png $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/48x48/apps/%{name}.png
cp -p fachwerk3d/INFO/fachwerk.png $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/48x48/mimetypes/application-x-%{name}.png
cp -p fachwerk3d/src/%{name}.desktop $RPM_BUILD_ROOT%{_datadir}/applications
cp -p fachwerk3d/src/%{name}.xml $RPM_BUILD_ROOT%{_datadir}/mime/packages/

rm -rf $RPM_BUILD_DIR/fachwerk3d


%clean 
rm -rf $RPM_BUILD_ROOT 

%files 
%defattr(-,root,root,0755)
%{_datadir}/%{name}
%doc %{_docdir}/%{name}/INFO
%doc %{_docdir}/%{name}/src
%doc %{_docdir}/%{name}/examples
%{_bindir}/fachwerk3d
%{_mandir}/man1/%{fachwerkman}
%{_datadir}/icons/hicolor/scalable/mimetypes/application-x-%{name}.svg
%{_datadir}/icons/hicolor/scalable/apps/%{name}.svg
%{_datadir}/icons/hicolor/48x48/apps/%{name}.png
%{_datadir}/icons/hicolor/48x48/mimetypes/application-x-%{name}.png
%{_datadir}/applications/%{name}.desktop
%{_datadir}/mime/packages/%{name}.xml

%post
/usr/bin/update-mime-database %{_datadir}/mime >/dev/null

%postun
/usr/bin/update-mime-database %{_datadir}/mime >/dev/null

%changelog 
* Thu May 13 2010 Adrian Vontobel <qwert2003@users.berlios.de> 0.4.1-1
- Initial rpm release.

