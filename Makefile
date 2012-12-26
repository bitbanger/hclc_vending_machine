#space-delimited lists of executable classes and libraries to ship
EXEC=Test
LIBS=sqlite-jdbc-3.7.2.jar

#command aliases
JAR=jar
JAVA=java
JAVAC=javac
JAVADOC=javadoc

#directory tree
BINDIR=bin
DISTDIR=dist
DOCDIR=doc
LIBDIR=lib
SRCDIR=src

devel: classes documents launchers

classes:
	- mkdir ${BINDIR}
	${JAVAC} -d ${BINDIR} -cp ${SRCDIR} ${SRCDIR}/*.java

distribution: classes
	- mkdir ${DISTDIR}
	$(foreach library,${LIBS},ln ${LIBDIR}/${library} ${DISTDIR})
	echo Class-Path: ${LIBS} > Manifest
	$(foreach class,${EXEC},${JAR} cfem ${DISTDIR}/${class}.jar ${class} Manifest -C ${BINDIR} .)

documents:
	- mkdir ${DOCDIR}
	${JAVADOC} -d ${DOCDIR} ${SRCDIR}/*.java

launchers:
	echo "export CLASSPATH=${BINDIR}$(foreach library,${LIBS},:${LIBDIR}/${library})" > vars.include
	$(foreach class,${EXEC},echo -e "#!/bin/sh\n. ./vars.include\n${JAVA} ${class}" > run-${class}.sh ; chmod +x run-${class}.sh)

clean: cleanbin cleandoc
	- rm run-*.sh
	- rm *.include
	- rm Manifest

distclean: clean cleandist

cleanbin:
	- rm -r ${BINDIR}

cleandist:
	- rm -r ${DISTDIR}

cleandoc:
	- rm -r ${DOCDIR}
