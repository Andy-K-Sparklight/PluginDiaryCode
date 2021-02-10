package rarityeg.harmonyauthsmart;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.UUID;

public interface IDataManager {
    void saveAll();

    void loadAll();

    @Nonnull
    String getPasswordHash(UUID id);

    boolean getIForgotState(UUID id);

    @Nonnull
    String getIForgotManualReason(UUID id);

    @Nonnull
    String getIForgotNewPasswordHash(UUID id);

    @Nonnull
    Date getLastLoginTime(UUID id);

    void setPasswordHash(UUID id, String hash);

    void setIForgotState(UUID id, boolean state);

    void setIForgotManualReason(UUID id, String reason);

    void setIForgotNewPasswordHash(UUID id, String hash);

    void setLastLoginTime(UUID id, Date date);

    boolean isExist(UUID id);

    UUID getNextRequest();

}
