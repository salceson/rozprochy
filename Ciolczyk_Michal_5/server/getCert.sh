#!/bin/bash

# Usage: ./getCert.sh <*.jks file> <*.csr file name> <*.crt file name>

# CHANGE THIS:
export REPO=~/.m2/repository

keytool -genkey -keystore $1 -alias rsakey
keytool -certreq -alias rsakey -keystore $1 -file $2
java -cp "target/classes:$REPO/com/zeroc/ice/3.5.0/ice-3.5.0.jar:$REPO/com/zeroc/icestorm/3.5.0/icestorm-3.5.0.jar" \
pl.edu.agh.ki.dsrg.sr.bankmanagement.certsigner.CertSigner --Ice.Config=cert.config $2 $3
keytool -import -alias cacert -trustcacerts -keystore $1 -file sr2015ca.crt
keytool -import -alias rsakey -keystore $1 -file $3
