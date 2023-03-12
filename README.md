# Driver App
#### March 10th 2023
#### By **Peter**

## Prerequisite

minSdkVersion -> 21

Gradle build system

# Maps API KEY

1. Before running the project add the maps API key. Go to your local.properties file, then add this line:

```MAPS_API_KEY="AIzaSyBcjeoJvP7zuhdwe3ocMkoPf9ALT7w4Otk"```

## TOC

- [Architecture](#architecture)
- [Flow](#flow)
- [Libraries](#libraries)
- [Extras](#extras)

## Architecture

For this app, I chose to use the MVVM architecture, which provides a simpler and more straightforward way to separate concerns, and followed the SOLID principles to ensure maintainability, scalability, and testability. Since the app only has two activities, the added complexity of Clean Architecture or multi-modular approach may not be necessary. By combining the MVVM architecture and SOLID principles, I was able to create a well-organized and maintainable app that is easy to test and extend.

<p align="center">
  <img src="https://github.com/peter-wachira/TMDBClient/blob/master/diagram.png" width="750"/>
</p>

### Communication between layers

1. The UI layer observes changes in the ViewModel and displays the data accordingly.
2. The ViewModel retrieves data from the Repository and updates the LiveData object.
3. The Repository retrieves data from a Remote Data Source.

### Components

* Presentation Layer: Consists of Activities/Fragments and their corresponding XML layout files.
* ViewModel: Holds the UI-related data and provides data to the View Layer via LiveData.
* Repository: Provides an abstraction layer between the Data layer and the ViewModel.
* Data Layer: Consists of a Remote Data Source that provides data to the Repository.

The App is not organized into multiple modules but follows the same principles of the Model View View-Model pattern.

1. The presentation layer handles the UI work with the logic contained in the **ViewModel**.
2. The UI uses a **LiveData** object from the ViewModel and observes it using the **Observer Pattern**.
3. A ListAdapter handles the actual displaying of the orders. Data over the network is retrieved using
   **retrofit** and **coroutines** to handle background work asynchronously. Additionally, note that
   the ViewModel uses the **viewModelScope** to launch the coroutines while Fragments use the **viewLifeCycleOwner**
   to observe data.
4. The data layer uses the recommended **Repository Pattern** to make the network calls providing an abstraction layer between the ViewModel and the Remote Data Source.

## Flow

**Deliveries screen**

Once the app is launched, the user has the option of choosing a delivery order to fulfill.

**Delivery details Screen**

This screen is accessible from the Deliveries screen to allow driver to see the order details.

**Pause trip screen**

This screen is accessible from the Deliveries screen to allow driver to pause a trip.

## Libraries

This app will make use of the following libraries:

- [Jetpack](https://developer.android.com/jetpack)🚀
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI data to survive configuration changes and is lifecycle-aware
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declaratively bind observable data to UI elements
  - [Navigation](https://developer.android.com/guide/navigation/) - Handle everything needed for in-app navigation
  - [Room DB](https://developer.android.com/topic/libraries/architecture/room) - Fluent SQLite database access
  - [Paging](https://developer.android.com/topic/libraries/architecture/paging) - Load and display small chunks of data at a time
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Notify views when underlying database changes
  - [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - Testing API endpoints
  - [Retrofit](https://square.github.io/retrofit/) - type safe http client with coroutines support
  - [Gson](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back
  - [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - logging HTTP request related data.
  - [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines
  - [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥
  - [Crashlytics](https://firebase.google.com/docs/crashlytics) - Provide Realtime crash reports from users end.
  - [Glide](https://github.com/bumptech/glide) - Hassle-free image loading
  - [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class.


  
#### CI-Pipeline

[Github Actions CI](https://github.com/features/actions/) is used for Continuous Integration every time an update is made
to the repo. The configuration is in the .develop.yml .master.yml .branch.yml*** files

## Contacts

```bash

You can reach me via my personal email pwachira900@gmail.com or my website https://peterwachira.com
