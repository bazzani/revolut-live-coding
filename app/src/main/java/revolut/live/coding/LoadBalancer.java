package revolut.live.coding;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class LoadBalancer {
    private static final int MAX_INSTANCES_ALLOWED = 10;

    final Set<URI> instances;

    public LoadBalancer() {
        this.instances = new HashSet<>();
    }

    public void registerInstance(URI instanceUri) {
        if (registeredInstanceCount() == MAX_INSTANCES_ALLOWED) {
            throw new LoadBalancerException("You can only register 10 backend instances in the Load Balancer");
        }

        instances.add(instanceUri);
    }

    boolean isRegistered(URI instanceUri) {
        return instances.contains(instanceUri);
    }

    public int registeredInstanceCount() {
        return instances.size();
    }
}
