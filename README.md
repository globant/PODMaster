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

#### Explore the API

  - [Browse the API documentation](http://10.200.10.223:3002/)
  - [Browse the API mocks](http://10.200.10.223:3001/browser.html#http://10.200.10.223:3000/api/users/kevin)
  - [Browse the annotated source](http://10.200.10.223:3002/annotated-source/Gruntfile.html)
  - [Browse this README](http://10.200.10.223:3002/README.html)

## TODOs:

  - Change all references to `http://10.200.10.223` to something like `http://dev.agilepods.globant.com`
  - Change grunt-connect to grunt-express, then we can:
    - use api-mock as a (restartable) middleware
    - use `grunt stop` and `grunt restart`
    - integrate with a watch task
    - use livereload for easy API authoring
    - serve static (like HAL-browser) content in the same origin of api-mock to avoid CORS issues and be able to use relative URLs inside HAL documents
