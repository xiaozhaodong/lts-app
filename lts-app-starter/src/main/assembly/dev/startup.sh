# shellcheck disable=SC1113
# /usr/bin/env bash
DEPLOY_DIR=/Users/xiaozhaodong/data/llas-service
# 打包的压缩包根目录 config 文件夹
CONF_DIR=$DEPLOY_DIR/cfg
# 打包的压缩包根目录 lib 文件夹
LIB_DIR=$DEPLOY_DIR/lib
# 打包的压缩包根目录 bin 文件夹
BIN_DIR=$DEPLOY_DIR/bin
XMS_VALUE=512m
XMX_VALUE=512m
XMN_VALUE=128m
#JAR_NAME=$(ls -lt $BIN_DIR | grep .jar$ | head -n 1 | awk '{print $9}')
#echo "Found Jar file '$JAR_NAME', we are starting it"
PIDS=$(ps -f | grep java | grep "$CONF_DIR" | awk '{print $2}')
if [ "$1" = "status" ]; then
  if [ -n "$PIDS" ]; then
    echo "The Application is running...!"
    echo "PID: $PIDS"
    exit 0
  else
    echo "The Application is stopped"
    exit 0
  fi
fi
if [ -n "$PIDS" ]; then
  echo "ERROR: The Application already started!"
  echo "PID: $PIDS"
  exit 1
fi
if [ -n "$2" ]; then
  SERVER_PORT_COUNT=$(netstat -tln | grep $2 | wc -l)
  if [ $SERVER_PORT_COUNT -gt 0 ]; then
    echo "ERROR: The Application port $2 already used!"
    exit 1
  fi
fi
# 无显示设备状态 仅仅支持IPV4
JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
  JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
  JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
JAVA_MEM_OPTS=""
BITS=$(java -version 2>&1 | grep -i 64-bit)
if [ -n "$BITS" ]; then
  JAVA_MEM_OPTS=" -server -Xms$XMS_VALUE -Xmx$XMX_VALUE -Xmn$XMN_VALUE -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
  JAVA_MEM_OPTS=" -server -Xms$XMS_VALUE -Xmx$XMX_VALUE -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi
#classpath设置########################################
ls $LIB_DIR > lib.list

libpath=".:$DEPLOY_DIR/bin"
# libpath="../lib/"
while read line
do
  # echo $line
  libpath=$libpath":../lib/"$line
done < lib.list
rm -rf lib.list
#echo $libpath
CONFIG_FILES=" -Dspring.config.location=$CONF_DIR/ "
CLASS_PATH_VALUE=" -classpath $libpath com.shaw.lts.starter.LtsAppApplication"
#JAVA_SYSTEM_PROPERTY=" -Dspring.devtools.restart.enabled=false "
JAVA_SYSTEM_PROPERTY=" -Dspring.profiles.active=dev "
echo "Application starting ~ "
echo "nohup java $JAVA_SYSTEM_PROPERTY $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS $CONFIG_FILES $CLASS_PATH_VALUE $MAIN_CLASS &"
nohup java $JAVA_SYSTEM_PROPERTY $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS $CONFIG_FILES $CLASS_PATH_VALUE $MAIN_CLASS &
PIDS=$(ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}')
echo "PID: $PIDS"
tail -f nohup.out
#echo "Application starting success"
