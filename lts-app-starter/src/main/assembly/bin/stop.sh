#!/bin/sh
# shellcheck disable=SC2006
pid=`ps -fu $USER |grep java | grep com.shaw.lts.starter.LtsAppApplication |awk '{print $2}'`

if [ "$pid" != "" ]
then
    kill -15 $pid
fi
