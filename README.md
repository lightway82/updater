[ ![Codeship Status for lightway82/updater](https://app.codeship.com/projects/3e7c6590-2b47-0135-7e95-4afd89638027/status?branch=master)](https://app.codeship.com/projects/224038)
[![BCH compliance](https://bettercodehub.com/edge/badge/lightway82/updater?branch=master)](https://bettercodehub.com/)

Framework для создания автоматических обновлений в java-приложениях. 
Реализован процесс обновления через xml-файлы. Возможно создание собственных стратегий обновления на его базе.
**Еще не завершен!**

Framework for creating automatic updates in java-applications.
Implemented the update process through xml-files. You can create your own update strategies based on it.
**Not yet completed!**

How to include updater to project:
Add to pom.xml: 
```
<repositories>
        <repository>
            <id>updater_repo</id>
            <url>https://raw.github.com/lightway82/updater_repo/mvn-repo</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>org.anantacreative</groupId>
            <artifactId>updater</artifactId>
            <version>0.10</version>
        </dependency>
    </dependencies>
```