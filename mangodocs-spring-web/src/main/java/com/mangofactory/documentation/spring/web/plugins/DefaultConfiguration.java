package com.mangofactory.documentation.spring.web.plugins;

import com.fasterxml.classmate.TypeResolver;
import com.mangofactory.documentation.spi.DocumentationType;
import com.mangofactory.documentation.spi.service.DefaultsProviderPlugin;
import com.mangofactory.documentation.spi.service.contexts.Defaults;
import com.mangofactory.documentation.spi.service.contexts.DocumentationContextBuilder;
import com.mangofactory.documentation.spring.web.RelativePathProvider;
import com.mangofactory.documentation.spring.web.SpringRequestMappingEvaluator;
import com.mangofactory.documentation.spring.web.scanners.RegexRequestMappingPatternMatcher;

import javax.servlet.ServletContext;

public class DefaultConfiguration implements DefaultsProviderPlugin {

  private final Defaults defaults;
  private final TypeResolver typeResolver;
  private final ServletContext servletContext;

  public DefaultConfiguration(Defaults defaults,
                       TypeResolver typeResolver,
                       ServletContext servletContext) {

    this.servletContext = servletContext;
    this.defaults = defaults;
    this.typeResolver = typeResolver;
  }

  @Override
  public DocumentationContextBuilder create(DocumentationType documentationType) {
    return new DocumentationContextBuilder(documentationType)
            .operationOrdering(defaults.operationOrdering())
            .apiDescriptionOrdering(defaults.apiDescriptionOrdering())
            .apiListingReferenceOrdering(defaults.apiListingReferenceOrdering())
            .additionalIgnorableTypes(defaults.defaultIgnorableParameterTypes())
            .additionalExcludedAnnotations(defaults.defaultExcludeAnnotations())
            .rules(defaults.defaultRules(typeResolver))
            .defaultResponseMessages(defaults.defaultResponseMessages())
            .pathProvider(new RelativePathProvider(servletContext))
            .requestMappingPatternMatcher(new RegexRequestMappingPatternMatcher())
            .typeResolver(typeResolver)
            .requestMappingEvaluator(new SpringRequestMappingEvaluator(new RegexRequestMappingPatternMatcher()));
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}
