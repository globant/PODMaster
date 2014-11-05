# agilePODS - API

## agilePODS API design documentation and tooling

  * Documentation generator
  * Mock Server
  * Generic API browser

### How to use:

#### Start
  ```
  npm install
  ```

#### Install api-mock as global
  ```
  npm install -g api-mock
  ```

#### Start all services
  start `grunt start` as a background task (ie: `grunt start &`)
  ```
  grunt start &
  ```

##IMPORTANT!

  All Urls depends on a host called `dev.agilepods.globant.com`, to get all this stuff working properly, the client must to edit their *hosts file* to point to the development server IP

#### Explore the API

  - [Browse the API documentation](/docs/)
  - [Browse the API mocks](/browser.html#/api/users/kevin)
  - [Browse the annotated source](/annotated-source/Gruntfile.html)
  - [Browse this README](/README.html)

## TODOs:

  - Change grunt-connect to grunt-express, then we can:
    - use api-mock as a (restartable) middleware
    - use `grunt stop` and `grunt restart`
    - integrate with a watch task
    - use livereload for easy API authoring
    - serve static (like HAL-browser) content in the same origin of api-mock to avoid CORS issues and be able to use relative URLs inside HAL documents
