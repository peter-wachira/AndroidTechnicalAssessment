

# Driver App
#### March 6th 2023
#### By **peteHack**


## Prerequisite

minSdkVersion -> 21

Gradle build system

# Maps API KEY

Before running the project add the maps API key. Go to your gradle.properties file, then add this line:

```MAPS_API_KEY="AIzaSyBcjeoJvP7zuhdwe3ocMkoPf9ALT7w4Otk"```


## TOC

- [Architecture](#architecture)
- [Flow](#flow)
- [Libraries](#libraries)
- [Extras](#extras)

## Architecture

### Implemented using Clean Architecture
The following diagram shows the structure of this project with 3 layers:
- Presentation
- Domain
- Data

<br>
<p align="center">
  <img src="https://github.com/peter-wachira/TMDBClient/blob/master/diagram.png" width="750"/>
</p>
<br>

### Communication between layers

1. UI calls method from ViewModel.
2. ViewModel executes Use case.
3. Each Repository returns data from a Data Source (Remote).
4. Information flows back to the UI where we display the Deliveries.



The App is not organized into multiple modules but follows the same principles of
the Presentation, Domain, and Data Layers.
The presentation layer handles the UI work with the logic contained in the **ViewModel**.
The UI uses a **LiveData** object from the ViewModel and observes it using the **Observer Pattern**.
A ListAdapter handles the actual displaying of the news. Data over the network is retrieved using
**retrofit** and **coroutines** to handle background work asynchronously. Additionally, note that
the ViewModel uses the **viewModelScope** to launch the coroutines while Fragments use the **viewLifeCycleOwner**
to observe data.
The data layer uses the recommended **Repository Pattern** to make the network calls.


## Flow

**Deliveries screen**

Once the app is launched, the user has the option of choosing a delivery order to fulfill.

**Delivery Details Screen**

This screen is accessible from the Deliveries screen to allow Driver to see the order details.


**Pause Trip Screen**

This screen is accessible from the Deliveries screen to allow Driver to Pause a Trip.

## Libraries

This app will make use of the following libraries:

- [Jetpack](https://developer.android.com/jetpack)🚀

    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI data to survive configuration changes and is lifecycle-aware
    - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declaratively bind observable data to UI elements
    - [Navigation](https://developer.android.com/guide/navigation/) - Handle everything needed for in-app navigation
    - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Manage your Android background jobs
    - [Room DB](https://developer.android.com/topic/libraries/architecture/room) - Fluent SQLite database access
    - [Paging](https://developer.android.com/topic/libraries/architecture/paging) - Load and display small chunks of data at a time
    - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Notify views when underlying database changes

- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - Testing API endpoints
- [Retrofit](https://square.github.io/retrofit/) - type safe http client with coroutines support
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back
- [Dagger2](https://github.com/google/dagger) - A fast dependency injector for Android and Java.
- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - logging HTTP request related data.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines
- [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥
- [Firebase](https://firebase.google.com/) - Backend As A Service for faster mobile development.
- [Crashlytics](https://firebase.google.com/docs/crashlytics) - Provide Realtime crash reports from users end.
- [Glide](https://github.com/bumptech/glide) - Hassle-free image loading
- [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class.




#### CI-Pipeline

[Github Actions CI](https://github.com/features/actions/) is used for Continuous Integration every time an update is made
to the repo. The configuration is in the .develop.yml .master.yml .branch.yml*** files


## Contacts

```bash

You can reach me via my personal email pwachira900@gmail.com or my website https://peterwachira.com

