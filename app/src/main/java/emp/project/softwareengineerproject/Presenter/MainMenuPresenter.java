package emp.project.softwareengineerproject.Presenter;

import android.os.Build;
import android.os.StrictMode;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import emp.project.softwareengineerproject.Interface.EDatabaseCredentials;
import emp.project.softwareengineerproject.Interface.IMainMenu;

public class MainMenuPresenter implements IMainMenu.IMainPresenter {
    private IMainMenu.IMainMenuView view;
    private IMainMenu.IMainService service;

    public MainMenuPresenter(IMainMenu.IMainMenuView view) {
        this.view = view;
        this.service = new MainMenuService();
    }

    @Override
    public void onLogoutButtonClicked(View v) {
        view.goToLoginScreen(v);
    }

    @Override
    public void onInventoryButtonClicked() {
        view.goToInventory();
    }

    @Override
    public void onSalesButtonClicked() {
        view.goToSales();
    }

    @Override
    public void onReportsButtonClicked() {
        view.goToReports();
    }

    @Override
    public void onUsersButtonClicked() {
        view.goToUsers();
    }

    @Override
    public void onSettingsButtonClicked() {
        view.goToSettings();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void directUsernameDisplay() throws SQLException {
        view.displayUsername();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        view.displayNumberOfNotifs(String.valueOf(service.getNumberOfNotifications(dtf.format(now))));
    }

    @Override
    public void onNotificationButtonClicked() {
        view.gotoNotifications();
    }

    private class MainMenuService implements IMainMenu.IMainService {
        private String DB_NAME = EDatabaseCredentials.DB_NAME.getDatabaseCredentials();
        private String USER = EDatabaseCredentials.USER.getDatabaseCredentials();
        private String PASS = EDatabaseCredentials.PASS.getDatabaseCredentials();


        @Override
        public void strictMode() throws ClassNotFoundException {
            StrictMode.ThreadPolicy policy;
            policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
        }

        @Override
        public int getNumberOfNotifications(String date) throws SQLException {
            Connection connection = DriverManager.getConnection(DB_NAME, USER, PASS);
            Statement statement = connection.createStatement();
            String sqlGetNumberOfNotifs = "SELECT COUNT(*) FROM notifications_table WHERE notif_date LIKE " + "'" + date + "%'";
            ResultSet resultSet = statement.executeQuery(sqlGetNumberOfNotifs);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        }
    }
}
