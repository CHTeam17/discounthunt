#!/bin/bash

#Load properties file
. ../user-team-sap-east.properties

export HTTP_PROXY=http://proxy.wdf.sap.corp:8080
/usr/bin/cf login -u $user -p $password -o sap_demo -a http://api.cf3.hybris.com --skip-ssl-validation
/usr/bin/cf push -f dev-manifest.yml
