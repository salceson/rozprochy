package pl.edu.agh.ki.sr.lab6.zookeeper.watchers;

import lombok.RequiredArgsConstructor;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

import static org.apache.zookeeper.AsyncCallback.StatCallback;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class ZNodeCallback implements StatCallback, Runnable {
    private Logger logger = LoggerFactory.getLogger(ZNodeCallback.class);
    private final ZooKeeper zooKeeper;
    private final String znode;
    private final String[] exec;
    private Process child;
    private ChildrenWatcher childrenWatcher;
    private byte[] prevData;

    @SuppressWarnings("deprecation")
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                quit(0);
            default:
                // Retry errors
                zooKeeper.exists(znode, true, this, null);
                return;
        }
        byte b[] = null;
        if (exists) {
            try {
                b = zooKeeper.getData(znode, false, null);
            } catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((b == null && b != prevData) || (b != null && !Arrays.equals(prevData, b)) && znode.equals(path)) {
            if (b != null) {
                if (child != null) {
                    child.destroy();
                    child = null;
                }
                try {
                    child = Runtime.getRuntime().exec(exec);
                } catch (IOException e) {
                    logger.error("Caught an exception: ", e);
                }
            } else {
                if (child != null) {
                    child.destroy();
                    child = null;
                }
            }
            prevData = b;
        }
        if (b != null && path.equals(znode)) {
            try {
                zooKeeper.getChildren(znode, childrenWatcher);
            } catch (KeeperException.NoNodeException ignored) {

            } catch (KeeperException | InterruptedException e) {
                logger.error("Caught an exception: ", e);
            }
        }
        zooKeeper.exists(znode, true, this, null);
    }

    @Override
    public void run() {
        childrenWatcher = new ChildrenWatcher(zooKeeper, znode);
        zooKeeper.exists(znode, true, this, null);
    }

    public void quit(int result) {
        if (child != null) {
            child.destroy();
            child = null;
        }
        logger.error("Session broken");
        System.exit(result);
    }
}
