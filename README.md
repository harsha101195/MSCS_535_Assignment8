# Safe File Read (Java)

This project demonstrates fail-safe file reading.

## Steps to Run

### 1. Create sample file
echo "fail-safe demo 1" > data/example.txt

### 2. Compile
javac -d out src/SafeFileReader.java src/Main.java

### 3. Run
java -cp out Main example.txt

## Common Test Scenarios

### 1. File does not exist
java -cp out Main no_such_file.txt

### 2. Permission denied
echo "secret" > data/secret.txt
chmod 000 data/secret.txt
java -cp out Main secret.txt
NOTE: restore permissions so you can delete/edit it later
chmod 644 data/secret.txt

