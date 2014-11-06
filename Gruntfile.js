module.exports = function(grunt) {
  'use strict';

  var PATH_ASSETS = 'src',
      PATH_ASSETS_JS = PATH_ASSETS + '/js',
      PATH_ASSETS_CSS = PATH_ASSETS + '/css',
      PATH_ASSETS_IMG = PATH_ASSETS + '/img',
      PATH_DEPLOY_ASSETS = 'public',
      PATH_DEPLOY_ASSETS_IMG = PATH_DEPLOY_ASSETS + '/img';

  //##Project configuration
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    //##Annotated source generation with docco
    //run with `grunt docs`
    docco: {
      docs:{
        src:[
            //Files to include
            './Gruntfile.js',
            'src/config/amd.js'
        ],
        //options:
        //- layout: style
        //- output: dir to generate docs
        options:{
          layout: 'classic',
          output: 'annotated-source'
        }
      }
    },

    //##Clean task
    clean: [PATH_DEPLOY_ASSETS],

    //##Bower task
    bower: {
      install: {
        options: {
          copy: false,
          layout: 'byComponent',
          install: true
        }
      }
    },

    //##Copy task
    copy: {
      main: {
        expand: true,
        cwd: PATH_ASSETS,
        src: '**',
        dest: PATH_DEPLOY_ASSETS
      }
    },

    //##Requirejs (optimizer) task
    requirejs: {
      compile: {
        options: {
          appDir: PATH_ASSETS,
          dir: PATH_DEPLOY_ASSETS,
          baseUrl: './js',
          mainConfigFile: PATH_ASSETS_JS + '/main.js',
          optimize: 'uglify2',
          optimizeCss: 'none',
          modules: [
            {
              name: 'main'
            }
          ],
          skipDirOptimize: true
        }
      }
    },

    //##jshint task
    jshint: {
      all: [
        'Gruntfile.js', PATH_ASSETS_JS + '/**/*.js',
        '!' + PATH_ASSETS_JS + '/vendor/**/*.js',
        '!' + PATH_ASSETS_JS + '/app/templates.js'
      ]
    },

    //##Code Convention checking
    jscs: {
      files: {
        src: [
          'Gruntfile.js', PATH_ASSETS_JS + '/**/*.js',
          '!' + PATH_ASSETS_JS + '/vendor/**/*.js',
          '!' + PATH_ASSETS_JS + '/app/templates.js'
        ]
      },
      options: {
        config: '.jscsrc'
      }
    },

    //##Template compiler (deprecated)
    handlebars: {
      compile: {
        options: {
          namespace: 'JST',
          amd: true,
          processName: function(filename) {
            var pieces = filename.split('/');
            return pieces[pieces.length - 1];
          }
        },
        files: {
          'src/js/app/templates.js': PATH_ASSETS_JS + '/**/*.hbs'
        }
      }
    },

    //##Contatenation task
    concat: {
      css: {
        src: [
          'src/vendor/normalize-css/normalize.css',
          PATH_ASSETS_CSS + '/*.css'
        ],
        dest: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>-<%= pkg.version %>.concat.css'
      }
    },

    //##Css minification task
    cssmin: {
      all: {
        src: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>-<%= pkg.version %>.concat.css',
        dest: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>.min-<%= pkg.version %>.css'
      }
    },

    //##Css linting task
    csslint: {
      lax: {
        rules: {
          'box-sizing': false,
          'adjoining-classes': false
        },
        src: [
          PATH_ASSETS_CSS +
          '/*.css', '!' +
          PATH_ASSETS_CSS +
          '/normalize.css'
        ]
      }
    },

    //##Image minification
    imagemin: {
      png: {
        options: {
          optimizationLevel: 7
        },
        dynamic: [
          {
            expand: true,
            cwd: PATH_ASSETS_IMG,
            src: ['**/*.png'],
            dest: PATH_DEPLOY_ASSETS_IMG
          }
        ]
      },
      jpg: {
        options: {
          progressive: true
        },
        dynamic: [
          {
            expand: true,
            cwd: PATH_ASSETS_IMG,
            src: ['**/*.jpg'],
            dest: PATH_DEPLOY_ASSETS_IMG
          }
        ]
      }
    },

    //##Development server task
    connect: {
      server: {
        options: {
          keepalive:true,
          port: 9001,
          hostname: '*',
          middleware:function(connect) {
            var
              mount = function (dir) {
                return connect.static(require('path').resolve(dir));
              },
              log = function (req, resp, next) {
                //grunt.log.writeln(JSON.stringify(req.headers));
                next();
              };

            return [
              log,
              mount('src')
            ];
          }
        }
      },
    }
  });

  grunt.loadNpmTasks('grunt-docco');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-csslint');
  grunt.loadNpmTasks('grunt-css');
  grunt.loadNpmTasks('grunt-contrib-imagemin');
  grunt.loadNpmTasks('grunt-contrib-handlebars');
  grunt.loadNpmTasks('grunt-bower-task');
  grunt.loadNpmTasks('grunt-jscs');
  grunt.loadNpmTasks('grunt-prompt');
  grunt.loadNpmTasks('grunt-contrib-connect');

  grunt.registerTask('default', 'build:dev');

  grunt.registerTask('build:prod', [
    'clean',
    'bower',
    'jshint:all',
    'handlebars',
    'csslint:lax',
    'requirejs',
    'concat',
    'cssmin',
    'imagemin'
  ]);

  grunt.registerTask('build:dev', ['clean', 'bower', 'jshint:all', 'handlebars',
    'csslint:lax', 'copy', 'concat'
  ]);
    // serve task
  grunt.registerTask('serve', function() {
    grunt.task.run([
    'connect:server'
    ]);
  });
};
