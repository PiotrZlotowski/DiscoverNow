package com.discover.server.architecture

import com.discover.server.ServerApplication
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service


@AnalyzeClasses(packagesOf = [ServerApplication::class])
class ArchitectureTests {

    @ArchTest
    val classes_that_are_in_configuration_package_should_be_annotated_with_configuration_related_annotations =
            classes().that().resideInAPackage("..configuration..")
                    .should().beAnnotatedWith(org.springframework.context.annotation.Configuration::class.java)
                    .orShould().beAnnotatedWith(EnableWebSecurity::class.java)
    @ArchTest
    val classes_that_are_annotated_with_controller_should_not_be_injected_to_different_stereotypes =
            noClasses().that().areAnnotatedWith(Service::class.java)
                    .or().areAnnotatedWith(Repository::class.java)
                    .or().areAnnotatedWith(Component::class.java)
                    .should().accessClassesThat().areAnnotatedWith(Controller::class.java)

    @ArchTest
    val project_structure_should_be_divded_into_package_by_feature_instead_of_package_by_layer =
            noClasses().that().arePublic()
                    .or().areNotPublic()
                    .should().resideInAnyPackage("..controller..","..service..", "..repository..", "..dto..")

}
