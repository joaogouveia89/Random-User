# Random User Android Project

This project is an Android application that consumes the [Random User API](https://randomuser.me/) to display user information and provide interactive features such as launching Google Maps with user locations and customizing icons based on user nationality.

## Features

- [x] ~~Display user information fetched from the Random User API.~~
- [x] ~~Open Google Maps with the latitude and longitude provided by the API.~~
- [x] ~~Customize icons based on the user's nationality flag.~~
- [x] ~~Swipe-to-refresh: Enable users to refresh the data by swiping down.~~
- [x] ~~Added a json raw interceptor for bug tracing help~~
- [ ] Bottom navigation bar: Implement navigation for different app sections.
- [ ] Add to contacts: Add functionality to save users as contacts, including:
    - [ ] A dedicated screen to display saved contacts.
    - [ ] A sticky letter list for easier navigation through the contact list.
- [ ] Clickable phone numbers: Enable users to initiate calls by tapping on phone numbers.

## Bug fixing

- [x] ~~Crash on user fetching~~

## Screenshots

Add screenshots here to showcase the application’s functionality. Include images for each major feature to give a visual understanding of the app.

## Libraries and Tools

### Plugins
- **Android Application Plugin**: `com.android.application` version 8.7.2
- **Kotlin Android Plugin**: `org.jetbrains.kotlin.android` version 2.0.0
- **Kotlin Compose Plugin**: `org.jetbrains.kotlin.plugin.compose` version 2.0.0

### Dependencies

| Library                 | Version        | Purpose                             |
|-------------------------|----------------|-------------------------------------|
| Android Core KTX        | 1.15.0         | Core Android extensions for Kotlin. |
| Lifecycle Runtime KTX   | 2.8.7          | Lifecycle-aware components.         |
| Activity Compose        | 1.9.3          | Jetpack Compose integration.        |
| Compose BOM             | 2024.04.01     | Compose dependency management.      |
| Material Icons Extended | Latest version | Material icons for Compose.         |
| Coil Compose            | 3.0.4          | Image loading for Compose.          |
| Coil Network (OkHttp)   | 3.0.4          | Network support for Coil.           |
| Retrofit                | 2.9.0          | REST API client.                    |
| Gson Converter          | 2.9.0          | JSON serialization/deserialization. |
| OkHttp                  | 5.0.0-alpha.2  | HTTP client.                        |
| Logging Interceptor     | 5.0.0-alpha.2  | Logging for HTTP requests.          |
| Palette KTX             | 1.0.0          | Extract colors from images.         |
| Kotlinx Datetime        | 0.6.1          | Date and time utilities for Kotlin. |
| JUnit                   | 4.13.2         | Unit testing framework.             |
| AndroidX Test JUnit     | 1.2.1          | Android JUnit integration.          |
| Espresso Core           | 3.6.1          | UI testing framework.               |

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/random-user-android.git
   ```
2. Open the project in Android Studio.
3. Sync the project to download dependencies.
4. Build and run the app on an emulator or physical device.

## API Information
The app fetches user data from the [Random User API](https://randomuser.me/). Below is an example of a typical API request:

```http
GET https://randomuser.me/api/
```

Refer to the [Random User API documentation](https://randomuser.me/documentation) for more details on available endpoints and response formats.

## Contributions

Feel free to submit pull requests to improve features, fix bugs, or implement items on the to-do list. Ensure to follow the existing code style and include proper documentation for any new features.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

