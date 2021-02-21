package rarityeg.harmonyauthsmart;

import rarityeg.harmonyauth.api.IStoredDataManager;

import java.util.UUID;

public class APIStoredDataManager implements IStoredDataManager {

    @Override
    public void setPasswordHash(UUID id, String hash) {
        getDataManager().setPasswordHash(id, hash);
    }

    @Override
    public void setIForgotState(UUID id, boolean isInIforgot) {
        getDataManager().setIForgotState(id, isInIforgot);
    }

    @Override
    public boolean getIForgotState(UUID id) {
        return getDataManager().getIForgotState(id);
    }

    @Override
    public void setIForgotReason(UUID id, String reason) {
        getDataManager().setIForgotManualReason(id, reason);
    }

    @Override
    public String getIForgotReason(UUID id) {
        return getDataManager().getIForgotManualReason(id);
    }

    private IDataManager getDataManager() {
        if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
            return new DBDataManager();
        } else {
            return new FileDataManager();
        }
    }
}
