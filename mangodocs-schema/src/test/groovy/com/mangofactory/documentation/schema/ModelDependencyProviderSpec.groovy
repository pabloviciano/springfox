package com.mangofactory.documentation.schema

import com.mangofactory.documentation.schema.mixins.TypesForTestingSupport
import spock.lang.Unroll

import static com.mangofactory.documentation.spi.schema.contexts.ModelContext.*

@Mixin([TypesForTestingSupport, AlternateTypesSupport])
class ModelDependencyProviderSpec extends SchemaSpecification {

  @Unroll
  def "dependencies are inferred correctly" () {
    given:
      def context = inputParam(modelType, documentationType, alternateTypeProvider())
      def dependentTypes = modelDependencyProvider.dependentModels(context)
      def dependentTypeNames = dependentTypes.collect() {
          typeNameExtractor.typeName(inputParam(it, documentationType, alternateTypeProvider()))
        }.unique()
        .sort()

    expect:
     dependencies == dependentTypeNames

    where:
      modelType                       | dependencies
      simpleType()                    | []
      complexType()                   | ["Category"]
      enumType()                      | []
      typeWithLists()                 | ["List", "Category",  "ComplexType"].sort()
      typeWithSets()                  | ["Set", "Category",  "ComplexType"].sort()
      typeWithArrays()                | ["Array", "Category", "ComplexType"].sort()
      genericClass()                  | ["List", "SimpleType"].sort()
      genericClassWithListField()     | ["List", "SimpleType"].sort()
      genericClassWithGenericField()  | ["Charset", "HttpHeaders", "List",
                                         "MediaType", "ResponseEntityAlternative«SimpleType»", "Set",
                                         "SimpleType", "URI", "Map«string,string»"].sort()
      genericClassWithDeepGenerics()  | ["Charset", "HttpHeaders", "List", "MediaType",
                                         "ResponseEntityAlternative«List«SimpleType»»",
                                         "Set", "SimpleType", "URI", "Map«string,string»"].sort()
      genericCollectionWithEnum()     | ["Collection«string»", "List"].sort()
      recursiveType()                 | ["SimpleType"]
  }

  @Unroll
  def "dependencies are inferred correctly for return parameters" () {
    given:
      def context = returnValue(modelType, documentationType, alternateTypeProvider())
      def dependentTypes = modelDependencyProvider.dependentModels(context)
      def dependentTypeNames = dependentTypes.collect() {
            typeNameExtractor.typeName(returnValue(it, documentationType, alternateTypeProvider()))
          }.unique()
          .sort()
    expect:
      dependencies == dependentTypeNames

    where:
      modelType                       | dependencies
      simpleType()                    | []
      complexType()                   | ["Category"]
      enumType()                      | []
      inheritedComplexType()          | ["Category"]
      typeWithLists()                 | ["List", "Category",  "ComplexType" ].sort()
      typeWithSets()                  | ["Set", "Category",  "ComplexType"].sort()
      typeWithArrays()                | ["Array", "Category", "ComplexType"].sort()
      genericClass()                  | ["List", "SimpleType"].sort()
      genericClassWithListField()     | ["List", "SimpleType"].sort()
      genericClassWithGenericField()  | ["Charset", "HttpHeaders", "List",
                                         "MediaType", "ResponseEntityAlternative«SimpleType»",
                                         "Set", "SimpleType", "URI"].sort()
      genericClassWithDeepGenerics()  | ["Charset", "HttpHeaders", "List",
                                         "MediaType", "ResponseEntityAlternative«List«SimpleType»»",
                                         "Set", "SimpleType", "URI"].sort()
      genericCollectionWithEnum()     | ["Collection«string»", "List"].sort()
      recursiveType()                 | ["SimpleType"]
  }



}
