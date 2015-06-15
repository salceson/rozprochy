#!/usr/bin/env bash

java -cp target/classes/ -Djava.security.policy=client.policy pl.edu.agh.ki.dsrg.rmi.tictactoe.Client $@