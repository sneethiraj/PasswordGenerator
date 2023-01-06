#!/bin/bash
curl -s "http://localhost:8080/v1/infotekies/passwords" | jq '.'
