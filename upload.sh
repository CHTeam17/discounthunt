#!/bin/bash

#Load properties file
. ../user-team-sap-east.properties

if [ $# == 0 ]
  then echo "Please provide path to manifest file"
else
  export HTTPS_PROXY=http://proxy.wdf.sap.corp:8080
  cf login -u $user -p $password -o $organisation -a $target --skip-ssl-validation
  cf push -f $1
fi