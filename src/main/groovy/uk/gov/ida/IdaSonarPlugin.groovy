package uk.gov.ida

import org.gradle.api.Plugin
import org.gradle.api.Project

class IdaSonarPlugin implements Plugin<Project> {
	private Project project

	void apply(Project project) {
        this.project = project

		project.repositories {
			maven { url 'https://artifactory.ida.digital.cabinet-office.gov.uk/artifactory/whitelisted-repos' }
		}
    project.extensions.create("idaSonar", IdaSonarPluginExtension)
    project.sonarqube {
      properties {
        property 'sonar.projectName', project.idaSonar.name
        property 'sonar.projectKey', project.idaSonar.name
        property 'sonar.host.url', 'http://sonar.ida.digital.cabinet-office.gov.uk:8888'
        property 'sonar.login', 'admin'
        property 'sonar.password', 'cup cream fair'
        property 'sonar.core.codeCoveragePlugin', 'jacoco'
      }
		}

		project.subprojects {
			apply plugin: 'jacoco'

			sonarqube {
				properties {
          property 'sonar.projectName', project.idaSonar.name
          property 'sonar.projectKey', project.idaSonar.name
          property 'sonar.host.url', 'http://sonar.ida.digital.cabinet-office.gov.uk:8888'
          property 'sonar.login', 'admin'
          property 'sonar.password', 'cup cream fair'
          property 'sonar.core.codeCoveragePlugin', 'jacoco'
					property 'sonar.sourceEncoding', 'UTF-8'
					property 'sonar.jacoco.reportPath', "$buildDir/jacoco/test.exec"
					property 'sonar.jacoco.itReportPath', "$buildDir/jacoco/intTest.exec"
				}
			}
		}

		project.configure(skipSonarOnTheseProjects()) {
			sonarqube {
        skipProject = true
			}
		}
	}

	private skipSonarOnTheseProjects() {
        project.idaSonar.projectsToSkip.collect { project.project(it) }
	}
}

class IdaSonarPluginExtension {
    String name
    String[] projectsToSkip
}

