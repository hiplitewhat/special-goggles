#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG" > /dev/null
    link=$( ls -L "$PRG" | awk '{print $NF}' )
    case $link in
        /*) PRG="$link" ;;
        *) PRG=$( dirname "$PRG" )/"$link" ;;
    esac
done
SAVED="$( cd -P "$( dirname "$PRG" )" && pwd )"
APP_HOME=$SAVED
APP_NAME="Gradle"
APP_BASE_NAME=$( basename "$0" )

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != maximum possible on this system.
MAX_FD="maximum"

# warn warns the given message
warn () {
    echo "$*" >&2
}

# die dies with the given message and code
die () {
    echo
    echo "$*"
    echo
    exit "$2"
}

#
# Try to find a valid Java home directory and then invoke the script. We attempt to do all of this
# before using any external programs.
#
# This will fail if, for instance, you edited .bash_profile, csh.login or some other hopefully-rarely-used
# shell initialization file to set MAX_FD to 'unlimited'. In this case, the shell code to compute MAX_FD
# above will fail and kapt the shell exit code.
# On all other shells, the result will be ignored.
#
if ! /usr/libexec/java_home -requires 11 >/dev/null 2>&1; then
    die "no viable Java home was found for Java 11"
fi

JDK_HOME=$(/usr/libexec/java_home -requires 11 2>/dev/null) || JDK_HOME=$(dirname $(dirname $(readlink -f $(which java))))
JAVA_HOME=$JDK_HOME
export JAVA_HOME

# Increase the maximum file descriptors if we can, though use "maximum" only if we seem to be in a
# Linux system we know the max is very high.
if ! "$cygwin" && ! "$darwin" && ! "$nonstop" ; then
    case $( uname ) in
        Linux* )
            ulimit -n "$MAX_FD" || warn "Could not set maximum file descriptor limit to $MAX_FD"
            ;;
    esac
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=$( save "$@" )

# Collect all arguments for the java command, following the shell argument syntax rules
set -- \
        "-Dorg.gradle.appname=$APP_BASE_NAME" \
        -classpath "$CLASSPATH" \
        org.gradle.wrapper.GradleWrapperMain \
        "$APP_ARGS"

exec java "$@"
