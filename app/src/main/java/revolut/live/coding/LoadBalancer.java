package revolut.live.coding;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoadBalancer {
    private static final int MAX_INSTANCES_ALLOWED = 10;

    final CopyOnWriteArrayList<URI> instances;

    public LoadBalancer() {
        this.instances = new CopyOnWriteArrayList();
    }

    synchronized public void registerInstance(URI instanceUri) {
        if (registeredInstanceCount() == MAX_INSTANCES_ALLOWED) {
            throw new LoadBalancerException("You can only register 10 backend instances in the Load Balancer");
        }

        if (!instances.contains(instanceUri)) {
            instances.add(instanceUri);
        }
    }

    boolean isRegistered(URI instanceUri) {
        return instances.contains(instanceUri);
    }

    public int registeredInstanceCount() {
        return instances.size();
    }

    public URI get() {
        if (instances.isEmpty()) {
            throw new LoadBalancerException("No instances in the Load Balancer");
        }

        int instanceIndex = new Random().nextInt(instances.size());

        return instances.get(instanceIndex);
    }
}
