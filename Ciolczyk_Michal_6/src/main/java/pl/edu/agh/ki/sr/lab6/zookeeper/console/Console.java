package pl.edu.agh.ki.sr.lab6.zookeeper.console;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.sr.lab6.zookeeper.watchers.ZNodeCallback;

import java.util.List;
import java.util.Scanner;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class Console implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Console.class);
    private final ZooKeeper zooKeeper;
    private final String znode;
    private final ZNodeCallback callback;

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error("Caught an exception: ", e);
        }
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if ("structure".equals(line) || "s".equals(line)) {
                try {
                    printStructure(znode);
                } catch (KeeperException | InterruptedException e) {
                    logger.error("Caught an exception", e);
                }
            }
            if ("quit".equals(line) || "q".equals(line)) {
                callback.quit(0);
            }
            if("help".equals(line) || "h".equals(line)) {
                System.out.println("Commands:");
                System.out.println("\thelp, h\t\t\t- display this help");
                System.out.println("\tstructure, s\t- display the structure of the watched node (" + znode + ")");
                System.out.println("\tquit, q\t\t\t- quits the program");
            }
        }
    }

    private void printStructure(String znode) throws KeeperException, InterruptedException {
        printStructure(znode, 0);
        System.out.println();
    }

    private void printStructure(String path, int indent) throws KeeperException, InterruptedException {
        List<String> children;

        try {
            children = zooKeeper.getChildren(path, false);
        } catch (KeeperException e) {
            logger.info("No node named " + znode + " or another exception: ", e);
            return;
        }

        System.out.print(Strings.repeat("\t", indent));
        System.out.println(path);

        for (String child : children) {
            printStructure(path + "/" + child, indent + 1);
        }
    }
}
