module Connect4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swt;

    opens core;
    opens ui;
    opens Server;
    opens Client;
}