# Open FDA live dashboard

Application built with Flask uses Dash and Plotly for graphs.  
Solution is dockerized.

## Run the project

* Running application

```
python3 flask app.py
```


## Docker image

* Building docker image
```
docker build --tag koziolk/openfdsa-live-dashboard .
```
* Running Docker image
```
docker run --env "MONGO_URI=mongodb://..." -p 8050:8050 koziolk/openfda-live-dashboard
```
* You can pull the latest docker image from docker.io registry
```
docker pull koziolk/openfda-live-dashboard
```

* Available env variables

```
MONGO_URI
MONGO_DB
GRAPH_REFRESH_INTERVAL
```
