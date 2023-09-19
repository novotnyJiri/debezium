/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.testing.system.tools.registry.builders;

import static io.debezium.testing.system.tools.kafka.builders.FabricKafkaConnectBuilder.KAFKA_CERT_SECRET;
import static io.debezium.testing.system.tools.kafka.builders.FabricKafkaConnectBuilder.KAFKA_CLIENT_CERT_SECRET;

import io.apicurio.registry.operator.api.model.ApicurioRegistry;
import io.apicurio.registry.operator.api.model.ApicurioRegistryBuilder;
import io.apicurio.registry.operator.api.model.ApicurioRegistrySpecConfigurationKafkaSecurity;
import io.apicurio.registry.operator.api.model.ApicurioRegistrySpecConfigurationKafkaSecurityBuilder;
import io.apicurio.registry.operator.api.model.ApicurioRegistrySpecConfigurationKafkaSecurityTlsBuilder;
import io.debezium.testing.system.tools.ConfigProperties;
import io.debezium.testing.system.tools.fabric8.FabricBuilderWrapper;

public class FabricApicurioBuilder
        extends FabricBuilderWrapper<FabricApicurioBuilder, ApicurioRegistryBuilder, ApicurioRegistry> {

    private static final String DEFAULT_PERSISTENCE_TYPE = "kafkasql";

    protected FabricApicurioBuilder(ApicurioRegistryBuilder builder) {
        super(builder);
    }

    @Override
    public ApicurioRegistry build() {
        return builder.build();
    }

    private static FabricApicurioBuilder base() {
        ApicurioRegistryBuilder builder = new ApicurioRegistryBuilder()
                .withNewMetadata()
                .withName("debezium-registry")
                .endMetadata()
                .withNewSpec()
                .endSpec();

        return new FabricApicurioBuilder(builder);
    }

    public static FabricApicurioBuilder baseKafkaSql(String bootstrap) {
        return base().withKafkaSqlConfiguration(bootstrap);
    }

    public FabricApicurioBuilder withKafkaSqlConfiguration(String bootstrap) {

        ApicurioRegistrySpecConfigurationKafkaSecurity tls = new ApicurioRegistrySpecConfigurationKafkaSecurityBuilder()
                .withTls(
                        new ApicurioRegistrySpecConfigurationKafkaSecurityTlsBuilder()
                                .withKeystoreSecretName(KAFKA_CLIENT_CERT_SECRET)
                                .withTruststoreSecretName(KAFKA_CERT_SECRET)
                                .build())
                .build();

        builder
                .editSpec()
                .withNewConfiguration()
                .withLogLevel(ConfigProperties.APICURIO_LOG_LEVEL)
                .withPersistence(DEFAULT_PERSISTENCE_TYPE)
                .withNewKafkasql()
                .withBootstrapServers(bootstrap)
                .withSecurity(tls)
                .endKafkasql()
                .endConfiguration()
                .endSpec();

        return self();
    }
}
