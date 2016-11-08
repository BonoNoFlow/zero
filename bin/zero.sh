#!/bin/bash
#
# this script only works when
# the following dir structure
# is followed:
# zero
# |- bin
# |   |- zero.sh
# |
# |- lib
#     |- zero.jar + dependencies
#
# you can symlink to this script 
# from every dir you want.
#

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do #find source until it is no symlink
	DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
	SOURCE="$(readlink "$SOURCE")"
	[[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$( cd  -P "$( dirname "$SOURCE" )" && pwd )"
DIR="$( dirname "$DIR")"

java -jar $DIR/lib/zero.jar
