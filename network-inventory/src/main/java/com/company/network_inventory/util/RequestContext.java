package com.company.network_inventory.util;

public class RequestContext {
    private static final ThreadLocal<String> actor = new ThreadLocal<>();
    private static final ThreadLocal<String> requestId = new ThreadLocal<>();

    public static void setActor(String value) { actor.set(value); }
    public static String getActor() { return actor.get(); }

    public static void setRequestId(String value) { requestId.set(value); }
    public static String getRequestId() { return requestId.get(); }

    public static void clear() {
        actor.remove();
        requestId.remove();
    }
}
