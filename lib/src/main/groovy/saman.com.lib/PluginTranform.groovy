package saman.com.lib
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginTranform implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("========================")
        System.out.println("hello gradle plugin!")
        System.out.println("========================")
    }
}