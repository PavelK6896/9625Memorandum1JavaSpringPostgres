#!/usr/bin/env bash

#mvn clean package

echo 'Copy files...'

scp -i "C:\pav-key-frankfurt.pem" target/9625Memorandum1JavaSpringPostgre-1.0-SNAPSHOT.jar ubuntu@3.121.40.255:/home/ubuntu/

echo 'Restart server...'

ssh -i "C:\pav-key-frankfurt.pem" ubuntu@3.121.40.255 << EOF
pgrep java | xargs kill -9
nohup java -jar 9625Memorandum1JavaSpringPostgre-1.0-SNAPSHOT.jar > log1.txt &
EOF

echo 'Bye'
