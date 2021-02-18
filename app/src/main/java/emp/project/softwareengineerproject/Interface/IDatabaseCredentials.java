package emp.project.softwareengineerproject.Interface;

public interface IDatabaseCredentials {

    String DB_NAME = DATABASE_CREDENTIALS.DB_NAME.getDatabaseCredentials();
    String USER = DATABASE_CREDENTIALS.USER.getDatabaseCredentials();
    String PASS = DATABASE_CREDENTIALS.PASS.getDatabaseCredentials();

    enum DATABASE_CREDENTIALS {
        //Free Mysql Server
        DB_NAME("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12388207"),
        USER("sql12388207"),
        PASS("qWRrDAzAJk");

        //Clever cloud server
        //DB_NAME("jdbc:mysql://bbqkcywrafsgjxlxecta-mysql.services.clever-cloud.com:3306/bbqkcywrafsgjxlxecta"),
        //USER("uf0mceexymqmcdl3"),
        //PASS("cx1yimaV0BM5yTw2IRgj");

        private String db_cred;

        DATABASE_CREDENTIALS(String db_cred) {
            this.db_cred = db_cred;
        }

        public String getDatabaseCredentials() {
            return db_cred;
        }
    }
}
