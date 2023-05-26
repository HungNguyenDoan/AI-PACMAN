package maze.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MazeGame extends Application {
    private static final int TILE_SIZE = 20;
    private static final int[] robotSTART = {1, 0};
    private int[] humanSTART;

    private int[] end;
    private Rectangle endRect;

    private Group mazeGroup;
    private Entity entity;

    private Entity player;
    
    private Scene startScene, mainScene, endScene;
    
    private Stage window;
    
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        // Đọc ma trận mê cung từ file
        Scanner scanner = new Scanner(new File("map.txt"));
        Solver solver = new Solver();
        solver.setMaze(scanner);
        humanSTART = solver.getPlayerSTART();
        end = solver.getEnd();
        ArrayList<int[]> path = solver.dfs();

        // Tạo đối tượng thực thể và đặt vị trí ban đầu
        entity = new Entity(robotSTART);
        player = new Entity(humanSTART);
        
        
        // Màn chơi mở đầu
        Group startGroup = new Group();
        startScene = new Scene(startGroup, solver.getMaze().get(0).size() * TILE_SIZE, solver.getMaze().size() * TILE_SIZE);
        
        // Add a background image
        Image backgroundImage = new Image("https://wallpaperaccess.com/full/1348293.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        startGroup.getChildren().add(backgroundImageView);
        
        Text title = new Text("Giải mã ma trận");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setFill(Color.WHITE);
        title.setLayoutX(50);
        title.setLayoutY(100);
//        Label startLabel = new Label(title);
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 30;");
        startButton.setLayoutX(150);
        startButton.setLayoutY(150);
        startGroup.getChildren().add(startButton);
        startGroup.getChildren().add(title);
        startButton.setOnAction(e ->  window.setScene(mainScene));
        
        Button exitButton = new Button("Exit  ");
        exitButton.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 30;");
        exitButton.setOnAction(e -> {
            // Close the window
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        });
        exitButton.setLayoutX(150);
        exitButton.setLayoutY(220);
        startGroup.getChildren().add(exitButton);
        
        
        // Tạo màn hình chơi game
        mazeGroup = new Group();
        for (int i = 0; i < solver.getMaze().size(); i++) {
            ArrayList<Integer> row = solver.getMaze().get(i);
            for (int j = 0; j < row.size(); j++) {
                int type = row.get(j);
                Rectangle tile = new Rectangle(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                if (type == 1) {
                    tile.setFill(Color.BLACK);
                } else if (type == 3) {
                    tile.setFill(Color.GREEN);
                    endRect = new Rectangle(end[1] * TILE_SIZE, end[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    tile.setFill(Color.WHITE);
                }
                mazeGroup.getChildren().add(tile);
            }
        }
//        final int counter = 0; // Khởi tạo biến counter với giá trị ban đầu là 0
//        startButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent e) {
//                counter++; // Tăng giá trị của biến counter lên 1 mỗi khi nhấn nút Start
//            }
//        });
        // Thêm thực thể vào màn hình chơi game
        Rectangle entityRect = new Rectangle(robotSTART[1] * TILE_SIZE, robotSTART[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        entityRect.setFill(Color.BLUE);
        mazeGroup.getChildren().add(entityRect);

        //Thêm người chơi vào màn hình game
        Rectangle playerRect = new Rectangle(humanSTART[1] * TILE_SIZE, humanSTART[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        playerRect.setFill(Color.RED);
        mazeGroup.getChildren().add(playerRect);

        // Tạo scene và hiển thị
        mainScene = new Scene(mazeGroup, solver.getMaze().get(0).size() * TILE_SIZE, solver.getMaze().size() * TILE_SIZE);
        window.setScene(startScene);
        window.show();

        // Di chuyển thực thể
        Timeline timeline = new Timeline();
        for (int i = 1; i < path.size(); i++) {
            int[] current = path.get(i);
            int x = current[0];
            int y = current[1];

            KeyValue xKeyValue = new KeyValue(entityRect.xProperty(), y * TILE_SIZE);
            KeyValue yKeyValue = new KeyValue(entityRect.yProperty(), x * TILE_SIZE);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500 * i), xKeyValue, yKeyValue);

            timeline.getKeyFrames().add(keyFrame);

            entity.setPosition(new int[]{x, y});

            //System.out.println(entity.getPosition()[0] + " " + entity.getPosition()[1]);


        }
        timeline.play();

        // Bắt sự kiện phím để di chuyển người chơi
        mainScene.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case UP:
                    player.moveUp(solver.getMaze(), player.getPosition());
                    playerRect.setY(player.getPosition()[0] * TILE_SIZE);
                    double[] human = {playerRect.getX(), playerRect.getY()};
                    double[] robot = {entityRect.getX(), entityRect.getY()};
                    winAlert(human,robot);
                    break;
                case DOWN:
                    player.moveDown(solver.getMaze(), player.getPosition());
                    playerRect.setY(player.getPosition()[0] * TILE_SIZE);
                    human = new double[]{playerRect.getX(), playerRect.getY()};
                    robot = new double[]{entityRect.getX(), entityRect.getY()};
                    winAlert(human,robot);
                    break;
                case LEFT:
                    player.moveLeft(solver.getMaze(), player.getPosition());
                    playerRect.setX(player.getPosition()[1] * TILE_SIZE);
                    human  = new double[]{playerRect.getX(), playerRect.getY()};
                    robot = new double[]{entityRect.getX(), entityRect.getY()};
                    winAlert(human,robot);
                    break;
                case RIGHT:
                    player.moveRight(solver.getMaze(), player.getPosition());
                    playerRect.setX(player.getPosition()[1] * TILE_SIZE);
                    human = new double[]{playerRect.getX(), playerRect.getY()};
                    robot = new double[]{entityRect.getX(), entityRect.getY()};
                    winAlert(human,robot);
                    break;
                default:
                    break;
            }
        });
    }

    private void winAlert(double[] human, double[] robot) {
        if (checkWin(human, robot) == 1) {
            System.out.println("You win!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You win!");
            alert.setHeaderText(null);
            alert.setContentText("You win!");
            alert.setOnHidden(evt -> Platform.exit());
            alert.show();
        }
        if (checkWin(human, robot) == 0){
            System.out.println("Bạn đã thua!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You lose!");
            alert.setHeaderText(null);
            alert.setContentText("You lose!");
            alert.setOnHidden(evt -> Platform.exit());
            alert.show();
        }
        if (checkWin(human, robot) == -1)
            System.out.println("Không có");
    }

    public int checkWin(double[] human, double[] robot) {
        double[] ending = {endRect.getX(), endRect.getY()};
        boolean humanWin = Arrays.equals(human,ending);
        boolean robotWin = Arrays.equals(robot, ending);
        //player đến đích trước cho kết quả 1;
        if (humanWin && !robotWin) {
                return 1;
        }
        //máy đến đích trước cho kết quả 0;
        if (!humanWin && robotWin) {
            return 0;
        }
        //chưa ai tới đích cho kết quả -1
        return -1;
    }

    public static void main(String[] args) {
        launch();
    }
}
