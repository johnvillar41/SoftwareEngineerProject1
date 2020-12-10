package emp.project.softwareengineerproject.Presenter.UsersPresenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import emp.project.softwareengineerproject.Interface.EDatabaseCredentials;
import emp.project.softwareengineerproject.Interface.IUsers.IUsers;
import emp.project.softwareengineerproject.Model.UserModel;
import emp.project.softwareengineerproject.View.LoginActivityView;
import emp.project.softwareengineerproject.View.UsersView.UsersActivityView;

public class UsersPresenter implements IUsers.IUsersPresenter {
    UserModel model;
    IUsers.IUsersService service;
    IUsers.IUsersView view;
    UsersActivityView context;

    public UsersPresenter(IUsers.IUsersView view, Context context) {
        this.view = view;
        this.service = new UsersService();
        this.model = new UserModel();
        this.context = (UsersActivityView) context;
    }

    @Override
    public void onPageDisplayProfile(final String user_id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressBar();
                    }
                });
                try {
                    final UserModel model = service.getUserProfileFromDB(user_id);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.displayProfile(model);
                        }
                    });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgressBar();
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public void onViewButtonClicked() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<UserModel> userList = service.getUsersListFromDB();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                view.displayPopupUsers(userList);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    public void onAddButtonClicked() {
        view.goToAddPage();
    }

    @Override
    public void onEditAccountButtonClicked(String id, String username, String password, String fullname) {
        if (!view.makeTextViewsEdittable()) {
            try {
                model = new UserModel(id, username, password, fullname);
                if (service.updateNewUserCredentials(model)) {
                    view.displayStatusMessage("Saving... Please Wait");
                    SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivityView.MyPREFERENCES_USERNAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    context.finish();
                    Intent intent = new Intent(context, LoginActivityView.class);
                    context.startActivity(intent);
                } else {
                    view.displayStatusMessage("Error!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class UsersService implements IUsers.IUsersService {

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
        public UserModel getUserProfileFromDB(String user_username) throws ClassNotFoundException, SQLException {
            strictMode();
            Connection connection = DriverManager.getConnection(DB_NAME, USER, PASS);
            String sqlGetUserProfile = "SELECT * FROM login_table WHERE user_username=" + "'" + user_username + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetUserProfile);
            if (resultSet.next()) {
                return new UserModel(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getBlob(5));
            } else {
                return null;
            }
        }

        @Override
        public List<UserModel> getUsersListFromDB() throws ClassNotFoundException, SQLException {
            strictMode();
            List<UserModel> list = new ArrayList<>();
            Connection connection = DriverManager.getConnection(DB_NAME, USER, PASS);
            String sqlSearch = "SELECT * FROM login_table";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSearch);
            while (resultSet.next()) {
                model = new UserModel(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getBlob(5));
                list.add(model);
            }
            return list;
        }

        @Override
        public boolean updateNewUserCredentials(UserModel model) {
            boolean isSuccesfull;
            try {
                Connection connection = DriverManager.getConnection(DB_NAME, USER, PASS);
                String sqlUpdate = "UPDATE login_table " +
                        "SET user_username='" + model.getUser_username() + "',user_password='" + model.getUser_password() + "',user_name='" + model.getUser_full_name() + "'" +
                        "WHERE user_id='" + model.getUser_id() + "'";
                Statement statement = connection.createStatement();
                statement.execute(sqlUpdate);
                isSuccesfull = true;
            } catch (Exception e) {
                isSuccesfull = false;
            }
            return isSuccesfull;

        }
    }
}
