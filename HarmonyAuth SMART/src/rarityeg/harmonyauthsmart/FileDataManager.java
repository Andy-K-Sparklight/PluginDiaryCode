package rarityeg.harmonyauthsmart;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class FileDataManager implements IDataManager {
    static FileConfiguration data;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveAll() {
        try {
            File dataFile = new File(HarmonyAuthSMART.instance.getDataFolder(), "data.yml");
            data.save(dataFile);
        } catch (IOException e) {
            HarmonyAuthSMART.instance.getLogger().warning("配置数据未能保存，可能产生回档问题！");
            e.printStackTrace();
        }
    }

    @Override
    public void loadAll() {
        File dataFile = new File(HarmonyAuthSMART.instance.getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    @Override
    @Nonnull
    public String getPasswordHash(UUID id) {
        return Objects.requireNonNull(data.getString("passwords." + id.toString(), ""));
    }

    @Override
    public boolean getIForgotState(UUID id) {
        return data.getBoolean("iforgot-states." + id.toString());
    }

    @Override
    @Nonnull
    public String getIForgotManualReason(UUID id) {
        return Objects.requireNonNull(data.getString("iforgot-reasons." + id.toString(), ""));
    }

    @Override
    @Nonnull
    public String getIForgotNewPasswordHash(UUID id) {
        return Objects.requireNonNull(data.getString("iforgot-newpwd." + id.toString(), ""));
    }

    @Override
    @Nonnull
    public Date getLastLoginTime(UUID id) {
        String dstr = data.getString("last-login." + id.toString(), "1970-01-01 23:59:59");
        if (dstr == null) {
            try {
                return sdf.parse("1970-01-01 23:59:59");
            } catch (ParseException e) {
                HarmonyAuthSMART.instance.getLogger().warning("这不可能！不可能出现这个错误！日期的读取失败了？");
                e.printStackTrace();
                return new Date();
            }
        } else {
            try {
                return sdf.parse(dstr);
            } catch (ParseException e) {
                try {
                    return sdf.parse("1970-01-01 23:59:59");
                } catch (ParseException e2) {
                    HarmonyAuthSMART.instance.getLogger().warning("这不可能！不可能出现这个错误！日期的读取失败了？");
                    e2.printStackTrace();
                    return new Date();
                }
            }
        }
    }

    @Override
    public void setPasswordHash(UUID id, String hash) {
        data.set("passwords." + id.toString(), hash);
    }

    @Override
    public void setIForgotState(UUID id, boolean state) {
        data.set("iforgot-states." + id.toString(), state);
    }

    @Override
    public void setIForgotManualReason(UUID id, String reason) {
        data.set("iforgot-reasons." + id.toString(), reason);
    }

    @Override
    public void setIForgotNewPasswordHash(UUID id, String hash) {
        data.set("iforgot-newpwd." + id.toString(), hash);
    }

    @Override
    public void setLastLoginTime(UUID id, Date date) {
        data.set("last-login." + id.toString(), sdf.format(date));
    }

    @Override
    public boolean isExist(UUID id) {
        return data.contains("passwords." + id.toString());
    }

    @Override
    public UUID getNextRequest() {
        Set<String> keys = Objects.requireNonNull(data.getConfigurationSection("iforgot-states")).getKeys(false);
        for (String s : keys) {
            if (data.getBoolean("iforgot-states." + s)) {
                return UUID.fromString(s);
            }
        }
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
}
