package rarityeg.harmonyauthsmart;

import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class DBDataManager implements IDataManager {
    static String db_url;
    static String username;
    static String password;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveAll() {
    }

    @Override
    public void loadAll() {
        FileConfiguration fc = HarmonyAuthSMART.instance.getConfig();
        String port = fc.getString("mysql.port");
        username = fc.getString("mysql.username");
        String db_name = fc.getString("mysql.db-name");
        password = fc.getString("mysql.password");
        if (port == null || username == null || db_name == null || password == null) {
            HarmonyAuthSMART.dbError = true;
            HarmonyAuthSMART.instance.getLogger().warning("数据库配置不完全，将改用备用存储方式。");
            return;
        }
        db_url = "jdbc:mysql://localhost:" + port + "/" + db_name + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS harmony_auth_data(UUID VARCHAR(255) PRIMARY KEY NOT NULL, PwdHash TEXT NOT NULL, IForgotState BOOLEAN NOT NULL, IForgotReason LONGTEXT NOT NULL, NewPwdHash TEXT NOT NULL, LastLogin TEXT NOT NULL);");
            statement.close();
            connection.close();
            HarmonyAuthSMART.instance.getLogger().info("成功与数据库建立连接！");
        } catch (SQLException e) {
            putError(e);
        }
    }

    private void putError(Exception e) {
        HarmonyAuthSMART.dbError = true;
        HarmonyAuthSMART.instance.getLogger().warning("数据库操作失败，将改用备用存储方式。");
        e.printStackTrace();
    }

    @Nonnull
    @Override
    public String getPasswordHash(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PwdHash FROM harmony_auth_data WHERE UUID=?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            String res = rs.getString("PwdHash");
            preparedStatement.close();
            connection.close();
            return Objects.requireNonNullElse(res, "");
        } catch (SQLException e) {
            putError(e);
            return "";
        }
    }

    @Override
    public boolean getIForgotState(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT IForgotState FROM harmony_auth_data WHERE UUID=?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            boolean res = rs.getBoolean("IForgotState");
            preparedStatement.close();
            connection.close();
            return Objects.requireNonNullElse(res, false);
        } catch (SQLException e) {
            putError(e);
            return false;
        }
    }

    @Nonnull
    @Override
    public String getIForgotManualReason(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT IForgotReason FROM harmony_auth_data WHERE UUID=?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            String res = rs.getString("IForgotReason");
            preparedStatement.close();
            connection.close();
            return Objects.requireNonNullElse(res, "");
        } catch (SQLException e) {
            putError(e);
            return "";
        }
    }

    @Nonnull
    @Override
    public String getIForgotNewPasswordHash(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT NewPwdHash FROM harmony_auth_data WHERE UUID=?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            String res = rs.getString("NewPwdHash");
            preparedStatement.close();
            connection.close();
            return Objects.requireNonNullElse(res, "");
        } catch (SQLException e) {
            putError(e);
            return "";
        }
    }

    @Nonnull
    @Override
    public Date getLastLoginTime(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT LastLogin FROM harmony_auth_data WHERE UUID=?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            String res = rs.getString("LastLogin");
            preparedStatement.close();
            connection.close();
            String dateString = Objects.requireNonNullElse(res, "1970-01-01 23:59:59");
            return sdf.parse(dateString);
        } catch (SQLException e) {
            putError(e);
            try {
                return sdf.parse("1970-01-01 23:59:59");
            } catch (ParseException e2) {
                HarmonyAuthSMART.instance.getLogger().warning("这不可能！不可能出现这个错误！日期的读取失败了？");
                e2.printStackTrace();
                return new Date();
            }
        } catch (ParseException e) {
            HarmonyAuthSMART.instance.getLogger().warning("这不可能！不可能出现这个错误！日期的读取失败了？");
            e.printStackTrace();
            return new Date();
        }
    }

    @Override
    public void setPasswordHash(UUID id, String hash) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO harmony_auth_data (UUID, PwdHash, IForgotState, IForgotReason, NewPwdHash, LastLogin) VALUES (?, ?, false, '', '', '1970-01-01 23:59:59') ON DUPLICATE KEY UPDATE PwdHash=?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, hash);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            putError(e);
        }
    }

    @Override
    public void setIForgotState(UUID id, boolean state) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO harmony_auth_data (UUID, PwdHash, IForgotState, IForgotReason, NewPwdHash, LastLogin) VALUES (?, '', ?, '', '', '1970-01-01 23:59:59') ON DUPLICATE KEY UPDATE IForgotState=?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.setBoolean(2, state);
            preparedStatement.setBoolean(3, state);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            putError(e);
        }
    }

    @Override
    public void setIForgotManualReason(UUID id, String reason) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO harmony_auth_data (UUID, PwdHash, IForgotState, IForgotReason, NewPwdHash, LastLogin) VALUES (?, '', false, ?, '', '1970-01-01 23:59:59') ON DUPLICATE KEY UPDATE IForgotReason=?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.setString(2, reason);
            preparedStatement.setString(3, reason);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            putError(e);
        }
    }

    @Override
    public void setIForgotNewPasswordHash(UUID id, String hash) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO harmony_auth_data (UUID, PwdHash, IForgotState, IForgotReason, NewPwdHash, LastLogin) VALUES (?, '', false, '', ?, '1970-01-01 23:59:59') ON DUPLICATE KEY UPDATE NewPwdHash=?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, hash);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            putError(e);
        }
    }

    @Override
    public void setLastLoginTime(UUID id, Date date) {

        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO harmony_auth_data (UUID, PwdHash, IForgotState, IForgotReason, NewPwdHash, LastLogin) VALUES (?, '', false, '', '', ?) ON DUPLICATE KEY UPDATE LastLogin=?;");
            preparedStatement.setString(1, id.toString());
            String dateString = sdf.format(date);
            preparedStatement.setString(2, dateString);
            preparedStatement.setString(3, dateString);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            putError(e);
        }

    }

    @Override
    public boolean isExist(UUID id) {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(UUID) FROM harmony_auth_data WHERE UUID=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id.toString());
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            int res = rs.getInt(1);
            preparedStatement.close();
            connection.close();
            return res != 0;
        } catch (SQLException e) {
            putError(e);
            return false;
        }
    }

    @Override
    public UUID getNextRequest() {
        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rsc = statement.executeQuery("SELECT COUNT(UUID) FROM harmony_auth_data WHERE IForgotState=true LIMIT 1");
            rsc.first();
            if (rsc.getInt(1) == 0) {
                return UUID.fromString("00000000-0000-0000-0000-000000000000");
            }

            ResultSet rs = statement.executeQuery("SELECT UUID FROM harmony_auth_data WHERE IForgotState=true LIMIT 1");
            rs.first();
            String uString = rs.getString("UUID");
            statement.close();
            connection.close();
            return UUID.fromString(uString);
        } catch (SQLException e) {
            putError(e);
            return UUID.fromString("00000000-0000-0000-0000-000000000000");

        }
    }
}
