# Spotify Party Planner
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/aeca5fa20835b080c838#?env%5Bjrie.eu%5D=W3sia2V5IjoiSE9TVCIsInZhbHVlIjoianJpZS5ldSIsImVuYWJsZWQiOnRydWV9XQ==)
[![Build Status](https://travis-ci.org/SuddenlyPineapple/spotify-music-party-planner-frontend.svg?branch=master)](https://travis-ci.org/SuddenlyPineapple/spotify-music-party-planner-frontend.svg?branch=master)

[Project Board Link](https://github.com/users/SuddenlyPineapple/projects/1)

## Run backend
To run `spotify.auth` environment variable must be set to Spotify API secret.
```
./gradlew bootRun
```

## Frontend setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your unit tests
```
npm run test:unit
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

## Deployment - Docker

1. For `develop-stage` run `npm install` in `frontend` folder (must have node:13.13.0 installed locally)
2. Then if you want to deploy app as `productions-stage` you must delete node_modules folder from `frontend` folder
3. Set varaibles in `.env `:
    - `OFFER_RIDER_ARCHITECTURE` accordingly to your machine - `arm` or `x86`
    - `TARGET` accordingly to purpose - `develop-stage` or `production-stage`
4. Run `docker-compose up --build`

If you want clean Docker after shuting down all containers type `docker-compose down -v --rmi all --remove-orphans`.