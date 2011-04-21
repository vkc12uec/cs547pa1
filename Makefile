all:
	rm index.log
	python example.py
	#java URLConnectionReader

r:
	python example.py

compile:
	javac URLConnectionReader.java
