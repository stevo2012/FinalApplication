# Home Energy Application

## Introduction
This application was created to help individuals and contractors guage basic load requirements and estimate cost amounts of houses, condos, apartments, 
and smaller units to assist in understanding what loads and costs are associated with all electrical versus electrical/gas energy solutions.
As of now this application exists as an Android App but is only available if loaded from Android Studio which can be downloaded at https://developer.android.com/studio.

## General Information
By utilizing an internal database of common large appliances and their electical loads with and without gas, the application allows for the user to enter select the number of each appliance in their home.
The app will also prompt the user to enter in the city and state they live in which makes an API call to the National Renewable Energy Labs database of current utility rates. Using this information the app will give the user a monthly cost estimate of their home based upon their inputs as well as the loads on their breakers.

This application is built mainly in Java with calls to the local database using SQLite. 

## Getting Started
In order to run this application as of now, the user will need to open it in Android Studio by downloading the programs main folder and opening it in the latest version of Android Studio (Chipmunk). From here the user can run the program on a local emulator or download it to their own android device.
Future versions of this application are intended to be available on the Google Play store. 

## Problems, Bugs, and Future Improvements
As of now, testing the basic framework of the application has allowed any obvious bugs to be worked out, though further testing is requried to ensure this remains true. One major issue is if the user intends to use the application without internet, to which the API call will fail and the application will not be able to provide any data.
Future versions of the software will allow for an offline mode, allowing the user to enter in their own utility rates as a substitution for utilizing the NREL database.
Future versions also intend to encorporate natural gas rate, not just propane (propane is the only availble gas where the developer lives), and also add an API call for current local rates for natural gas, or propane if they become available. 
