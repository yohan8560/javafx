module com.itgroup.www {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;


    opens com.itgroup.www to javafx.fxml;
    exports com.itgroup.www;

    exports com.itgroup.application to javafx.graphics;
    opens com.itgroup.controller to javafx.fxml;
    opens com.itgroup.application to javafx.fxml;
    opens com.itgroup.bean to javafx.base;

}