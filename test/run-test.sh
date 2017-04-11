#!/usr/bin/bash

cd system-test
mvn -Dmaven.repo.local=../local-repo test -Dtest=AccountIT
