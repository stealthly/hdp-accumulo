#
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
#
#

# The file containing the running pid
PID_FILE=/var/run/webhcat/webhcat.pid

WEBHCAT_LOG_DIR=/var/log/webhcat

# The console error log
ERROR_LOG=/var/log/webhcat/webhcat-console-error.log

# The console log
CONSOLE_LOG=/var/log/webhcat/webhcat-console.log

# Set HADOOP_HOME to point to a specific hadoop install directory
export HADOOP_HOME=${HADOOP_HOME:-/usr}

# set hadoop HADOOP_CLIENT_OPTS so that hadoop does not override it
export HADOOP_CLIENT_OPTS="-Xmx256m $HADOOP_CLIENT_OPTS"
