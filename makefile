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

CLASSES = \
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
	-$(RM) .\\classes\\wci\\frontend\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\tokens\\*.class
	-$(RM) .\\classes\\wci\\frontend\\pascal\\parsers\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\symtabimpl\\*.class
	-$(RM) .\\classes\\wci\\intermediate\\icodeimpl\\*.class
	-$(RM) .\\classes\\wci\\message\\*.class
	-$(RM) .\\classes\\wci\\util\\*.class


# This target entry uses Suffix Replacement within a macro:
# $(name:string1=string2)
#	In the words in the macro named 'name' replace 'string1' with 'string2'.
# Below we are replacing the suffix .java of all words in the macro CLASSES
# with the .class suffix.
classes: $(CLASSES:.java=.class)

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

# Compile with intermediate code flag.
intermediate_xml:
	java -cp classes Pascal compile -i assignments.txt

small_intermediate_xml:
	java -cp classes Pascal compile -i small_assignments.txt
