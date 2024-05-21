package xyz.mjav.theqbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Threaded class to perform periodic database operations
 */
public class SqliteDbTasks implements Runnable {

    private static Logger log = LogManager.getLogger("common-log");

    private Database sqliteDb;
    public Boolean isThreadRunning = true;

    /**
     * Class constructor
     * @param sqliteDb
     */
    public SqliteDbTasks(Database sqliteDb) {
        this.sqliteDb = sqliteDb;
    }

    public void run() {

        log.info("Started db cron thread");

        try {
            while(isThreadRunning == true) {
                Thread.sleep(60000); // to put inside conf file
                sqliteDb.cleanInvalidLoginTokens();
            }
        }
        catch (Exception e) { log.error("SqliteDbTasks/run: error starting database tasks: ", e); }
    }
}
