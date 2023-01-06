#!/bin/bash
ProfileName="infotekies"
if [ $# -gt 0 ]
then
	ProfileName="${1}"
fi
curl -s "http://localhost:8080/v1/${ProfileName}/passwords" | jq '.'
