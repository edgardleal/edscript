#!/bin/bash

export PATH=/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/X11/bin:/usr/local/MacGPG2/bin

current_path="$(pwd)"

java  -Duser.timezone='UTC-03:00' -classpath 'lib/*' -DCURRENT_PATH=$current_path carga.EdScript $@
