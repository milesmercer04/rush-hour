#!/bin/bash

JAR_DIR="/usr/share/rush-hour/rush-hour-solver"
JAR_FILE=$(find "$JAR_DIR" -maxdepth 1 -name "*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo "Error: No .jar file found in $SCRIPT_DIR"
  exit 1
fi

# Launch the JAR
exec java -jar "$JAR_FILE"
