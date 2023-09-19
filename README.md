# Cargo Tracker 0.2

This project is an extension to [CargoTracker 0.1](https://github.com/gxshub/cargo-tracker-0.1/tree/v2).
A new _stream processing_ microservice **Analytics** is implemented. 
It creates a stream of the total cargo booking amounts by destination
(namely, a "_SUM with Group By_" SQL-like query).

To enable this stream-based query, the [`CargoBookedEvenData`](./bookingms/src/main/java/csci318/demo/cargotracker/shareddomain/events/CargoBookedEventData.java) class is enriched.
Also for this demonstration purposes, a demo client is also created to send random booking requests continuously.

After setting up Apache Kafka (see below), run the **Booking MS**, **Analytics MS** and **Demo Client**, 
and then monitor data in the consoles.

## Apache Kafka Setup
This Spring Boot project uses Apache Kafka as a messaging platform.
To run this project, you need to set up Kafka first.

#### Linux and MacOS
Download a **binary package** of Apache Kafka (e.g., `kafka_2.13-2.8.0.tgz`) from
[https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
and upzip it.
In the Terminal, `cd` to the unzip folder, and start Kakfa with the following commands (each in a separate Terminal session):
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
```

#### Windows
Download a **binary package** of Apache Kafka (e.g., `kafka_2.13-2.8.0.tgz`) from
[https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
and unzip it to a directory, e.g., `C:\kafka`&mdash;Windows does not like a complex path name (!).

<!--
In the configuration file `C:\kafka\config\zookeeper.properties`, comment out the line `"dataDir=/tmp/zookeeper"`. In `C:\kafka\config\server.properties`, change the line `"log.dirs=/tmp/kafka-logs"` to `"log.dirs=.kafka-logs"`.
-->

Use the following two commands in the Windows CMD (one in each window) to start Kafka:
```bash
C:\kafka\bin\windows\zookeeper-server-start.bat C:\kafka\config\zookeeper.properties
```
```bash
C:\kafka\bin\windows\kafka-server-start.bat C:\kafka\config\server.properties
```

