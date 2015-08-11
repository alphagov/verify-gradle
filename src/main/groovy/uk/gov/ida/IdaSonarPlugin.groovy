package uk.gov.ida

import org.gradle.api.Plugin
import org.gradle.api.Project

class IdaSonarPlugin implements Plugin<Project> {
	private Project project

	void apply(Project project) {
        this.project = project

		project.apply plugin:'sonar-runner'
		project.repositories {
			maven { url 'https://artifactory.ida.digital.cabinet-office.gov.uk/artifactory/whitelisted-repos' }
		}
        project.extensions.create("idaSonar", IdaSonarPluginExtension)
        project.sonarRunner {
			sonarProperties {
				property 'sonar.projectName', project.idaSonar.name
				property 'sonar.projectKey', project.idaSonar.name
				property 'sonar.host.url', 'http://sonar.ida.digital.cabinet-office.gov.uk:8888'
				property 'sonar.jdbc.url', 'jdbc:postgresql://sonar.ida.digital.cabinet-office.gov.uk/sonar'
				property 'sonar.jdbc.driverClassName', 'org.postgresql.Driver'
				property 'sonar.jdbc.username', 'sonar'
				property 'sonar.jdbc.password', 'holborn missing purple chimney'
				property 'sonar.login', 'admin'
				property 'sonar.password', 'cup cream fair'
				property 'sonar.core.codeCoveragePlugin', 'jacoco'
			}
		}

		project.subprojects {
			apply plugin: 'jacoco'

			sonarRunner {
				sonarProperties {
					property 'sonar.sourceEncoding', 'UTF-8'
					property 'sonar.jacoco.reportPath', "$buildDir/jacoco/test.exec"
					property 'sonar.jacoco.itReportPath', "$buildDir/jacoco/intTest.exec"
				}
			}
		}

		project.configure(skipSonarOnTheseProjects()) {
			sonarRunner {
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

