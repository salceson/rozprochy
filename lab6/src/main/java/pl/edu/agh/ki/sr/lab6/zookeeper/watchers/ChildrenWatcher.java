package pl.edu.agh.ki.sr.lab6.zookeeper.watchers;

import lombok.RequiredArgsConstructor;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeChildrenChanged;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class ChildrenWatcher implements Watcher {
    private final ZooKeeper zooKeeper;
    private final String znode;
    private final Logger logger = LoggerFactory.getLogger(ChildrenWatcher.class);

    @Override
    public void process(WatchedEvent event) {
        if (event.getPath() == null || !event.getPath().equals(znode)) {
            try {
                zooKeeper.getChildren(znode, this);
            } catch (KeeperException | InterruptedException e) {
                logger.error("Caught an exception: ", e);
            }
        }
        if (event.getType() == NodeChildrenChanged) {
            int count = countChildren(znode);
            System.out.print("Children count: " + count + "\n> ");
        }
    }

    private int countChildren(String path) {
        int childrenCount = 0;
        List<String> children;
        try {
            children = zooKeeper.getChildren(path, this);
        } catch (KeeperException | InterruptedException e) {
            logger.error("Caught an exception: ", e);
            return 0;
        }
        for (String child : children) {
            final String childPath = path + "/" + child;
            childrenCount++;
            childrenCount += countChildren(childPath);
        }
        return childrenCount;
    }
}
