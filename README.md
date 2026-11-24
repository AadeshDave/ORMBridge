# ORMBridge

ORMBridge converts SQL `CREATE TABLE` scripts into annotated Java DTO classes. It can also create the tables in MySQL, and it includes a small ORM and a web page for working with the tables.

## Features

- **SQL to Java generator.** Paste a SQL script and get one Java class per table, with getters, setters, `equals`, `hashCode` and `compareTo`.
- **Annotations.** The generated classes carry `@PrimaryKey`, `@CompositeKey`, `@ForeignKey`, `@Column`, `@MaxLength`, `@Check` and `@Auto` (for `AUTO_INCREMENT` columns).
- **Optional MySQL validation.** The script can be checked against your own MySQL server before generating.
- **Create tables.** Logged-in users can create the tables in their database from the same page.
- **CRUD page.** View, add, edit and delete rows in any table. The page reads the table structure from the database, so it works without generated classes.
- **Small ORM.** `ORMMapping` reads the annotations by reflection. `ORMOperations` runs insert, update and delete with `PreparedStatement`.
- **Login with sessions.** Users log in with their own MySQL account. The login is kept in the server session and is never sent back to the browser.

## Tech

- Java (JDK 24) and Jakarta Servlets / JSP, with custom JSP tags
- Apache Tomcat 11
- JSqlParser 4.9 for parsing SQL
- MySQL with JDBC
- Gson for JSON
- Plain JavaScript (XMLHttpRequest) for the pages

## Screenshots

Add your screenshots to an `images` folder and link them here.

| Generator page | CRUD page |
| --- | --- |
| ![Generator](images/generator.png) | ![CRUD](images/crud.png) |

## How to run

1. Install JDK, Tomcat 11 and MySQL.
2. Create a database for the app.
3. Copy `db.conf.example` to `WEB-INF/db.conf` and fill in your own database details. Never commit the real `db.conf`.
4. Compile the Java sources into `WEB-INF/classes`. Put JSqlParser, Gson and the MySQL connector jars in `WEB-INF/lib`.
5. Copy the project into Tomcat's `webapps/sql-to-java` folder and start Tomcat.
6. Open `http://localhost:8080/sql-to-java/`.

## How it works

1. `SQLScriptParser` uses JSqlParser to read each `CREATE TABLE` into table and column models.
2. `DTOWriter` writes one annotated Java class per table.
3. `ORMMapping` reads a class by reflection, or reads a table's structure from the database metadata, and builds the column and key lists.
4. `ORMOperations` uses those lists to run the SQL with `PreparedStatement`.
5. `CRUDServlet` is the single entry point for the CRUD page. It checks the session first, checks the table name against the database, and returns friendly messages only.

## Security notes

- All values go through `PreparedStatement`.
- Table names are checked against the list from the database, and column names come from the database metadata.
- Every CRUD request checks the session.
- Error details are logged on the server and never shown in the browser.

## Roadmap

- HTTPS for the app
- CSRF token for insert, update and delete
- Unit tests for the parser and the ORM
- A `@Table` annotation for table names that differ from class names
- A SQL console on the CRUD page

## Author

Aadesh Dave
