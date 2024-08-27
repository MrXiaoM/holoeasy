package org.holoeasy.packet;

import com.google.common.collect.Lists;
import org.holoeasy.util.ClosedRange;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;

import java.util.List;

public interface IPacket {

    List<ClosedRange<VersionEnum>> versionSupport();

    default boolean isCurrentVersion() {
        for (ClosedRange<VersionEnum> range : versionSupport()) {
            if (VersionUtil.CLEAN_VERSION.in(range)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    static List<ClosedRange<VersionEnum>> list(ClosedRange<VersionEnum>... vararg) {
        return Lists.newArrayList(vararg);
    }
}


