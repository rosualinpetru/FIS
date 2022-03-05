package upt.ac.cti.vvs.amp;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import upt.ac.cti.vvs.amp.docker.DockerClientBuilder;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class HelloController {
    @FXML
    public TextField port;
    @FXML
    public Button healthcheck;
    @FXML
    public Circle healthCheckCircle;
    @FXML
    private Label welcomeText;
    @FXML
    public Button startServer;
    @FXML
    public Button stopServer;
    @FXML
    public Circle serverStateCircle;

    private final int DEFAULT_PORT = 8181;

    private final String DB_CONTAINER_NAME = "/tomdb";

    private final String SERVER_CONTAINER_NAME = "/tom";

    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();

    private boolean existsContainer(String containerName) {
        return dockerClient.listContainersCmd().withShowAll(true).exec().stream().anyMatch(it -> Arrays.asList(it.getNames()).contains(containerName));
    }

    private boolean existsRunningContainer(String containerName) {
        return dockerClient.listContainersCmd().exec().stream().anyMatch(it -> Arrays.asList(it.getNames()).contains(containerName));
    }

    private void uiServerDown() {
        serverStateCircle.setFill(Paint.valueOf("RED"));
        healthCheckCircle.setFill(Paint.valueOf("WHITE"));
        port.setDisable(false);
        stopServer.setVisible(false);
        startServer.setVisible(true);
        startServer.setDisable(false);
        healthcheck.setDisable(true);
    }

    private void uiServerStarting() {
        serverStateCircle.setFill(Paint.valueOf("ORANGE"));
        healthCheckCircle.setFill(Paint.valueOf("WHITE"));
        port.setDisable(true);
        stopServer.setVisible(false);
        startServer.setVisible(true);
        startServer.setDisable(true);
        healthcheck.setDisable(true);
    }

    private void uiServerUp() {
        serverStateCircle.setFill(Paint.valueOf("GREEN"));
        healthCheckCircle.setFill(Paint.valueOf("WHITE"));
        port.setDisable(true);
        stopServer.setVisible(true);
        startServer.setVisible(false);
        startServer.setDisable(false);
        healthcheck.setDisable(false);
    }

    private void changeServerState(ServerState s) {
        switch (s) {
            case UP:
                uiServerUp();
                break;
            case DOWN:
                uiServerDown();
                break;
            case STARTING:
                uiServerStarting();
                break;
        }
        welcomeText.setText("Server state: " + s.name());
    }

    @FXML
    void initialize() {

        if (existsRunningContainer(SERVER_CONTAINER_NAME)) {
            var container = dockerClient.inspectContainerCmd(SERVER_CONTAINER_NAME).exec();

            var portValue = container
                    .getNetworkSettings()
                    .getPorts()
                    .getBindings()
                    .entrySet()
                    .stream()
                    .filter(it -> it.getKey().getPort() != DEFAULT_PORT)
                    .findAny().map(it -> it.getKey().getPort()).orElse(DEFAULT_PORT);

            port.setText(portValue.toString());

            changeServerState(ServerState.UP);
        } else
            changeServerState(ServerState.DOWN);
    }


    public void startServer() {
        changeServerState(ServerState.STARTING);

        Network net;
        var nets = dockerClient.listNetworksCmd().withNameFilter("tom").exec();
        if (nets.size() >= 1) {
            net = nets.get(0);
        } else {
            var r = dockerClient.createNetworkCmd().withName("tom").exec();
            net = dockerClient.listNetworksCmd().withIdFilter(r.getId()).exec().get(0);
        }


        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> {
                            welcomeText.setText("Database staring...");
                            if (!existsRunningContainer(DB_CONTAINER_NAME)) {
                                if (existsContainer(DB_CONTAINER_NAME)) {
                                    dockerClient.removeContainerCmd(DB_CONTAINER_NAME).exec();
                                }
                                var container = dockerClient.createContainerCmd("mysql:8.0.26")
                                        .withPortBindings(PortBinding.parse("3306:3306"))
                                        .withEnv("MYSQL_ROOT_PASSWORD=" + System.getenv("DB_PASS"),
                                                "MYSQL_DATABASE=rhs_tomapp",
                                                "MYSQL_USER=tomapp",
                                                "MYSQL_PASSWORD=" + System.getenv("DB_PASS"))
                                        .withName(DB_CONTAINER_NAME).exec();

                                dockerClient.connectToNetworkCmd().withContainerNetwork(new ContainerNetwork().withNetworkID(net.getId()).withAliases("tomdb")).withNetworkId(net.getId()).withContainerId(container.getId()).exec();

                                dockerClient.startContainerCmd(container.getId()).exec();
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(20),
                        event -> {
                            welcomeText.setText("Server starting...");
                            if (!existsRunningContainer(SERVER_CONTAINER_NAME)) {
                                if (existsContainer(SERVER_CONTAINER_NAME)) {
                                    dockerClient.removeContainerCmd(SERVER_CONTAINER_NAME).exec();
                                }

                                var serverContainer = dockerClient.createContainerCmd("tom:1.0.0")
                                        .withPortBindings(PortBinding.parse(port.getText() + ":" + DEFAULT_PORT))
                                        .withExposedPorts(ExposedPort.parse(port.getText()))
                                        .withEnv("MYSQL_PASS=" + System.getenv("DB_PASS"),
                                                "DB_HOST=tomdb",
                                                "EMAIL_PASS=" + System.getenv("DB_PASS"),
                                                "EMAIL_ADDR=tomapplication.dia@gmail.com")
                                        .withName(SERVER_CONTAINER_NAME).exec();

                                dockerClient.connectToNetworkCmd().withContainerId(serverContainer.getId()).withNetworkId(net.getId()).exec();

                                dockerClient.startContainerCmd(serverContainer.getId()).exec();
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(35),
                        event -> {
                            changeServerState(ServerState.UP);
                        })
        );

        timeline.play();


    }


    public void healthcheck() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + port.getText() + "/tom/healthcheck"))
                .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                healthCheckCircle.setFill(Paint.valueOf("GREEN"));
            } else {
                healthCheckCircle.setFill(Paint.valueOf("RED"));
            }

            welcomeText.setText("Status code: " + response.statusCode());
        } catch (Exception e) {
            healthCheckCircle.setFill(Paint.valueOf("RED"));
            welcomeText.setText("Error!");
            System.out.println(URI.create("http://localhost:" + port.getText() + "/tom/healthcheck"));
        }

    }

    public void stopServer() {

        if (existsRunningContainer(SERVER_CONTAINER_NAME)) {
            dockerClient.stopContainerCmd(SERVER_CONTAINER_NAME.replace("/", "")).exec();
        }

        if (existsRunningContainer(DB_CONTAINER_NAME)) {
            dockerClient.stopContainerCmd(DB_CONTAINER_NAME.replace("/", "")).exec();
        }

        changeServerState(ServerState.DOWN);
    }

}