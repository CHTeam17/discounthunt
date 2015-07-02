#!/bin/bash
#Load properties file
if [ $# == 0 ]
  then echo "Please provide path to manifest file"
else
  export HTTPS_PROXY=http://proxy.wdf.sap.corp:8080
  ./cf login -u $CF_USER -p $CF_PASSWORD -o sap_cec_prod -a https://api.cf3.hybris.com --skip-ssl-validation
  ./cf push -f $1
fi