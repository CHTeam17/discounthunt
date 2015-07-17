#!/bin/bash
if [ -z "$TRAVIS_BRANCH" ]
then 
  echo "Only to be executed by Travis CI"
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]
then
  echo "Pull request detected, won't deploy"
else
  export HTTPS_PROXY=http://proxy.wdf.sap.corp:8080
  ./cf login -u $CF_USER -p $CF_PASSWORD -o $CF_ORG -a https://api.cf3.hybris.com --skip-ssl-validation
  ./cf set-env $CF_APP YAAS_CLIENT_ID $YAAS_CLIENT_ID
  echo "Setting env variable 'YAAS_CLIENT_SECRET' to '[secure]' for app $CF_APP in org $CF_ORG as $CF_USER..."
  ./cf set-env $CF_APP YAAS_CLIENT_SECRET $YAAS_CLIENT_SECRET > /dev/null # do this silently, as travis logs can be read by everyone!
  ./cf set-env $CF_APP YAAS_CLIENT $YAAS_CLIENT
  ./cf push -f dev-manifest.yml
fi
