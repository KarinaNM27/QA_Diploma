# Процедура запуска авто-тестов

1. Открыть проект в IntelliJ IDEA.
2. Запустить Docker через команду docker-compose up.
3. Запустить приложение через "java -jar aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app"` для mysql либо 
"java -jar aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app" для postgres.
4. Запустить тесты командой "./gradlew clean test -D dbUrl=jdbc:mysql://localhost:3306/app -D dbUser=app -D dbPass=pass" для mysql либо "./gradlew clean test -D dbUrl=jdbc:postgresql://localhost:5432/app -D dbUser=app -D dbPass=pass" для postgres. 
5. Просмотреть отчет в Allure через команду "./gradlew allureServe" либо в Gradle через путь ./build/reports/tests/test/index.html.

