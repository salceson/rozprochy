#!/bin/bash

# CHANGE THIS:
export REPO=~/.m2/repository

keytool -genkey -keystore c.jks -alias rsakey
keytool -certreq -alias rsakey -keystore c.jks -file c.csr
mvn package
java -cp "target/classes:$REPO/com/zeroc/ice/3.5.0/ice-3.5.0.jar:$REPO/com/zeroc/icestorm/3.5.0/icestorm-3.5.0.jar" pl.edu.agh.ki.dsrg.sr.bankmanagement.certsigner.CertSigner --Ice.Config=server.config
keytool -import -alias cacert -trustcacerts -keystore c.jks -file sr2015ca.crt
keytool -import -alias rsakey -keystore c.jks -file c.crt
