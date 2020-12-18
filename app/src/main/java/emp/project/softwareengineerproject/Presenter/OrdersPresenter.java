package emp.project.softwareengineerproject.Presenter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.SQLException;
import java.util.List;

import emp.project.softwareengineerproject.Interface.IOrders;
import emp.project.softwareengineerproject.Model.OrdersModel;
import emp.project.softwareengineerproject.Services.OrdersService;
import emp.project.softwareengineerproject.View.OrdersView.OrdersActivityView;

public class OrdersPresenter implements IOrders.IOrdersPresenter {

    private IOrders.IOrdersView view;
    private OrdersModel model;
    private OrdersActivityView context;
    private OrdersService service;

    public OrdersPresenter(IOrders.IOrdersView view, OrdersActivityView context) {
        this.view = view;
        this.model = new OrdersModel();
        this.context = context;
        this.service = new OrdersService(this.model);
    }

    @Override
    public void onNavigationPendingOrders() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });

                try {
                    final List<OrdersModel> ordersList = service.getOrdersFromDB(STATUS.PENDING.getStatus());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.displayRecyclerView(ordersList);
                            view.hideProgressIndicator();
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
    public void onNavigationFinishedOrders() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });

                try {
                    final List<OrdersModel> ordersList = service.getOrdersFromDB(STATUS.FINISHED.getStatus());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.displayRecyclerView(ordersList);
                            view.hideProgressIndicator();
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
    public void onNavigationCancelledOrders() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });

                try {
                    final List<OrdersModel> ordersList = service.getOrdersFromDB(STATUS.CANCELLED.getStatus());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.displayRecyclerView(ordersList);
                            view.hideProgressIndicator();
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
    public void onMenuPendingClicked(final String order_id) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });
                try {
                    service.updateOrderFromDB(order_id,STATUS.PENDING.getStatus());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgressIndicator();

                    }
                });
            }
        });thread.start();

    }

    @Override
    public void onMenuFinishClicked(final String order_id) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });
                try {
                    service.updateOrderFromDB(order_id,STATUS.FINISHED.getStatus());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgressIndicator();
                    }
                });
            }
        });thread.start();
    }

    @Override
    public void onMenuCancelClicked(final String order_id) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.displayProgressIndicator();
                    }
                });
                try {
                    service.updateOrderFromDB(order_id,STATUS.CANCELLED.getStatus());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgressIndicator();
                    }
                });
            }
        });thread.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addNotification(String title,String content) {
        try {
            service.addNotificationInDB(title,content);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private enum STATUS {
        PENDING("Processing"),
        CANCELLED("Cancelled"),
        FINISHED("Finished");

        private String status;

        STATUS(String status) {
            this.status = status;
        }

        private String getStatus() {
            return status;
        }
    }
}
