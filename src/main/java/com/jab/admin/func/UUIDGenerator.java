package com.jab.admin.func;

public class UUIDGenerator {
    public static String getUUID() {
      return java.util.UUID.randomUUID().toString();
    }
}
