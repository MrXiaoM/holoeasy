package org.holoeasy.util;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class Serializers {
    public static WrappedDataWatcher.Serializer BYTE_SERIALIZER() {
        return WrappedDataWatcher.Registry.get(java.lang.Byte.class);
    }

    public static WrappedDataWatcher.Serializer BOOL_SERIALIZER() {
        return WrappedDataWatcher.Registry.get(java.lang.Boolean.class);
    }

    public static WrappedDataWatcher.Serializer STRING_SERIALIZER() {
        return WrappedDataWatcher.Registry.get(java.lang.String.class);
    }

    public static WrappedDataWatcher.Serializer ITEM_SERIALIZER() {
        return WrappedDataWatcher.Registry.getItemStackSerializer(false);
    }
}
