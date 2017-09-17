package com.markklim.plugins.mfc.configuration

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

class Migrator {
    private static Cluster cluster = null
    private static Session session = null

    static Session openSession(Map parameters) {
        if(cluster == null ) {
            cluster = Cluster.builder()
                    .addContactPoint(parameters.host)
                    .build()
            session = cluster.connect()
        }
        session
    }

    static void closeSession() {
        if (cluster != null) {
            session = null
            cluster.close()
            cluster = null
        }
        println 'Session closed'
    }
}
