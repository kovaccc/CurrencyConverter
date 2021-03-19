# CurrencyConverter - Android Application for currency conversion
CurrencyConverter is  android application  that enables the conversion of values from one currency to another according to the current state of exchange rates in The Croatian National Bank (HNB).

## About

Android project that has it's own API that allows communication of the application and HNB Exchange Rate Lookup API available on the web server. The API is based on the REST architecture and it is using Retrofit with method GET to get response from service. The results retrieved are structured in a **JSON** transport format.

**Android version 30.0.2**
## Features
The android app lets you: 
 - Get currencies from REST API 
 - Select the currency from which you want to convert
 - Select the currency you want to convert
 - Enter the value you want to convert
 - Get conversion result
 

## Screenshots
![image](https://user-images.githubusercontent.com/75457058/111833081-eaa35280-88f1-11eb-942a-b169d79390a6.png)
![image](https://user-images.githubusercontent.com/75457058/111833093-f0993380-88f1-11eb-9885-8bf1fd34d37d.png)
![image](https://user-images.githubusercontent.com/75457058/111833104-f727ab00-88f1-11eb-9206-c0b30a45e526.png)

## Permissions

CurrencyConverter requires the following permissions in AndroidManifest.xml: 

-  Internet permission is used for getting response from web service

## Setup
1. Clone the repository
```
https://github.com/kovaccc/CurrencyConverter.git
```
2. Open the project with your IDE/Code Editor
3. Run it on simulator or real Android device 
