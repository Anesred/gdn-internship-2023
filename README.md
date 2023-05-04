# Simple http server exposing NBP api's three endpoints

## Requirements

This project requires:
- Intellij
- Maven

## Starting

1. Open project
2. Reload Maven if needed
3. Run HttpServer.java

## Usage

To use the server you can use a web browser or the terminal.

To use browser, simply enter the following into the address bar.

baseUrl: localhost:8080

after baseUrl you can use the following endpoints:

- /average/{code}/{date} - returns average exchange rate of given currency

- /minmaxaverage/{code}/last/{N} - returns minimum and maximum average value of the last N quotations, where 0<N<=255

- /majordifference/{code}/last/{N} - returns the biggest difference between bid and ask rate in the last N quotations

To use the terminal, simply use the "curl" command

ex.: "curl http://localhost:8080/average/gbp/2012-12-12"

## Result

Output should look like shown:

![output](https://github.com/anesred/gdn-internship-2023/blob/main/example.png?raw=true)