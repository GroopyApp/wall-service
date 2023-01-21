package app.groopy.roomservice.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("app.groopy.providers.elasticsearch.repository")
@ComponentScan(basePackages = {"app.groopy.providers.elasticsearch"})
public class ElasticsearchClientConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private String port;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient ()
    {
        final ClientConfiguration clientConfiguration =
                ClientConfiguration.builder().connectedTo(host, port).build();

        return RestClients.create(clientConfiguration).rest();
    }
}