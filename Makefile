#space-delimited lists of executable classes and libraries to ship
EXEC=Test
LIBS=sqlite-jdbc-3.7.2.jar
TESTCHAIN=org.junit.runner.JUnitCore
TESTLIBS=junit-4.11.jar

#command aliases
JAR=jar
JAVA=java
JAVAC=javac
JAVADOC=javadoc
SHELLPATH=/bin/sh

#directory tree
BINDIR=bin
DISTDIR=dist
DOCDIR=doc
LIBDIR=lib
LICDIR=license
SRCDIR=src
TBDIR=tbin
TESTDIR=junit_test

#launcher scripts
INCPOSTFIX=.include
POSTFIX=.sh
RUNPREFIX=run-
TESTSTEM=testsuite

#targets
devel: classes documents launchers

classes:
	- mkdir ${BINDIR}
	${JAVAC} -d ${BINDIR} -cp ${SRCDIR}$(subst $(empty) :$(empty),:,$(foreach library,${LIBS},:${LIBDIR}/${library})) ${SRCDIR}/*.java

tests: classes
	- mkdir ${TBDIR}
	${JAVAC} -d ${TBDIR} -cp ${TESTDIR}:${BINDIR}$(subst $(empty) :$(empty),:,$(foreach library,${TESTLIBS},:${LIBDIR}/${library})) ${TESTDIR}/*.java
	echo -e "#!${SHELLPATH}\ncd ${TBDIR}\n${JAVA} -cp ../${TBDIR}:../${BINDIR}$(subst $(empty) :$(empty),:,$(foreach library,${TESTLIBS},:../${LIBDIR}/${library})$(foreach library,${LIBS},:../${LIBDIR}/${library})) ${TESTCHAIN} $(patsubst ${TESTDIR}/%.java,%,$(wildcard $(shell grep -le '@Test' ${TESTDIR}/*.java)))" > ${TESTSTEM}${POSTFIX}
	chmod +x ${TESTSTEM}${POSTFIX}

distribution: classes
	- mkdir ${DISTDIR}
	$(foreach library,${LIBS},ln ${LIBDIR}/${library} ${DISTDIR};)
	$(foreach license,$(wildcard ${LICDIR}/*),ln ${license} ${DISTDIR};)
	echo Class-Path: ${LIBS} > Manifest
	$(foreach class,${EXEC},${JAR} cfem ${DISTDIR}/${class}.jar ${class} Manifest -C ${BINDIR} .;)

documents:
	- mkdir ${DOCDIR}
	${JAVADOC} -d ${DOCDIR} -classpath ${SRCDIR} ${SRCDIR}/*.java

launchers:
	echo "export CLASSPATH=${BINDIR}$(subst $(empty) :$(empty),:,$(foreach library,${LIBS},:${LIBDIR}/${library}))" > vars${INCPOSTFIX}
	$(foreach class,${EXEC},echo -e '#!${SHELLPATH}\n. ./vars.include\n${JAVA} ${class} "$$@"' > ${RUNPREFIX}${class}${POSTFIX} ; chmod +x ${RUNPREFIX}${class}${POSTFIX};)

clean: cleanbin cleandoc cleantbin
	- rm ${TESTSTEM}${POSTFIX}
	- rm Manifest

distclean: clean cleandist
	- rm ${RUNPREFIX}*${POSTFIX}
	- rm *${INCPOSTFIX}

cleanbin:
	- rm -r ${BINDIR}

cleandist:
	- rm -r ${DISTDIR}

cleandoc:
	- rm -r ${DOCDIR}

cleantbin:
	- rm -r ${TBDIR}
