package rarityeg.harmonyauthsmart;

import java.util.*;

public class RuntimeDataManager {
    private static final List<UUID> RESTRICTS = new ArrayList<>();
    private static final Map<UUID, Integer> IFORGOT_SETUP_MAP = new HashMap<>();
    private static final List<UUID> READ_MODE_LIST = new ArrayList<>();
    
    public synchronized static void addRestrictUUID(UUID id) {
        RESTRICTS.add(id);
    }

    public synchronized static void removeRestrictUUID(UUID id) {
        RESTRICTS.remove(id);
    }

    public static boolean hasRestrictUUID(UUID id) {
        return RESTRICTS.contains(id);
    }

    public synchronized static void toReadMode(UUID id) {
        READ_MODE_LIST.add(id);
    }

    public synchronized static void exitReadMode(UUID id) {
        READ_MODE_LIST.remove(id);
    }

    public synchronized static boolean isInReadMode(UUID id) {
        return READ_MODE_LIST.contains(id);
    }

    public synchronized static void toIForgotMode(UUID id, int mode) {
        IFORGOT_SETUP_MAP.put(id, mode);
    }

    public synchronized static void exitIForgotMode(UUID id) {
        IFORGOT_SETUP_MAP.remove(id);
    }

    public synchronized static int getIForgotMode(UUID id) {
        return Objects.requireNonNullElse(IFORGOT_SETUP_MAP.get(id), 0);
    }
}
