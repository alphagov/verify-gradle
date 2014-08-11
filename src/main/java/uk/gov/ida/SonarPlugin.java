package uk.gov.ida;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.sonar.runner.SonarRunnerPlugin;

public class SonarPlugin implements org.gradle.api.Plugin<Project> {

    private final String SONAR_HOST_URL = "http://sonar.ida.digital.cabinet-office.gov.uk:8888";
    private final String SONAR_JDBC_URL = "jdbc:postgresql://sonar.ida.digital.cabinet-office.gov.uk/sonar";
    private final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private final String SONAR_JDBC_USERNAME = "sonar";
    private final String SONAR_JDBC_PASSWORD = "holborn missing purple chimney";
    private final String SONAR_LOGIN = "admin";
    private final String SONAR_PASSWORD = "cup cream fair";
    private final String CODE_COVERAGE_PLUGIN = "jacoco";

    @Override
    public void apply(Project project) {

        Action<Task> action = new Action<Task>() {
            @Override
            public void execute(Task task) {
                Project project = task.getProject();
                SonarRunnerPlugin sonarRunnerPlugin = new SonarRunnerPlugin();


                task.setProperty("sonar.projectName", project.getName());
                task.setProperty("sonar.projectKey", project.getName());
                task.setProperty("sonar.jdbc.url", SONAR_JDBC_URL);
                task.setProperty("sonar.host.url", SONAR_HOST_URL);
                task.setProperty("sonar.jdbc.driverClassName", DRIVER_CLASS_NAME);
                task.setProperty("sonar.jdbc.username", SONAR_JDBC_USERNAME);
                task.setProperty("sonar.jdbc.password", SONAR_JDBC_PASSWORD);
                task.setProperty("sonar.login", SONAR_LOGIN);
                task.setProperty("sonar.password", SONAR_PASSWORD);
                task.setProperty("sonar.core.codeCoveragePlugin", CODE_COVERAGE_PLUGIN);
                System.out.println("Calling sonar runner");
                sonarRunnerPlugin.apply(project);
            }
        };
        project.getTasks().getByName("sonar").doFirst(action);
    }
}
