package rarityeg.harmonyauth.api;

import java.util.UUID;

public interface IStoredDataManager {
    /**
     * Set the password hash for a player.
     * Please ensure that the play knows the new password!
     *
     * @param id   The uuid of the player.
     * @param hash The MD5 hash of the password.
     */
    void setPasswordHash(UUID id, String hash);

    /**
     * Set the IForgot state of the player.
     *
     * @param id          The uuid of the player.
     * @param isInIforgot Whether the player is in IForgot mode.
     */
    void setIForgotState(UUID id, boolean isInIforgot);

    /**
     * Get the IForgot state of the player.
     *
     * @param id The uuid of the player.
     * @return The IForgot state.
     */
    boolean getIForgotState(UUID id);

    /**
     * Set the IForgot reason of the player.<br/>
     * <code>&lt;Internal&gt; Accepted.</code> for accepted and <code>&lt;Internal&gt; Rejected.</code> for rejected.
     *
     * @param id     The uuid of the player.
     * @param reason The IForgot reason.
     */
    void setIForgotReason(UUID id, String reason);

    /**
     * Get the IForgot reason of the player.
     *
     * @param id The uuid of the player.
     * @return The IForgot reason.
     */
    String getIForgotReason(UUID id);
}
