#space-delimited lists of executable classes and libraries to ship
EXEC=Test
LIBS=sqlite-jdbc-3.7.2.jar

#command aliases
JAR=jar
JAVAC=javac
JAVADOC=javadoc

#directory tree
BINDIR=bin
DISTDIR=dist
DOCDIR=doc
LIBDIR=lib
SRCDIR=src

devel: classes documents

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

clean: cleanbin cleandoc
	- rm Manifest

distclean: clean cleandist

cleanbin:
	- rm -r ${BINDIR}

cleandist:
	- rm -r ${DISTDIR}

cleandoc:
	- rm -r ${DOCDIR}
