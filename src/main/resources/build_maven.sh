#!/usr/bin/env bash
cd $1 && mvn archetype:generate \
		-DinteractiveMode=false \
		-DarchetypeArtifactId=maven-archetype-quickstart \
		-DgroupId=$2 \
		-DartifactId=$3 \
		-Dversion=$4