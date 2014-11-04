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
            'pub/index.html':['blueprints/api.md']
        },
        options:{
          theme: 'slate-multi'
        }
      }
    },

    //##API mock server
    //run with `grunt shell:apimock`
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

    //##Development server task
    connect: {
      server: {
        options: {
          keepalive:true,
          port: 3002,
          hostname: '*',
          middleware:function(connect) {
            var
              mount = function (dir) {
                return connect.static(require('path').resolve(dir));
              };

            return [
              mount('pub')
            ];
          }
        }
      },
    }
  });

  grunt.loadNpmTasks('grunt-markdown');
  grunt.loadNpmTasks('grunt-docco');
  grunt.loadNpmTasks('grunt-shell-spawn');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-aglio');
  grunt.loadNpmTasks('grunt-apimock');

  //grunt.registerTask('default', 'build');

  // serve task
  grunt.registerTask('start', function() {
    grunt.task.run([
    'docco:docs',
    'aglio:api',
    'markdown:all',
    'shell:apimock',
    'connect:server'
    ]);
  });
};
