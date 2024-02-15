# Дипломный проект по профессии «Тестировщик»

## Предварительные требования
1. **Установить Docker Toolbox**
2. **Запустить Docker Quickstart Terminal**
3. **Открыть проект в IntelliJ IDEA**

По умолчанию проект настроен на работу с базой данных MySQL

## Подготовка к тестированию
1. Запустить Docker Quickstart Terminal
2. Открыть проект в Intellij IDEA
3. Получить данные и настроить проект в Intellij IDEA под работу с базой данных по ip-адресу 192.168.99.100
4. Загрузить проект на github
5. Перейти в Docker Quickstart Terminal
6. Клонировать проект:
   ```
   git clone https://github.com/AsaulkaKsenia/DiplomBuyingTour
   ```
7. Перейти в созданный каталог:
   ```
   cd DiplomBuyingTour
   ```
8. Создать и запустить контейнеры:
   ```
   docker-compose up --build
   ```
9. Убедиться в успешном запуске контейнеров   
10. Запустить SUT во вкладке Terminal Intellij IDEA:
   ```
   java -jar artifacts/aqa-shop.jar
   ```
11. Убедиться в успешном запуске SUT

## Проведение тестирования
1. Запустить автотесты:
   ```
   ./gradlew clean test allureReport -Dheadless=true
   ```
2. Для просмотра отчета Allure в терминале ввести команду:
   ```
   ./gradlew allureServe
   ```

## Работа с БД PostgreSQL
## Подготовка к тестированию
1. Запустить SUT во вкладке Terminal Intellij IDEA:  
      ```
      java -D:spring.datasource.url=jdbc:postgresql://185.119.57.164:5432/base_postgresql -jar artifacts/aqa-shop.jar
      ```
2. Убедиться в успешном запуске SUT

## Проведение тестирования
1. Запустить автотесты:  
      
      ```
      ./gradlew clean test allureReport -Dheadless=true -Ddatasource=jdbc:postgresql://185.119.57.164:5432/base_postgresql
      ```
     *или в файле проекта build.gradle в разделе test закомментировать строку* 
     - "systemProperty 'datasource', System.getProperty('datasource', 'jdbc:mysql://192.168.99.100:3306/base_mysql')"
     
     *и раскомментировать строку* 
     - "systemProperty 'datasource', System.getProperty('datasource', 'jdbc:postgresql://192.168.99.100:5432/base_postgresql')"
     
     Далее в Terminal Intellij IDEA ввести команду:
     ```
     ./gradlew clean test allureReport -Dheadless=true
     ```

2. Для просмотра отчета Allure в терминале ввести команду:
   ```
   ./gradlew allureServe
   ```

