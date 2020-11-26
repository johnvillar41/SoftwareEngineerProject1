package emp.project.softwareengineerproject.Presenter;

import android.os.StrictMode;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.mysql.jdbc.PreparedStatement;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import emp.project.softwareengineerproject.Interface.IUpdateInventory;
import emp.project.softwareengineerproject.Model.ProductModel;

public class InventoryUpdatePresenter implements IUpdateInventory.IUpdatePresenter {
    IUpdateInventory.IUupdateInventoryView view;
    DBhelper dBhelper;
    ProductModel model;

    public InventoryUpdatePresenter(IUpdateInventory.IUupdateInventoryView view) {
        this.view = view;
        this.model = new ProductModel();
        this.dBhelper = new DBhelper();
    }

    @Override
    public void onCancelButtonClicked() {
        view.goBack();
    }


    @Override
    public void onSaveButtonClicked(String product_id,
                                    TextInputLayout editText_productTitle,
                                    TextInputLayout txt_product_description,
                                    TextInputLayout txt_product_Price,
                                    TextInputLayout txt_product_Stocks, InputStream upload_picture, View v) throws SQLException {
        try {
            dBhelper.updateProductToDB(model.validateUpdate(editText_productTitle,txt_product_description,txt_product_Price,txt_product_Stocks,product_id,upload_picture));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void displayHints(ProductModel model) throws SQLException {
        view.setHints(model);
    }

    @Override
    public void ImageButtonClicked() {
        view.loadImageFromGallery();
    }

    private static class DBhelper implements IUpdateInventory.IDbHelper {

        private String DB_NAME = "jdbc:mysql://192.168.1.152:3306/agt_db";
        private String USER = "admin";
        private String PASS = "admin";

        @Override
        public void strictMode() throws ClassNotFoundException {
            StrictMode.ThreadPolicy policy;
            policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
        }

        @Override
        public void updateProductToDB(ProductModel model) throws SQLException, ClassNotFoundException, FileNotFoundException {
            strictMode();
            Connection connection = DriverManager.getConnection(DB_NAME, USER, PASS);
            String sql = "UPDATE greenhouse_products SET product_picture=?" +
                    ",product_name=? WHERE product_id=" + "'" + model.getProduct_id() + "'";
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);

            preparedStatement.setBlob(1,model.getUpload_picture());
            preparedStatement.setString(2, model.getProduct_name());
            preparedStatement.executeUpdate();

            String sqlcmd = "UPDATE greenhouse_products SET product_description=" + "'" + model.getProduct_description() + "'," +
                    "product_price=" + model.getProduct_price() + "," + "product_stocks="  + model.getProduct_stocks() +
                    " WHERE product_id=" + "'" + model.getProduct_id() + "'";
            Statement statement = connection.createStatement();
            statement.execute(sqlcmd);
            statement.close();
            connection.close();

        }
    }
}
