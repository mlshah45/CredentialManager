# CredentialManager
A Credentials Manager android application to store and backup all the login credentials at one place using a dynamic password.

## Overview
A time based 4 digit passcode is used to open the application. So if the current time is 18:32, then passcode to open the application will be 1832.
An option to set a number between 0 - 10 is there using which passcode can be made more random. If a number set is 5 and current time is 18:32 then 5 is added to the time and passcode will be 1837.

Files and folders can be created once logged in. Each file is used to store app name, login id and password of tha app. This data is then persisted for future lookups. It has the options to edit/delete the data.

This application helps to not remember the password for anything at all. Just use the time to access all the ids and passcode for any of the sign in's.

It has the support to take a backup of the data as well.

![Home page](https://cloud.githubusercontent.com/assets/8069263/24841871/e8de35fe-1d53-11e7-9f82-6f6944aacd35.png)

## Technologies used

* Java, Sql, Android Studio
