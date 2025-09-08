# PasswordGenerator
#
# Author: Selvamohan Neethiraj
# (c) 2004-2020, InfoTekies Corporation
#

This is just to generate more compilcated password for website that has more conditions (such as One Numeric, One Upercase, ....)

#
# Maven to run the Password Generator
#
mvn exec:java -Dexec.mainClass=com.infotekies.passwdgen.PasswordGenerator

#
# Run directly using Java JRE
#

mvn package
java -cp "./src/main/resources:./target/*"  com.infotekies.passwdgen.PasswordGenerator

#
# Run as Docker Service
#

./runDockerService

#
# To stop the docker service for PasswordGenerator,
#

./stopDockerService

#
# Services Exposed
#
Generate Password:	/v1/{profileName}/passwords
# => profileName could be any string that has been defined in the src/main/resources/passwordgen.properties 

