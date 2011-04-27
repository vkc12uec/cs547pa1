all:
	rm index.log
	python example.py
	#java URLConnectionReader

r:
	python example.py

n:
	python ngramQuery.py

compile:
	javac URLConnectionReader.java
