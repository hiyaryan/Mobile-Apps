# Assign6_7rmenese1

## Description
This repository is for SER423 semester long mobile projects, AndroidApp and iOSApp, using the instructor provided JsonRPC server, PlaceJsonRPCServer.

## Projects
### AndroidApp

#### Specs
- macOS High Sierra 10.13.6
- Android Studio Arctic Fox 2020.3.1
- Java 14.0.1
- Nexus 5 API 23

#### Description
This project is for the Android mobile app. The app demonstrates three requirements in parallel, saving app data locally by file, by database, and saving it on a server. There are two places where the places.json data is stored: in the local storage on the device (file and database) and on the PlaceJsonRPCServer. If the server is not running, the app will still work but the data will only be saved locally. When the app is run for the first time, there is a places.json file bundled with it, this is used to demonstrate the first requirement, manipulating the app model locally; it is not used when accessing data on the server. There is no difference between running the app via a file/database system vs using a server, however, the data accessible by the user will vary as different data repositories are accessed. 

#### Data repositories
- Local Storage: This is where the "file-version" and "SQLite-version" of the app stores data
- PlaceJsonRPCServer: http://127.0.0.1:8080

#### STARTUP
1. Spin-up the server: navigate to PlaceJsonRPCServer execute `java -jar lib/placeserver.jar 8080`
2. Open the AndroidApp in Android Studio and start a new simulation

#### HOWTO
- To get a place on the home page (Place Description View), 
    1. Tap on the spinner (the downward arrow above the "Name" button).
    2. Select an item from the spinner.
    3. Note: The test button on the main view presents the Life Cycle methods in the console as required by Lab Exercise 2: Life-cycles

- To add a new place to places
    1. On the home page (Place Description View) tap the "Name" button to navigate to the Services View
    2. On the Services View select Add Place to navigate to the Add View
    3. On the Add View, fill out the Edit Text fields ("Name" is a required field) and press submit
    4. On the home page, click on the spinner and select the newly added place to view. 
    5. Note: The Clear button on the Add View clears all Edit Text fields

- To delete a place
    1. On the home page (Place Description View) tap the "Name" button to navigate to the Services View
    2. On the Services View select Remove <Name>
    3. Select OK to delete the place or Cancel to navigate back to the main page.

- To modify a place
    1. On the home page (Place Description View) tap the "Name" button to navigate to the Services View
    2. On the Services View select Modify <Name>
    3. On the Modify View, fill out the Edit Text fields ("Name" is a required field) and press submit
    4. On the home page, click on the spinner and select the place that was modifed to view changes. 
    5. Note: The Clear button on the Modify View clears all Edit Text fields

- To find the Great Circle distance and Initial Bearing between to places
    1. On the home page (Place Description View) tap the "Name" button to navigate to the Services View
    2. On the Services View select Find Distance
    3. On the Calc Distance View the first spinner is the "from place"
    4. On the Calc Distance View the second spinner is the "to place"
    5. The distance and bearing are automatically calculated when either spinner changes the selected item

### iOSApp

#### Specs
- macOS High Sierra 10.13.6
- Xcode 10.1
- Swift 4.1.2
- java 14.0.1
- iPhone 8 (recommended)

#### Description
This project directory is for the iOS mobile app. The app demonstrates two requirements in parallel, saving app data locally and saving it on a server. There are two places where the places.json data is stored: in the Caches directory and on the PlaceJsonRPCServer. If the server is not running, the app will still work but the data will only be saved locally. When the app is run for the first time, there is a places.json file bundled with it, this is used to demonstrate the first requirement.

#### Data repositories
- Caches directory: (below is an example of where the data will be stored in the Caches directory—this will be printed to the console for the user to view it's location)
/Users/Ryan/Library/Developer/CoreSimulator/Devices/F0C3EBDD-CA06-4237-933E-0F70E66CD5A3/data/Containers/Data/Application/80F02512-1EFD-4CA1-BA7E-2B4E4C1AEACE/Library/Caches/places.json

- PlaceJsonRPCServer: http://127.0.0.1:8080

#### STARTUP
1. Spin-up the server: navigate to PlaceJsonRPCServer execute `java -jar lib/placeserver.jar 8080`
2. Open the iOSApp in Xcode and start a new simulation

#### HOWTO
- To get a place on the home page (Place Description View), 
    1. Tap on the name (this is a disguised button) next to the name label
    2. On the Place Listings View tap on a cell in the table and press select

- To add a new place to places
    1. On the home page (Place Description View) tap the '+' button on the top right of the navigation bar
    2. Fill in the data and press save—this will navigate the user to the Place Listings View
    3. Find the newly added place and select to view on the home page (Place Description View)

- To delete a place
    1. Tap on the name next to the name label to navigate to the Place Listings View
    2. Swipe left on any cell to delete the place
    
- To update the description of a place
    1. On the home page  (Place Description View) tap description text view next to the description label
    2. Tap on the name (this is a disguised button) next to the name label—on view change the description will be updated


### PlaceJsonRPCServer
This instructor provided project is the server that both AndroidApp and iOSApp projects connect to.
