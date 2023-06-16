# EcoSorter Android App

## About Our App
Our project aims to tackle the growing Indonesia problem of waste management and disposal by developing a mobile application that classifies waste items and educates users on proper waste management practices. The application leverages image processing and machine learning algorithms to identify and categorize waste materials, followed by providing relevant articles and guidelines to help users dispose of the waste responsibly. By addressing the lack of knowledge and awareness surrounding waste management, we aim to encourage responsible waste disposal behavior, contributing to a cleaner environment.

## Library We Use

| Library name  | Usages        | Dependency    |
| ------------- | ------------- | ------------- |
| [Retrofit2](https://square.github.io/retrofit/) | Request API and convert json response into an object | implementation "com.squareup.retrofit2:retrofit:2.9.0" <br> implementation "com.squareup.retrofit2:converter-gson:2.9.0" |
| [Camera]([https://www.tensorflow.org/lite](https://developer.android.com/jetpack/androidx/releases/camera?hl=id)) | Using Camera and Gallery | implementation "androidx.camera:camera-camera2:1.2.3" |
| [OkHttp](https://square.github.io/okhttp/) | Make a data request to the server | implementation "com.squareup.okhttp3:logging-interceptor:4.9.3" |
| [Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=CjwKCAjwnZaVBhA6EiwAVVyv9N5Jvs6cSYCGlBiY0NPil7uduzHbZ6cCt3wLu5zziuXBaENV6_JYORoC-FEQAvD_BwE&gclsrc=aw.ds) | Local database | implementation "androidx.room:room-ktx:2.4.2" <br> implementation "androidx.room:room-runtime:2.4.2" |
| [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle?hl=id) | Connecting frontend and backend | implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1" <br> implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1" |











