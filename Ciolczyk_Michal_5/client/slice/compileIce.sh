#!/bin/bash

export ICE_INCLUDE_PATH="/usr/share/Ice-3.5.1/slice/Ice"
export OPTIONS="-I ${ICE_INCLUDE_PATH}"

slice2cpp --output-dir ../generated/ Bank.ice ${OPTIONS}
slice2cpp --output-dir ../generated/ CertSigner.ice ${OPTIONS}
slice2cpp --output-dir ../generated/ FinancialNews.ice ${OPTIONS}
