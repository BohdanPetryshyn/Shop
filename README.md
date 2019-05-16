# shop
Intelliarts Summer Camp Test Project

# What you need?
- Java 8
- Maven
- Git
- MySql

# How to run?
- `git clone https://github.com/BohdanPetryshyn/shop.git`
- Edit shop\src\main\resources\config\application.properties
  - Set `currency.rates.fixer.access_key` property to your [Fixer.io](https://fixer.io) API key.
  - Set `currency.rates.favorite_currencies` property to currencies that you want to cache from the very begining of using this app.(Other currencies you will use will be added to this list automatically).
  - Set `currency.rates.cache_lifetime` property to the time of cache relevance in minutes(60 minutes recomended).
  - Set `dbcp.url` property to your MySql database url.
  - Set `dbcp.username` property to your database username.
  - Set `dbcp.password` property to your database password.
- `cd shop`
- `mvn package`
- `cd target`
- `java -jar shop-1.0-jar-with-dependencies.jar `

# How to use?
- Type `?list` to see the list of avalible commands.
- Type `?help <command name>` to see command description
