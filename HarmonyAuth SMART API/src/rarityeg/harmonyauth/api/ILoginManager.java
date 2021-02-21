package rarityeg.harmonyauth.api;

import java.util.UUID;

public interface ILoginManager {
    /**
     * Check if the player has logged in.
     *
     * @param id The uuid of the player.
     * @return If this player has logged in.
     */
    boolean isLoggedIn(UUID id);

    /**
     * Set a player logged in, password is not essential.
     *
     * @param id The uuid of the player.
     */
    void login(UUID id);
}

