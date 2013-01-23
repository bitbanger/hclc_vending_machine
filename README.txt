High Cohesion, Low Coupling build system
----------------------------------------

Adding main classes
===================
Some automatic functions of the build system rely on an up-to-date list of
runnable classes (i.e. those that have main(String[]) methods).  If you create
a new such class, update the space-delimited EXEC variable in the Makefile.

Normal use
==========
Compile everything: $ make
Run a main class: $ ./run-ClassName.sh
Cleaning up (if desired): $ make clean

Unit testing
============
Compile the tests: $ make tests
Run the test handle: $ ./testsuite.sh
Cleaning up (if desired): $ make clean

Shipping a binary release
=========================
Build the release: $ make distribution
Run a main class: $ cd dist ; java -jar ClassName.jar
Cleaning up (if desired): $ make distclean

Directory structure
===================
bin #should not get committed
dist #should not get committed
doc #should not get committed
junit_test
lib
license
src
tbin #should not get committed

NOTE: Please preserve the cases of these folder names!
WARNING: The bin, dist, doc, and tbin areas are volatile, and are often wiped
by the build system without any warning!  Avoid placing important
files---including databases---in these locations.
