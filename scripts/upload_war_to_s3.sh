#!/usr/bin/env bash

releaseVersion=$1

FILE=./oauth-server/target/OAuthApi/oauth-server-"${releaseVersion}".zip

if [ -f "$FILE" ]; then
    echo "$FILE exists."
	if [[ $BUILD == "dev" ]]; then
		echo "Uploading to S3 dev"
	  aws s3 cp $FILE s3://oauth-service/dev/
	  echo "Uploaded to s3 dev"
	 fi;
	if [[ $BUILD == "prod" ]]; then
	  echo "Uploading to S3 prod"
	  aws s3 cp $FILE s3://oauth-service/prod/
    echo "Uploaded to s3 prod"
  fi;
else 
    echo "$FILE does not exist."
fi
