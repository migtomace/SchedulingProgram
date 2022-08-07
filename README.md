# SchedulingProgram


SchedulingProgram is a Desktop Application developed using Java 8, JavaFX framework, and MySQL. This application primarily consists of Java and FXML files. The purpose of this application is to provide global consultants the ability to schedule, edit, view and delete appointments with their clients.

## Login/Logout

Consultants are able to login upon application startup, where they are set to active in the database. The users information is saved in static variables stored in "UserSession.java". Once they logout of the system, they are set to inactive in the database.

## Create Account

If they don't have an account they can press the link supplied on the login page which will redirect them to the appropriate form. When the form is completed they will be logged in upon successful account creation.

## Appointments

Upon login consultants are alerted if they have an appointment within 15mins of their login time. Also provided is a table of their next 7 days of appointments. The table has the ability to switch to a 30 day view as well. They can select their appointment and view, edit and delete the information for the appointment. Click on the "Create" button to add a new appointment. The Add Appointment View allows the consultant to search company wide clients by their name, automatically adding it to the form.

## Customers

Navigate to the Customers Scene to view, create, and edit company wide clients. Deleting clients requires admin authentication, which is currently implemented through an alert. The implementation of accessing an administrative code or password would need to be implemented before the application was deployed for customer use.

## Dates/Times

You will find a clock on both the Login and Create account Scenes. These clocks use the LocalDateTime which displays the system default of the user. Appointment times are created as LocalDateTime as well, however, they are converted to ZonedDateTime before they are then converted into UTC Timestamps for the MySQL Database. This is important as this is a global application that will be used in different timezones, having all appointment times inserted or updated in the database as UTC allows us to convert these dates into any timezone when displayed at a later time.

## Alerts

JavaFX alerts are used throughout the application an can be altered in "Controller/MyAlerts.java". Alerts are used on forms to provide the user with helpful feedback. Alerts are also used to let the user know when a successful transaction has been made with database, such as adding an appointment or customer.

## Reports

There are three reports Monthly (not 30 day) reports provided. The consultant can generate a report of Appointments by the Type of appointments, which allows them to see how many (type: " ") appointments they have in the current month. They also have the capability to view the schedule for the current month, where they can view the date, times, title, customer, and appointment ID. The third report allows them to view what customers they are meeting with this month. They are provided with the Customer ID, name and contact number.

# Set Up and Run Application for Maintenance Purposes

## Prerequisites 
These will all need to be installed:
- Java SDK
- JavaFX sdk-18.0
- MySQL Connector: mysql-connector-java-8.0.21

## Installation and Setup
- Java 8 SDK - Find the correct version at “https://www.oracle.com/java/technologies/downloads/”, download and install. Then configure:
  - File > Project Structure > Project Settings > Project
    - SDK - set this to the SDK location
    - Language Level - set this to 8
  - File > Project Structure > Platform Settings > SDK
    - Add the path to the SDK here
  - File > Project Structure > Modules > Dependencies
  - Set the SDK in the Build and Run configurations as well.
- JavaFX 18 - Follow the instructions at “https://openjfx.io/openjfx-docs/#install-javafx” to install and configure correctly. 
  - This will need to be configured as a library by going to File > Project Structure > Libraries and adding it as a library.
  - And a dependency by going to File > Project Structure > Modules > Dependencies and adding it as a dependency
- The MySQL connector is located in the libs folder:
  - This will need to be configured as a library by going to File > Project Structure > Libraries and adding it as a library.
  - And a dependency by going to File > Project Structure > Modules > Dependencies and adding it as a dependency
  - Run from IntelliJ Build Environment

