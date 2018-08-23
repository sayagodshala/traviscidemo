package com.merabreak.pushNotifications;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay on 11/10/2016.
 */
public enum NotificationType {

    NEW_CHALLENGE(1), NEW_OFFER(2), FOR_GUEST(3), NEW_PRODUCT(4), NEW_RAFFLE(5), REFFERER_REGD(6), USER_LEVELUP(7), RAFFLE_WINNER(8), LIVE_CHALLENGE(9), DLT_UNKNOWN(101);

    int value;

    private NotificationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NotificationType fromInt(int i) {
        Map<Integer, NotificationType> intToTypeMap = new HashMap<Integer, NotificationType>();

        for (NotificationType type : NotificationType.values()) {
            intToTypeMap.put(type.value, type);
        }

        NotificationType type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return NotificationType.DLT_UNKNOWN;
        return type;
    }
}
