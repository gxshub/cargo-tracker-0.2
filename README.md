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

## Stream Processing

This project is an extension to [CargoTracker 0.1](https://github.com/gxshub/cargo-tracker-0.1/tree/v2).
A new service named **Analytics MS** is implemented, which processes the
booking event stream and enables real-time queries.

<!-- 
To enable this stream-based query, the [`CargoBookedEvenData`](./bookingms/src/main/java/csci318/demo/cargotracker/shareddomain/events/CargoBookedEventData.java) class is enriched with more attributes (compared with the same event in version [0.1](https://github.com/gxshub/cargo-tracker-0.1/tree/v2)).
Also for demonstration purposes, a demo client is created to send random booking requests continuously.
-->

<!-- After setting up Apache Kafka (see below for [instructions](./README.md#apache-kafka-setup)), run the **Booking MS**, **Analytics MS** and **Demo Client**, 
and then monitor the data shown in the consoles.-->

The stream processing function is implemented in the [`StreamProcessor`](./analyticsms/src/main/java/csci318/demo/cargotracker/analyticsms/applicationservice/StreamProcessor.java) class of **Analytics MS**.
The processing logic can be expressed as "the total cargo booking amounts by destination (city)", 
namely, a "_SUM with Group By_" SQL-like query.
The aggregation results are persistent (or "materialized") in a state store (in particular, a KeyValueStore).
```java
@Configuration
public class StreamProcessor {
    public final static String TOTAL_BOOKINGS = "total-bookings";

    @Bean
    public Consumer<KStream<String, CargoBookedEvent>> process() {
        return inputStream -> {

            //generate RUNNING total booking amounts by destination
            KTable<String, Long> totalBookings = inputStream.map((key, value) -> {
                        String destCity = value.getCargoBookedEventData().getDestLocation();
                        Long bookAmount = value.getCargoBookedEventData().getBookingAmount().longValue();
                        return KeyValue.pair(destCity, bookAmount);
                    }).
                    groupByKey(Grouped.with(Serdes.String(), Serdes.Long())).
                    reduce(Long::sum,
                            Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(TOTAL_BOOKINGS).
                                    withKeySerde(Serdes.String()).withValueSerde(Serdes.Long()));

            // print data to console (not part of the stream processing logic)
            totalBookings.toStream().
                    print(Printed.<String, Long>toSysOut().withLabel("Total bookings by city"));
        };
    }
}
```

The Kafka binding configuration for **Analytics MS** is defined in the `application.properties` file:
```properties
server.port=8788
spring.cloud.function.definition=process
spring.cloud.stream.bindings.process-in-0.destination=cargobookings
spring.cloud.stream.kafka.binder.brokers=localhost:9092
spring.cloud.stream.kafka.streams.binder.configuration.commit.interval.ms=500
```
The function `process()` in the Java class `StreamProcessor` must match the strings `"process"` and `"process-in-0"` in `application.properties`
(e.g., if the function is `whatevernameyoulike()` then the corresponding strings are `"whatevernameyoulike"` and `"whatevernameyoulike-in-0"`).

### Interactive Query at Real Time

The state store (i.e., KeyValueStore) is used for interactive queries on real-time analytics data.
The code is implemented in the 
[`InteractiveQuery`](./analyticsms/src/main/java/csci318/demo/cargotracker/analyticsms/applicationservice/InteractiveQuery.java) class:
```java
@Service
public class InteractiveQuery {

    private final InteractiveQueryService interactiveQueryService;

    public InteractiveQuery(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    public List<BookingsByCity> getAllBookingsByCity() {
        List<BookingsByCity> allBookingsByCity = new ArrayList<>();
        KeyValueIterator<String, Long> all = getTotalBookingsKSStore().all();
        while (all.hasNext()) {
            KeyValue<String, Long> ks = all.next();
            BookingsByCity quantityPerCity = new BookingsByCity();
            quantityPerCity.setCity(ks.key);
            quantityPerCity.setBookingQuantity(ks.value);
            allBookingsByCity.add(quantityPerCity);
        }
        return allBookingsByCity;
    }

    private ReadOnlyKeyValueStore<String, Long> getTotalBookingsKSStore() {
        return this.interactiveQueryService.getQueryableStore(StreamProcessor.TOTAL_BOOKINGS,
                QueryableStoreTypes.keyValueStore());
    }
}
```

#### REST Request for Interactive Query

Run the DemoClient and get the changing query results: 

(Linux/MacOS)
```shell
curl -X GET -H "Content-Type:application/json" http://localhost:8788/queries/findAllBookingsByCity
```
(windows)
```shell
curl -X GET -H "Content-Type:application/json" http://localhost:8788/queries/findAllBookingsByCity
```
