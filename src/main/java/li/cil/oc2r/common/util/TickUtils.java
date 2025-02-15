/* SPDX-License-Identifier: MIT */

package li.cil.oc2r.common.util;

import li.cil.oc2r.common.Constants;

import java.time.Duration;

public final class TickUtils {
    public static int toTicks(final Duration duration) {
        return (int) (duration.getSeconds() * Constants.SECONDS_TO_TICKS);
    }
}
