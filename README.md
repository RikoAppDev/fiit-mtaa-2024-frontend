# GrabIT

### Fruit and Vegetable Harvest Coordination App

GrabIT is a mobile application designed for organizing and coordinating the harvest of fruits and
vegetables. Its aim is to connect harvest organizers with pickers and streamline the organization
and registration process for harvesting. This facilitates the dissemination of information about the
date and time of harvest, necessary equipment to bring for harvesting, and the amount to be paid to
pickers.

Organizers can create and manage harvests, while pickers have access to a list of available harvests
they can sign up for. The application offers an interactive map of local harvests, attendance
processing tools for harvest organizers, coordination tools, and real-time updates for pickers
during the harvest, among many other features that simplify and optimize the entire harvesting
process.

## Tech Stack

- Language: Kotlin Multiplatform
- UI Framework: Jetpack Compose Multiplatform

## Project Components

### Mandatory Project Components:

- **User Management (Persistent User Authentication):** Each user must create an account before
  using the application. Authentication upon login is handled using JWT.
- **Support for Offline Mode:** Functionality supported only for harvest organizers. They can manage
  picker attendance at the harvest even without internet access. Data is stored locally and
  synchronized with the database upon reconnection to the network.
- **Support for Real-time Communication (WebSockets/SSE - Server Sent Events):** Organizers can send
  notifications to pickers signed up for the harvest during the event.
- **Support for Phone and Tablet Views:** A separate tablet screen is implemented to display "All
  Harvests", where cards are displayed in rows of 3. On phones, only one card is displayed per row.
- **Support for Dark Mode:** The application supports dark mode on all screens on both devices.
- **Support for Multiple Languages:** The application supports Slovak and English languages.
- **Support for Push Notifications:** The application supports push notifications on the Android
  platform, integration implemented using Firebase Messaging.
- **Support for Permission Handling:** The application requests permission for location usage and
  file access. It responds appropriately to denial of access to these services.

### Optionally Mandatory Project Components:

- **Integration of Firebase Services (Analytics, Crash Reporting):** The application supports
  analytics and crash reporting for the Android platform.
- **Location-based Service - GPS:** The application supports user location tracking with adequate
  permission handling on both platforms. Based on location data, the application can filter events
  located within a specific distance from the user.
- **Integration of Additional Sensor:** Integration of sensors for biometric authentication (Apple
  Face ID, Fingerprint reader).

### GrabIT application

Download application: [here](https://drive.google.com/drive/folders/1sXEF2VHFLxky5UuZlswzMuIf3N_RwECZ?usp=sharing)

### Authors

- [Martin Vančo](https://github.com/vancik01)
- [Frederik Duvač](https://github.com/RikoAppDev)
