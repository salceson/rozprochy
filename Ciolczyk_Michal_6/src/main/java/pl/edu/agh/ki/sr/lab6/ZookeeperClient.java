package pl.edu.agh.ki.sr.lab6;

import lombok.RequiredArgsConstructor;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.sr.lab6.zookeeper.console.Console;
import pl.edu.agh.ki.sr.lab6.zookeeper.watchers.ChildrenWatcher;
import pl.edu.agh.ki.sr.lab6.zookeeper.watchers.ZNodeCallback;

import java.io.IOException;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class ZookeeperClient implements Watcher, Runnable {
    private ZooKeeper zooKeeper;
    private final String hostPort;
    private final String znode;
    private final String filename;
    private final String exec[];
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    private ZNodeCallback callback;
    private ChildrenWatcher childrenWatcher;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("USAGE: ZookeeperClient host:port znode filename program [args ...]");
            System.exit(2);
        }
        String hostPort = args[0];
        String znode = args[1];
        String filename = "troll.txt";
        String exec[] = new String[args.length - 2];
        System.arraycopy(args, 2, exec, 0, exec.length);
        try {
            new Thread(new ZookeeperClient(hostPort, znode, filename, exec)).start();
        } catch (Exception e) {
            logger.error("Caught an exception: ", e);
        }
    }

    public void run() {
        try {
            zooKeeper = new ZooKeeper(hostPort, 3000, this);
            callback = new ZNodeCallback(zooKeeper, znode, exec);
            childrenWatcher = new ChildrenWatcher(zooKeeper, znode);
            new Thread(callback).start();
        } catch (IOException e) {
            logger.error("Caught an exception: ", e);
            return;
        }
        Console console = new Console(zooKeeper, znode, callback);
        new Thread(console).start();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() != Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    //Not needed now
                    break;
                case Expired:
                    callback.quit(1);
                    break;
            }
        }
    }
}
