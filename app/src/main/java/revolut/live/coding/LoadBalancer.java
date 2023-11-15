package revolut.live.coding;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoadBalancer {
    private static final int MAX_INSTANCES_ALLOWED = 10;
    private static final Random RANDOM = new Random();

    final CopyOnWriteArrayList<URI> instances;
    private final boolean roundRobinModeEnabled;

    public LoadBalancer() {
        this(false);
    }

    public LoadBalancer(boolean roundRobinModeEnabled) {
        this.roundRobinModeEnabled = roundRobinModeEnabled;
        this.instances = new CopyOnWriteArrayList<>();
    }

    public synchronized void registerInstance(URI instanceUri) {
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

        if (roundRobinModeEnabled) {
            return returnRoundRobinInstance();
        } else {
            return returnRandomInstance();
        }
    }

    int currentInstanceIndex;

    private URI returnRoundRobinInstance() {

        if (currentInstanceIndex >= instances.size()) {
            currentInstanceIndex = 0;
        }

        URI uri = instances.get(currentInstanceIndex);
        currentInstanceIndex++;

        return uri;
    }

    private URI returnRandomInstance() {
        int instanceIndex = RANDOM.nextInt(instances.size());
        return instances.get(instanceIndex);
    }
}
