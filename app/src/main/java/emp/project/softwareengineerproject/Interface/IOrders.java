package emp.project.softwareengineerproject.Interface;

import java.sql.SQLException;
import java.util.List;

import emp.project.softwareengineerproject.Model.OrdersModel;

public interface IOrders {
    interface IOrdersView {
        void initViews();

        void displayProgressIndicator();

        void hideProgressIndicator();

        void displayRecyclerView(List<OrdersModel> orderList);

    }

    interface IOrdersPresenter {
        void onNavigationPendingOrders();

        void onNavigationFinishedOrders();

        void onNavigationCancelledOrders();

        void onMenuPendingClicked(String order_id);

        void onMenuFinishClicked(String order_id);

        void onMenuCancelClicked(String order_id);

        void addNotification(String title,String content);
    }

    interface IOrdersService extends IServiceStrictMode {
        List<OrdersModel> getOrdersFromDB(String status) throws ClassNotFoundException, SQLException;

        void updateOrderFromDB(String order_id,String status) throws ClassNotFoundException, SQLException;

        void addNotificationInDB(String title,String content) throws ClassNotFoundException, SQLException;
    }
}
