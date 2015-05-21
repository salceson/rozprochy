#!/bin/bash

export ICE_INCLUDE_PATH="/usr/share/Ice-3.5.1/slice/Ice"
export OPTIONS="-I ${ICE_INCLUDE_PATH}"

slice2cpp --output-dir ../ Bank.ice ${OPTIONS}
slice2cpp --output-dir ../ CertSigner.ice ${OPTIONS}
slice2cpp --output-dir ../ FinancialNews.ice ${OPTIONS}
