# PasswordGenerator
#
# Author: Selvamohan Neethiraj
# (c) 2004-2016, InfoTekies Corporation
#

This is just to generate more compilcated password for website that has more conditions (such as One Numeric, One Upercase, ....)

#
# Maven to run the Password Generator
#
mvn exec:java -Dexec.mainClass=com.infotekies.passwdgen.PasswordGenerator

#
# Run directly using Java JRE
#

java -cp "./target/*:."  com.infotekies.passwdgen.PasswordGenerator
