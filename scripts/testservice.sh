#!/bin/bash
ProfileName="infotekies"
if [ $# -gt 0 ]
then
	ProfileName="${1}"
fi
echo "+ curl -s \"http://localhost:8080/v1/${ProfileName}/passwords\" | jq '.'"
curl -s "http://localhost:8080/v1/${ProfileName}/passwords" | jq '.'
echo "+ curl -s -v \"http://localhost:8080/v1/${ProfileName}/passwords404error\""
curl -s -v "http://localhost:8080/v1/${ProfileName}/passwords404error" 
