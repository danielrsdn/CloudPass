# CloudPass
Backend service that manage authentication, and storage, upload and retrieval of photos on AWS Cloud. Hosted on AWS Lambda as container image. 

# Deploy locally via Docker for testing

```bash
$ ./gradlew copyRuntimeDependencies
$ ./gradlew build
$ docker build -t cloud-pass . 
$ docker run -p 9000:8080 cloud-pass .
$ curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
```
