#!/bin/bash

export ICE_INCLUDE_PATH="/usr/share/Ice-3.5.1/slice/Ice"
export OPTIONS="-I ${ICE_INCLUDE_PATH}"

slice2java --output-dir generated/ slice/Bank.ice ${OPTIONS}
