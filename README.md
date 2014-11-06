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

#### Deploy hal-browser one level up on the file system hierarchy (optionally)
  ```
  cd ..
  git clone  https://github.com/mikekelly/hal-browser.git

  ```
  - Note that this is not required because the `grunt serve` task have a proxy to rawgit (see [proxy to rawgit.com](#proxy-to-rawgit-com))

#### Start all services
  start `grunt start` as a background task (ie: `grunt start &`)
  ```
  grunt start &
  ```

#### Explore the API

  - [Browse the API documentation](/docs/)
  - Browse the API
    - [With a local client](/browser.html#/api/users/kevin)
    - [With mike's browser](/github/mikekelly/hal-browser/master/browser.html#/api/users/kevin)
    - [With FoxyCart's browser](/github/FoxyCart/hal-browser/master/hal_browser.html#/api/users/kevin)
    - [With jendagroovy's browser](/github/jendagroovy/hal-browser/master/browser.html#http://10.200.10.223:3000/api/users/kevin)
  - [Browse the annotated source](/annotated-source/Gruntfile.html)
  - [Browse this README](/README.html)

#### Proxy to rawgit.com
In order to use any client (available on github) to explore the API, the proxy have a
rewrite rule to redirect static content requests from /github to http://rawgit.com.

Note that to get this working throght globant proxy, the process `grunt start`  must
be wrapped/proxyfied with something like proxychains4

##### Proxychains4
- Installation on debian
  ```
  git clone git@github.com:rofl0r/proxychains-ng.git
  cd proxychains-ng
  sudo make all
  ```

- Configuration:

  The /etc/proxychains.conf file:

  ```
  strict_chain
  localnet 10.0.0.0/255.0.0.0
  localnet 127.0.0.0/255.0.0.0
  [ProxyList]
  http  10.28.2.251 3128
  ```

- How to use:

  ```
  proxychains4 grunt start &
  #now you can visit /github/mikekelly/hal-browser/master/browser.html#/api/users/kevin
  ```

## TODOs:

  - Change grunt-connect to grunt-express, then we can:
    - use api-mock as a (restartable) middleware
    - use `grunt stop` and `grunt restart`
    - integrate with a watch task
    - use livereload for easy API authoring
    - serve static (like HAL-browser) content in the same origin of api-mock to avoid CORS issues and be able to use relative URLs inside HAL documents
