package upt.ac.cti.vvs.amp.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.jaxrs.JerseyDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;


public class DockerClientBuilder {

    private final DockerClientConfig dockerClientConfig;

    private DockerCmdExecFactory dockerCmdExecFactory = null;

    private DockerHttpClient dockerHttpClient = null;

    private DockerClientBuilder(DockerClientConfig dockerClientConfig) {
        this.dockerClientConfig = dockerClientConfig;
    }

    public static DockerClientBuilder getInstance() {
        return new DockerClientBuilder(
                DefaultDockerClientConfig.createDefaultConfigBuilder().build()
        );
    }

    /**
     *
     * @deprecated use {@link #getInstance(DockerClientConfig)}
     */
    @Deprecated
    public static DockerClientBuilder getInstance(DefaultDockerClientConfig.Builder dockerClientConfigBuilder) {
        return getInstance(dockerClientConfigBuilder.build());
    }

    public static DockerClientBuilder getInstance(DockerClientConfig dockerClientConfig) {
        return new DockerClientBuilder(dockerClientConfig);
    }

    /**
     *
     * @deprecated use {@link DefaultDockerClientConfig.Builder#withDockerHost(String)}
     */
    @Deprecated
    public static DockerClientBuilder getInstance(String serverUrl) {
        return new DockerClientBuilder(
                DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .withDockerHost(serverUrl)
                        .build()
        );
    }

    /**
     *
     * @deprecated no replacement, use one of {@link DockerHttpClient}
     */
    @Deprecated
    public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
        return new JerseyDockerCmdExecFactory();
    }

    /**
     * Note that this method overrides {@link DockerHttpClient} if it was previously set
     *
     * @deprecated use {@link #withDockerHttpClient(DockerHttpClient)}
     */
    @Deprecated
    public DockerClientBuilder withDockerCmdExecFactory(DockerCmdExecFactory dockerCmdExecFactory) {
        this.dockerCmdExecFactory = dockerCmdExecFactory;
        this.dockerHttpClient = null;
        return this;
    }

    /**
     * Note that this method overrides {@link DockerCmdExecFactory} if it was previously set
     */
    public DockerClientBuilder withDockerHttpClient(DockerHttpClient dockerHttpClient) {
        this.dockerCmdExecFactory = null;
        this.dockerHttpClient = dockerHttpClient;
        return this;
    }

    public DockerClient build() {
        if (dockerHttpClient != null) {
            return DockerClientImpl.getInstance(
                    dockerClientConfig,
                    dockerHttpClient
            );
        } else if (dockerCmdExecFactory != null) {
            return DockerClientImpl.getInstance(dockerClientConfig)
                    .withDockerCmdExecFactory(dockerCmdExecFactory);
        } else {

            return DockerClientImpl.getInstance(
                    dockerClientConfig,
                    new JerseyDockerHttpClient.Builder()
                            .dockerHost(dockerClientConfig.getDockerHost())
                            .sslConfig(dockerClientConfig.getSSLConfig())
                            .build()
            );
        }
    }
}