#!/bin/bash

#Load properties file
. ../user.properties

export HTTP_PROXY=http://proxy.wdf.sap.corp:8080
/usr/bin/cf login -u $user -p $password -o sap_demo -a http://api.cf1.hybris.com --skip-ssl-validation
/usr/bin/cf push -f dev-manifest.yml