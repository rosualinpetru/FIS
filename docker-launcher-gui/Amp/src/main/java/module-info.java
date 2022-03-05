module Amp {

    requires docker.java.core;
    requires docker.java.api;
    requires docker.java.transport.jersey;
    requires docker.java.transport;

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    requires java.net.http;

    opens upt.ac.cti.vvs.amp;
    exports upt.ac.cti.vvs.amp;
}