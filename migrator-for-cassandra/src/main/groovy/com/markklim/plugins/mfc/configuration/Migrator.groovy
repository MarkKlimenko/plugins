package com.markklim.plugins.mfc.configuration

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.Session

class Migrator {
    private static Cluster cluster = null
    private static Session session = null

    static Session openSession(Map parameters) {
        if(cluster == null ) {
            if(parameters.user && parameters.password ) {
                cluster = Cluster.builder()
                        .addContactPoint(parameters.host)
                        .withPort(parameters.port as Integer)
                        .withAuthProvider(new PlainTextAuthProvider(parameters.user, parameters.password))
                        .build()
            } else {
                cluster = Cluster.builder()
                        .addContactPoint(parameters.host)
                        .withPort(parameters.port as Integer)
                        .build()
            }
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
