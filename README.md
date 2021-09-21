<h1 align="start">kakao-search</h1>

<p align="start">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://github.com/beomjo/kakao-search/actions/workflows/android.yml"><img alt="Build Status" src="https://github.com/beomjo/kakao-search/actions/workflows/android.yml/badge.svg"/></a>
  <a href="https://ktlint.github.io/"><img alt="ktlint" src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg"/></a>
</p>


<p align="start">
   Pageation was implemented using the Kakao search API, </br>
  and examples of search, alignment, separator, and bookmark functions can be viewed.
</p>


## Clone Project
```
$ git clone --recursive https://github.com/beomjo/kakao-search.git
```

## API
API from https://developers.kakao.com/  
in your local.properties

```
REST_KEY={YOUR_KEY}
```  

## Download
-

<img src="https://user-images.githubusercontent.com/39984656/133936547-9751f4d6-e336-438b-b5ee-2238b36edc58.gif" align="right" width="50%"/>    

## Tech stack & Open-source libraries
- Minimum SDK level 21
- **Coroutines, Flow** for asynchronous.
- **LiveData** - notify domain layer data to views.
- **Lifecycle** - dispose of observing data when lifecycle state changes.
- **ViewModel** - UI related data holder, lifecycle aware.
- **Room** - Storing data in local database
- **Paging3** - Load and display data pages of large data sets from local storage or over a network
- **Dagger Hilt** - Dependency Injection
- **Navigation Component** - Move implementation between Fragment and Activity simply and reliably
- **Glide** - loading images
- **Bindables** - DataBinding kit for notifying data changes from Model layers to UI layers.
- **Material-Components** - Material design components like ripple animation, cardView.
- **Testing**
  - **kotest**
  - **mockk**


## Architecture
- Clean Architecture
- MVVM Architecture (View - DataBinding - ViewModel - Model)

## MAD STORE
![summary](https://user-images.githubusercontent.com/39984656/133936190-2808b53b-1fea-4729-bc44-55aebe845ea5.png)
![kotlin](https://user-images.githubusercontent.com/39984656/133936194-ce21ad52-70c9-4a61-be71-c62e748468e3.png)
![jetpack](https://user-images.githubusercontent.com/39984656/133936197-cbfefd7d-51bc-48d1-8cba-ac983b4fe44f.png)


## LICENSE
```xml
Designed and developed by 2021 beomjo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.