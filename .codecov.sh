#!/bin/bash
# Executes ./gradlew connectedCheck until it works or max attempt threshold is met.
success=0
outputCode=-1
maxAttempts=20
while [ $outputCode -ne $success ]
do
		./gradlew connectedCheck
		outputCode=$?

		let maxAttempts--
		if [ $maxAttempts -eq 0 ]; then
				exit 1
		fi
done
