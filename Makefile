compile :
	javac -cp lib/postgresql-42.7.11.jar -d bin src/db/*.java src/model/*.java src/mapper/*.java src/driver/*.java

run :
	java -cp "bin;lib/postgresql-42.7.11.jar" driver.Main
