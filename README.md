### Example Commands for Java Client CLI
```bash
java -jar target/s3compatible-client-0.0.1-SNAPSHOT.jar create-bucket -n=teamname-examplebucket
java -jar target/s3compatible-client-0.0.1-SNAPSHOT.jar upload-file -b=teamname-examplebucket -f hello.txt -k=hello.txt
java -jar target/s3compatible-client-0.0.1-SNAPSHOT.jar download-file -b=teamname-bucket-todelete-2 -k=hello.txt -o=downloaded_hello.txt
```
