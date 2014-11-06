module.exports = function(grunt) {
  'use strict';

  //##Project configuration
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    //##Markdown content
    //run with `grunt markdown`
    markdown: {
      all:{
        files:[
          {
            expand:true,
            src:'*.md',
            dest:'pub/',
            ext:'.html'
          }
        ],
        options:{
          markdownOptions:{
            gfm:true,
            hightlight:'auto'
          }
        }
      }
    },

    //##Annotated source generation with docco
    //run with `grunt docs`
    docco: {
      docs:{
        src:[
            //Files to include
            './Gruntfile.js'
        ],
        //options:
        //- layout: style
        //- output: dir to generate docs
        options:{
          layout: 'classic',
          output: 'pub/annotated-source'
        }
      }
    },

    //##API documentations renderer and server
    //run with `grunt aglio:api`
    aglio: {
      api:{
        //Files to include
        files:{
            'pub/docs/index.html':['blueprints/api.md']
        },
        options:{
          theme: 'slate-multi'
        }
      }
    },

    //##API mock server
    //- run with `grunt shell:apimock`
    //- kill with `grunt shell:apimock:kill`
    //###Atention:
    // this is a sort of hack to start api-mock in background.
    // TODO: use `grunt-apimock` or use `api-mock` as a node module integrated as a express app
    shell: {
      apimock:{
        command: 'api-mock blueprints/api.md -p 3000 -k',
        options:{
          async:true,
          detached:true
        }
      }
    },

    //##Watch for file changes task
    watch: {
      blueprints: {
        files: 'blueprints/*',
        tasks: ['aglio:api']
      },
      contents: {
        files: '*.md',
        tasks: ['markdown']
      }
    },

    //##Development server and proxies task
    //run with `grunt serve`
    //### Note & TODO:
    //The hal-browser is (currently) outside of this project.
    //This code assume that hal-browser repo is cloned one level up
    // ```
    // cd ..
    // git clone  https://github.com/mikekelly/hal-browser.git
    // ```
    connect: {
      server: {
        options: {
          keepalive:true,
          port: 3002,
          hostname: '*',
          middleware:function(connect) {
            var
              //the proxy server:
              proxy = require('grunt-connect-proxy/lib/utils').proxyRequest,
              //the static content server:
              mount = function (dir) {
                return connect.static(require('path').resolve(dir));
              };

            //returns the middlewares
            return [
              proxy,
              //mount static content
              mount('pub'),
              //mount (static) hal-browser
              mount('../hal-browser')
            ];
          }
        },
        //config to redirect requests from `/api` to `127.0.0.1:3000/api`
        proxies: [
          {
              context: '/api',
              host: '127.0.0.1',
              port: 3000,
              https: false,
              //changeOrigin not necessary
              changeOrigin: false,
              xforward: false
          },
          //####proxy to rawgit.com
          //Redirect requests from `/github`
          //to `http://rawgit.com`
          //Note that to get this working throght
          //globant proxy, the process must
          //be wrapped/proxified with something like
          //proxychains4
          //####The /etc/proxychains.conf file:
          //```
          //strict_chain
          //localnet 10.0.0.0/255.0.0.0
          //localnet 127.0.0.0/255.0.0.0
          //[ProxyList]
          //http  10.28.2.251 3128
          //```
          {
              context: '/github',
              host: 'rawgit.com',
              port: 80,
              https: false,
              //changeOrigin not necessary
              changeOrigin: true,
              xforward: false,
              rewrite:{
                '^/github':''
              }
          }
        ]
      },
    }
  });

  grunt.loadNpmTasks('grunt-markdown');
  grunt.loadNpmTasks('grunt-docco');
  grunt.loadNpmTasks('grunt-shell-spawn');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-connect-proxy');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-aglio');
  grunt.loadNpmTasks('grunt-apimock');

  grunt.registerTask('default', 'start');

  //### Server and Development proxy
  // Configure Proxies and start the connect server
  grunt.registerTask('serve', function() {
    grunt.task.run([
    'configureProxies:server',
    'connect:server'
    ]);
  });

  //### start all task
  grunt.registerTask('start', function() {
    grunt.task.run([
    // Generate annotated source documentation
    'docco:docs',
    // Generate API documentation
    'aglio:api',
    // Render markdown content
    'markdown:all',
    // Start the API mock server
    'shell:apimock',
    // Start development server
    'serve'
    ]);
  });
};
