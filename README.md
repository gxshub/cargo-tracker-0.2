# Cargo Tracker 0.2 (Stream Processing)

## Apache Kafka Setup
This Spring Boot project uses Apache Kafka as a messaging platform.
To run this project, you need to set up Kafka first.

#### Linux and MacOS
Download a **binary package** of Apache Kafka (e.g., `kafka_2.13-3.7.0.tgz`) from
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
Download a **binary package** of Apache Kafka (e.g., `kafka_2.13-3.7.0.tgz`) from
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

### Run the Application ##
Book and check cargoes with the following command:
(Linux/MacOS)
```shell
curl -X POST -H "Content-Type:application/json" -d '{"bookingAmount":20,"originLocation":"HK","destLocation":"NY","destArrivalDeadline":"2010-08-01"}' http://localhost:8787/cargobooking
```
```shell
curl -X GET -H "Content-Type:application/json" http://localhost:8787/cargobooking/findAllBookingIds
```
(windows)
```shell
curl -X POST -H "Content-Type:application/json" -d "{\"bookingAmount\":20,\"originLocation\":\"HK\",\"destLocation\":\"NY\",\"destArrivalDeadline\":\"2010-08-01\"}" http://localhost:8787/cargobooking
```
```shell
curl -X GET -H "Content-Type:application/json" http://localhost:8787/cargobooking/findAllBookingIds
```

### View Kafka Topics
After running the `bookingms`'s main class, check the Kafka topics with the following command:

(Linux/MacOS)
```shell
./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --list
```
(Windows)
```shell
C:\kafka\bin\windows\kafka-topics.bat --bootstrap-server=localhost:9092 --list
```
You should see a topic name `cargobookings`. You can read data in the `cargobookings` topic:

(Linux/MacOS)
```shell
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cargobookings --from-beginning
```
(Windows)
```shell
c:\kafka\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic cargobookings --from-beginning
```

### Trouble Shooting
If you cannot start Kafka, try to clean up data in the Kafka topics to start over.
For this purpose, in Linux/MacOS, delete the folders `/tmp/zookeeper`, `/tmp/kafka-logs`
and `/tmp/kafka-streams` (if any). In Windows, delete the folders `C:\tmp\zookeeper`,
`C:\tmp\kafka-logs` and `C:\kafka\kafka-streams` (if any).

## Stream Processing Microservices

This project is an extension to [CargoTracker 0.1](https://github.com/gxshub/cargo-tracker-0.1/tree/v2).
A new _stream processing_ microservice **Analytics** is implemented. 
It creates a stream of the total cargo booking amounts by destination
(namely, a "_SUM with Group By_" SQL-like query).

<!-- 
To enable this stream-based query, the [`CargoBookedEvenData`](./bookingms/src/main/java/csci318/demo/cargotracker/shareddomain/events/CargoBookedEventData.java) class is enriched with more attributes (compared with the same event in version [0.1](https://github.com/gxshub/cargo-tracker-0.1/tree/v2)).
Also for demonstration purposes, a demo client is created to send random booking requests continuously.
-->

<!-- After setting up Apache Kafka (see below for [instructions](./README.md#apache-kafka-setup)), run the **Booking MS**, **Analytics MS** and **Demo Client**, 
and then monitor the data shown in the consoles.-->

The stream processing function is implemented in the `StreamProcessor` class of **Analytics MS**:
```java
@Configuration
public class StreamProcessor {
    public final static String BOOKING_STATE_STORE = "cargo-booking";

    @Bean
    public Consumer<KStream<String, CargoBookedEvent>> process() {
        return inputStream -> {

            //generate RUNNING total booking amounts by destination
            KStream<String, Long> aggregatedStream = inputStream.map((key, value) -> {
                        String destCity = value.getCargoBookedEventData().getDestLocation();
                        Long bookAmount = value.getCargoBookedEventData().getBookingAmount().longValue();
                        return KeyValue.pair(destCity, bookAmount);
                    }).
                    groupByKey(Grouped.with(Serdes.String(), Serdes.Long())).
                    reduce(Long::sum).toStream();

            //just print the stream out to console
            aggregatedStream.
                    print(Printed.<String, Long>toSysOut().withLabel("Total booking amount by destination"));
        };
    }
}
```
The Kafka binding configuration for **Analytics MS** is defined in the YAML format, i.e., [`application.yml`](./analyticsms/src/main/resources/application.yml) file (which is similar to the previously used `application.properties` file but more readable):
```yaml
server.port: 8788
spring.cloud.stream.bindings:
  process-in-0:
    destination: cargobookings
spring.cloud.stream.kafka.streams.binder:
  brokers: localhost:9092
  serdeError: logAndContinue
  configuration:
    commit.interval.ms: 500
    default.key.serde: org.apache.kafka.common.serialization.Serdes$StringSerde
    default.value.serde: org.springframework.kafka.support.serializer.JsonSerde
    spring.json.value.default.type: csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent
```
***Note (IMPORTANT!)*** The function name `process()` in the above java class `StreamProcessor` must match the string `"process-in-0"` in the above YAML file
(e.g., if the function name is `whatevernameyoulike()` then the corresponding string is `"whatevernameyoulike-in-0"`).


