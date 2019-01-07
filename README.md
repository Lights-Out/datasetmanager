#### Authentication in Google Cloud
Set environment variable GOOGLE_APPLICATION_CREDENTIALS to a path to google credentials file.

### Installation
```sh
git clone https://github.com/Lights-Out/datasetmanager.git && cd datasetmanager
mvn install
```
### Run
1 task:
```sh
cd table-creator
mvn exec:java -Dexec.args="projectId datasetId prefix count"
```
2 task:
```sh
cd table-updater
mvn exec:java -Dexec.args="projectId datasetId"
```
