JFLAGS = -g -encoding ISO8859_1  # -encoding in case latin characters are used
JC = javac
JVM= java  # Added by Agust�n Gonz�lez
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = T1Stage2.java Broker.java Topic.java \
		Publisher.java Subscriber.java \
	    Recorder.java Broker.java Component.java Follower.java

MAIN = T1Stage2

default: classes

classes: $(CLASSES:.java=.class)

run: 
	$(JVM) $(MAIN) config.txt

clean:
	$(RM) *.class