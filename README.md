# Drombler ACP

*Drombler Abstract Client Platform (ACP)* is an abstract, GUI-toolkit agnostic, modular Rich Client Platform based on:

 * **OSGi**: *OSGi* is the de facto standard for writing modular software in *Java*.
 * **Maven (POM-first)**: *Drombler ACP* applications can build with *Maven*. The build follows the standard POM-first approach (the OSGi meta data will be generated for you).
 * **Declarative programming model**: Annotations can be used at many places to register elements such as menus, toolbars and GUI components.
 * **Drombler Commons**: *Drombler Commons* is a collection of reusable libraries and frameworks. They ship with OSGi meta data but don't require an OSGi environment. Read more about *Drombler Commons* here: [Drombler Commons](https://github.com/Drombler/drombler-commons)

See the [documentation](https://www.drombler.org/drombler-acp) for the Javadoc and information about the provided Maven modules (available from Maven Central).

## Implementations

 * [Drombler FX](https://github.com/Drombler/drombler-fx) (*JavaFX*-based)

## Issues
Please file issues to [Drombler FX](https://github.com/Drombler/drombler-fx/issues) for the time being.

## Build the project from sources
```bash
mvn clean install
```
Please note that the develop branch (SNAPSHOT version) of the project might depend on SNAPSHOT versions of other projects.

If you don't want to build the dependent projects as well, please make sure to define a proxy in your [Maven Repository Manager](https://maven.apache.org/repository-management.html) to the following Maven Repository: https://oss.sonatype.org/content/repositories/snapshots/ and include it in your [single group](https://help.sonatype.com/repomanager3/formats/maven-repositories#MavenRepositories-ConfiguringApacheMaven).
