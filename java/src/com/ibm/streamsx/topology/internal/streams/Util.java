/*
# Licensed Materials - Property of IBM
# Copyright IBM Corp. 2015  
 */
package com.ibm.streamsx.topology.internal.streams;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Util {
    public static final String STREAMS_DOMAIN_ID = "STREAMS_DOMAIN_ID";
    public static final String STREAMS_INSTANCE_ID = "STREAMS_INSTANCE_ID";
    private static final String STREAMS_INSTALL = "STREAMS_INSTALL";
    private static String streamsInstall;

    /**
     * Get a valid STREAMS_INSTALL value.
     * @return STREAMS_INSTALL value
     * @throws IllegalStateException if STREAMS_INSTALL not set
     *          or not set to a Streams install directory
     */
    public static String getStreamsInstall() throws IllegalStateException {
        if (streamsInstall==null) {
            String si = getenv(STREAMS_INSTALL);
            File f = new File(si, ".product");
            if (!f.exists())
                throw new IllegalStateException(STREAMS_INSTALL+" "+si+" is not a Streams install directory.");
            streamsInstall = si;
        }
        return streamsInstall;
    }
    
    /**
     * Get the environment variable value
     * @param name environment variable name
     * @return env value
     * @throws IllegalStateException if name not set
     */
    public static String getenv(String name) throws IllegalStateException {
        String s = System.getenv(name);
        if (s==null)
            throw new IllegalStateException(name+" environment variable is not set.");
        return s;
    }
    
    /**
     * Check if things are in place to successfully invoke streamtool
     * @throws IllegalStateException if preconditions not met
     */
    public static void checkInvokeStreamtoolPreconditions() 
            throws IllegalStateException {
        Util.getenv(Util.STREAMS_DOMAIN_ID);
        Util.getenv(Util.STREAMS_INSTANCE_ID);
    }
 
    /**
     * Extract the map item <code>key</code> with constraint checking.
     * @param map map of key/value
     * @param key item of interest
     * @param requiredClass
     * @return the value associated with the key
     * @throws IllegalArgumentException if value is null or isn't instanceof requiredClass
     */
    public static Object getConfigEntry(Map<String,? extends Object> map, String key, 
            Class<?> requiredClass) throws IllegalArgumentException {
        Object val = map.get(key);
        if (val==null)
            throw new IllegalArgumentException("config item "+key+" value is null");
        if (!requiredClass.isAssignableFrom(val.getClass())) {
            throw new IllegalArgumentException("config item "+key
                                        + " value is "+val.getClass()
                                        + " but require instanceof "+requiredClass);
        }
        return val;
    }

    /**
     * Concatenate a list with items separated with a space.
     * 
     * @param strings list to concatenate
     * @return concatenated list
     */
    public static String concatenate(List<String> strings) {
        // wish for java8 String.join()
        StringBuilder cmdsb = new StringBuilder();
        for(String s : strings) {
            cmdsb.append(s); cmdsb.append(" ");
        }
        return cmdsb.toString();
    }

}