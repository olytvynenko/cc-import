#!/bin/bash
export SPARK_MAJOR_VERSION=2
export HADOOP_CONF_DIR=/etc/hadoop/conf
/usr/bin/spark-submit  \
        --master "yarn"  \
        --deploy-mode "cluster"  \
        --num-executors 30  \
        --executor-cores 1  \
        --driver-cores 4  \
        --executor-memory 7G  \
        --driver-memory 7G  \
        --conf spark.yarn.executor.memoryOverhead=2048  \
        --conf spark.yarn.driver.memoryOverhead=2048  \
        --conf spark.yarn.maxAppAttempts=1  \
        --conf spark.sql.files.ignoreCorruptFiles=true \
        --verbose  \
        --name "cc-import"  \
		--class ltv.spark.Run  \
        ./target/scala-2.11/cc-import.jar "$1"