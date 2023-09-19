package csci318.demo.cargotracker.routingms.application.internal;

import csci318.demo.cargotracker.shareddomain.TransitEdge;
import csci318.demo.cargotracker.shareddomain.TransitPath;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CargoRoutingService {

    public TransitPath findOptimalRoute(String origin, String destination, String arrivalDeadline){

            TransitPath transitPath = new TransitPath();
            List<TransitEdge> transitEdges = new ArrayList<>();
            for (int i=0;i<4;i++) {

                TransitEdge transitEdge = new TransitEdge();
                transitEdge.setVoyageNumber("V11");
                transitEdge.setFromUnLocode("CHK");
                transitEdge.setFromDate(new Date());
                transitEdge.setToDate(new Date());
                transitEdge.setToUnLocode("NYC");
                transitEdges.add(transitEdge);
            }
            transitPath.setTransitEdges(transitEdges);
            return transitPath;
    }
}
