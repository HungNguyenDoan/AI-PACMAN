module maze.pacman {
    requires javafx.controls;
    requires javafx.fxml;


    opens maze.pacman to javafx.fxml;
    exports maze.pacman;
}