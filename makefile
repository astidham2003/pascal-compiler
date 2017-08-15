# Define compiler and compiler flag variables.
JFLAGS = -g -d .\\classes
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

# CLASSES is a macro consisting of the name of each java source file.
CLASSES = \
	.\\wci\\backend\\Backend.java \
	.\\wci\\backend\\BackendFactory.java \
	.\\wci\\backend\\compiler\\CodeGenerator.java \
	.\\wci\\backend\\interpreter\\Executor.java \
	.\\wci\\frontend\\EofToken.java \
	.\\wci\\frontend\\FrontEndFactory.java \
	.\\wci\\frontend\\Parser.java \
	.\\wci\\frontend\\Scanner.java \
	.\\wci\\frontend\\Source.java \
	.\\wci\\frontend\\Token.java \
	.\\wci\\frontend\\TokenType.java \
	.\\wci\\frontend\\pascal\\PascalParserTD.java \
	.\\wci\\frontend\\pascal\\PascalScanner.java \
	.\\wci\\frontend\\pascal\\PascalToken.java \
	.\\wci\\frontend\\pascal\\PascalTokenType.java \
	.\\wci\\frontend\\pascal\\PascalErrorHandler.java \
	.\\wci\\frontend\\pascal\\PascalErrorCode.java \
	.\\wci\\frontend\\pascal\\tokens\\PascalErrorToken.java \
	.\\wci\\frontend\\pascal\\tokens\\PascalWordToken.java \
	.\\wci\\intermediate\\ICode.java \
	.\\wci\\intermediate\\SymTab.java \
	.\\wci\\message\\Message.java \
	.\\wci\\message\\MessageHandler.java \
	.\\wci\\message\\MessageListener.java \
	.\\wci\\message\\MessageProducer.java \
	.\\wci\\message\\MessageType.java \
	.\\Pascal.java


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
	-$(RM) .\\classes\\wci\\intermediate\\*.class
	-$(RM) .\\classes\\wci\\message\\*.class

