# pascal-compiler
Following along with the 'Writing Compilers and Interpreters 3rd Edition' book by Ronald Mak.

# Build instructions
Tested using java 1.8.
Uses a makefile to build.
On Windows, requires gnuWin32 tools in path.
From the command line in the directory with the makefile: 
* 'make classes' compiles the *.java files into *.class files and places them in the classes directory.
* 'make clean' will remove all of the .class files from the classes directory.

# Run instructions
On the command line:
* 'java -cp classes Pascal compile hello.pas'
* 'java -cp classes Pascal execute hello.pas'

The first command uses the wci\backend\compiler\CodeGenerator class and the second uses wci\backend\interpreter\Executor class.
