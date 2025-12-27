# MEGSI-AUTENTICADOR

## PREREQUISITES

### NETWORK

```
docker network create megsi-net
```

### CONFIG SERVER

```
docker run -d \
  --name megsi-config-server \
  --network megsi-net \
  -p 8888:8888 \
  -v /srv/configuration-server-fs:/srv/configuration-server-fs \
  rafaelrpsantos/megsi-config-server-fs:latest
```

### KAFKA

```
docker run -d \
  --name kafka \
  --network megsi-net \
  -p 9092:9092 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_CLUSTER_ID=5L6g3nShT-eMCtK--X86sw \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@kafka:9093 \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,INTERNAL://:29092,CONTROLLER://:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092,INTERNAL://kafka:29092 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,INTERNAL:PLAINTEXT,CONTROLLER:PLAINTEXT \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL \
  apache/kafka:latest
```

### MARIADB

```
docker run -d --name mariadb --network megsi-net -p 3306:3306 -e MARIADB_ROOT_PASSWORD=uminho mariadb:latest

docker exec -it mariadb mariadb -u root -p

CREATE DATABASE MEGSI;
```

### MIGRATIONS

```
mvn flyway:migrate \
-Dflyway.url=jdbc:mariadb://localhost:3306/MEGSI \
-Dflyway.user=root \
-Dflyway.password=uminho
```

### MEGSI-AUTENTICADOR

```
docker build -t "rafaelrpsantos/megsi-autenticator:latest" .
```

```
docker run -d \
  --name megsi-autenticador \
  --network megsi-net \
  -p 8080:8080 \
  -e SPRING_CLOUD_CONFIG_URI=http://megsi-config-server:8888 \
  -e MARIADB_SERVER_URI=mariadb \
  -e MYSQL_ROOT_PASSWORD=uminho \
  -e KAFKA_SERVER_URI=kafka:29092 \
  rafaelrpsantos/megsi-autenticator:latest
```