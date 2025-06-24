package com.adorsys.webank.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebankReflectionHints implements RuntimeHintsRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(WebankReflectionHints.class);

    // Add all relevant packages for dynamic scanning
    private static final String[] PACKAGES_TO_SCAN = {
        "com.adorsys.webank", // local project
        "de.adorsys.ledgers.postings.impl.converter", // ledgers mappers
        "de.adorsys.webank.bank.api.service.domain", // webank domain models
        "de.adorsys.webank.bank.api.service.mappers" // webank mappers (add for MapStruct)
        // Add more if you discover more packages
    };

    // Add explicit critical external classes for reflection (for classes not found by scanning)
    private static final String[] EXTERNAL_CLASSES_TO_REGISTER = {
        // Flyway
        "org.flywaydb.core.Flyway",
        "org.flywaydb.core.api.configuration.FluentConfiguration",
        // Ledgers mappers
        "de.adorsys.ledgers.postings.impl.converter.LedgerAccountMapperImpl",
        "de.adorsys.ledgers.postings.impl.converter.LedgerMapperImpl",
        "de.adorsys.ledgers.postings.impl.converter.ChartOfAccountMapperImpl",
        "de.adorsys.ledgers.postings.impl.converter.AccountStmtMapperImpl",
        "de.adorsys.ledgers.postings.impl.converter.AccountStmtMapper",
        // Webank domain models
        "de.adorsys.webank.bank.api.service.domain.ASPSPConfigData",
        "de.adorsys.webank.bank.api.service.domain.LedgerAccountModel",
        "de.adorsys.webank.bank.api.service.domain.ClearingAccount",
        // Webank MapStruct mapper implementation (explicit for native)
        "de.adorsys.webank.bank.api.service.mappers.BankAccountMapperImpl"
        // Add more if native image errors mention missing reflection registration
    };
    // NOTE: If native image errors mention other MapStruct mappers, add them here as well.


    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // Register holidays.yml resource for native image
        hints.resources().registerPattern("holidays.yml");
        hints.resources().registerPattern("com/adorsys/webank/mockbank/holidays.yml");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(classLoader);

        // Dynamic package scanning
        for (String pkg : PACKAGES_TO_SCAN) {
            String resourcePattern = "classpath*:" +
                    ClassUtils.convertClassNameToResourcePath(pkg) + "/**/*.class";
            try {
                Resource[] resources = resolver.getResources(resourcePattern);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        String className = metadataReader.getClassMetadata().getClassName();
                        hints.reflection().registerType(TypeReference.of(className),
                                hint -> hint.withMembers(
                                        org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                        org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS,
                                        org.springframework.aot.hint.MemberCategory.DECLARED_FIELDS
                                ));
                    }
                }
            } catch (IOException e) {
                logger.error("Failed to scan package for reflection hints ({}): {}", pkg, e.getMessage(), e);
            }
        }

        // Explicit registration for critical external classes
        for (String className : EXTERNAL_CLASSES_TO_REGISTER) {
            try {
                hints.reflection().registerType(TypeReference.of(className),
                        hint -> hint.withMembers(
                                org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS,
                                org.springframework.aot.hint.MemberCategory.DECLARED_FIELDS,
                                org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                                org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS
                        ));
            } catch (Exception e) {
                logger.warn("Could not register external class for reflection: {} - {}", className, e.getMessage());
            }
        }
    }
}
