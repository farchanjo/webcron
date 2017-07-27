# WEB Cron

This project is spring boot based and using features from Quartz.

There are a web interface to manage all cron jobs.

# How to build
```bash
mvn package
```
It's going generate a artifact called webcron.jar, the just start it.

```bash
java -Dfile.encoding=UTF-8 -Xms50m -Xmx200m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -jar webcron.jar
```
You must choose your heap space for your needed.

STILL IN DEVELOPMENT. If you know Java+Spring come to help to improve it!! :)
