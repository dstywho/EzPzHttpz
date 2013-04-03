

Add this to you pom.xml
-----------------------
    <dependencies>
        .....
        <dependency>
            <groupId>org.calgb.test</groupId>
            <artifactId>PerformanceToolBox</artifactId>
            <version>1.0-SNAPSHOT</version>
            <repositories>
        </dependency>
        ....
    </dependencies>
    ....
    ....
    <repositories>
        ....
        <repository>
            <id>httpz</id>
            <url>https://raw.github.com/dstywho/EzPzHttpz/mvn-repo/snapshots</url>
        </repository>
        ....
    </repositories>




To publish
-------------
mvn -DaltDeploymentRepository=snapshot-repo::default::file:$PWD/snapshots clean deploy -DskipTests
create an orphan branch 
add snapshots dir
commit
