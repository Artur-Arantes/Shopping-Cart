package br.com.liven.shopping.cart


import org.testcontainers.containers.MySQLContainer
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class ShoppingCartDataBaseContainer extends MySQLContainer<ShoppingCartDataBaseContainer> {
    private static final String MYSQL_VERSION = "mysql:8.0"
    private static final String APP_NAME = "shopping_cart"
    private static ShoppingCartDataBaseContainer container

    private boolean isActive

    static ShoppingCartDataBaseContainer getInstance(boolean isActive = true) {
        if (!container) {
            container = new ShoppingCartDataBaseContainer(isActive)
        }
        return container
    }

    private ShoppingCartDataBaseContainer(boolean isActive) {
        super(MYSQL_VERSION)
        this.isActive = isActive
        withUsername(APP_NAME)
        withDatabaseName(APP_NAME)
        withPassword(APP_NAME)
        withReuse(false)
    }

    @Override
    void start() {
        if (isActive) {
            super.start()
            System.setProperty("DB_URL", container.getJdbcUrl())
            System.setProperty("DB_USER", container.getUsername())
            System.setProperty("DB_KEY", container.getPassword())
        }
    }

    @Override
    void stop() {
    }

    @Override
    void close(){

    }
}
