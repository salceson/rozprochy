#!/usr/bin/env bash

mvn compile
java -cp target/classes/ -Djava.security.policy=client.policy pl.edu.agh.ki.dsrg.rmi.tictactoe.Server $@
