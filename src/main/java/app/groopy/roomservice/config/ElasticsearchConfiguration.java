package app.groopy.roomservice.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("app.groopy.providers.elasticsearch.repository")
@ComponentScan(basePackages = {"app.groopy.providers.elasticsearch"})
@Profile("!dev")
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private String port;
//    @Value("${elasticsearch.username}")
//    private String username;
//    @Value("${elasticsearch.password}")
//    private String password;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient ()
    {
        final ClientConfiguration clientConfiguration =
                ClientConfiguration.builder()
                        .connectedTo(host, port)
//                        .usingSsl()
//                        .withBasicAuth(username, password)
                        .build();

        return RestClients.create(clientConfiguration).rest();
    }
}