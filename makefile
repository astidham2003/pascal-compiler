# Define compiler and compiler flag variables.
JFLAGS = -Xlint:unchecked -g -d .\\classes
JC = javac

# Target entry for creating .class files from .java files.
# Uses suffix rule syntax:
#	DSTS:
#		rule
# 'TS' is the suffix of the target file.  'DS' is the suffix of the
# dependency file.
# 'rule' is the rule for building the target. '$*' is a built-in macro
# that gets the basename of the current target.
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

SOURCES = \
	.\\Pascal.java \
	.\\wci\\backend\\*.java \
	.\\wci\\backend\\compiler\\*.java \
	.\\wci\\backend\\interpreter\\*.java \
	.\\wci\\backend\\interpreter\\executors\\*.java \
	.\\wci\\frontend\\*.java \
	.\\wci\\frontend\\pascal\\*.java \
	.\\wci\\frontend\\pascal\\parsers\\*.java \
	.\\wci\\frontend\pascal\\tokens\\*.java \
	.\\wci\\intermediate\\*.java \
	.\\wci\\intermediate\\icodeimpl\\*.java \
	.\\wci\\intermediate\\symtabimpl\\*.java \
	.\\wci\\intermediate\\typeimpl\\*.java \
	.\\wci\\message\\*.java \
	.\\wci\\util\\*.java

CLASSES = \
	.\\wci\\intermediate\\TypeSpec.java \
	.\\wci\\intermediate\\typeimpl\\TypeFormImpl.java \
	.\\wci\\intermediate\\typeimpl\\TypeKeyImpl.java \
	.\\Pascal.java

# RM is a predefined macro in make (RM = rm -f)
# XXX Can't seem to get recursive deletion of all files in
# XXX all subdirectories working right now.  Will have to come
# XXX to this and try again.
clean:
	-$(RM) .\\classes\\*.class
	-$(RM) .\\classes\\wci\\backend\\*.class
	-$(RM) .\\classes\\wci\\backend\\compiler\\*.class
	-$(RM) .\\classes\\wci\\backend\\interpreter\\*.class
	-$(RM) .\\classes\\wci\\backend\\interpreter\\executors\\*.class
	-$(RM) .\\classes\\wci\\frontend\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\tokens\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\parsers\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\icodeimpl\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\symtabimpl\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\typeimpl\\*.class
	-$(RM) .\\classes\\wci\\message\\*.class
	-$(RM) .\\classes\\wci\\util\\*.class

# This target entry uses Suffix Replacement within a macro:
# $(name:string1=string2)
#	In the words in the macro named 'name' replace 'string1' with 'string2'.
# Below we are replacing the suffix .java of all words in the macro CLASSES
# with the .class suffix.
classes: $(CLASSES:.java=.class)

# Generate documents with JavaDocs
docs:
	javadoc \
	$(SOURCES) \
	-overview .\\overview.html \
	-d .\docs \

scanner_test:
	java -cp classes Pascal compile scannertest.txt

# Interpret hello.pas
hello_i:
	java -cp classes Pascal execute hello.pas

# Compile hello.pas
hello_c:
	java -cp classes Pascal compile hello.pas

# Compile with cross reference flag
newton_xref:
	java -cp classes Pascal compile -x newton.pas

# Compile with i flag to produce the intermediate code xml.
intermediate_xml:
	java -cp classes Pascal compile -i assignments.txt

# Compile with i flag to produce the intermediate code xml.
# This version has errors.
intermediate_error_xml:
	java -cp classes Pascal compile -i assignments_error.txt

# Execute assignments.txt with the interpreter.
assignments_execute:
	java -cp classes Pascal execute assignments.txt

# Compile repeat.txt.  Book also called this loop.txt.  But repeat is
# more clearer.
repeat_xml:
	java -cp classes Pascal compile -i repeat.txt

# Compile repeat_error.txt
repeat_error_xml:
	java -cp classes Pascal compile -i repeat_error.txt

# Compile while.txt
while_xml:
	java -cp classes Pascal compile -i while.txt

# Compile while_errors.txt
while_error_xml:
	java -cp classes Pascal compile -i while_errors.txt

# Compile for.txt
for_xml:
	java -cp classes Pascal compile -i for.txt

# Compile if_error.txt
for_error_xml:
	java -cp classes Pascal compile -i for_error.txt

# Compile if.txt
if_xml:
	java -cp classes Pascal compile -i if.txt

# Compile if_error.txt
if_error_xml:
	java -cp classes Pascal compile -i if_error.txt

# Compile case.txt
case_xml:
	java -cp classes Pascal compile -i case.txt

# Compile case_error.txt
case_error_xml:
	java -cp classes Pascal compile -i case_error.txt

# Execute loops.txt.
loops_execute:
	java -cp classes Pascal execute loops.txt

# Compile if.txt
if_execute:
	java -cp classes Pascal execute if.txt

# Compile if.txt
case_execute:
	java -cp classes Pascal execute case.txt

