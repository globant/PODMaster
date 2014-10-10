module.exports = function(grunt) {
  'use strict';

  var PATH_ASSETS = 'src',
      PATH_ASSETS_JS = PATH_ASSETS + '/js',
      PATH_ASSETS_CSS = PATH_ASSETS + '/css',
      PATH_ASSETS_IMG = PATH_ASSETS + '/img',
      PATH_DEPLOY_ASSETS = 'public';

  // ==========================================================================
  // Project configuration
  // ==========================================================================
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    clean: [PATH_DEPLOY_ASSETS],

    bower: {
      install: {
        options: {
          copy: false,
          layout: 'byComponent',
          install: true
        }
      }
    },

    copy: {
      main: {
        expand: true,
        cwd: PATH_ASSETS,
        src: '**',
        dest: PATH_DEPLOY_ASSETS
      }
    },

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

    // js linting options
    jshint: {
      all: [
        'Gruntfile.js', PATH_ASSETS_JS + '/**/*.js',
        '!' + PATH_ASSETS_JS + '/vendor/**/*.js',
        '!' + PATH_ASSETS_JS + '/app/templates.js'
      ]
    },

    //Code Convention checking
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

    concat: {
      css: {
        src: ['src/vendor/normalize-css/normalize.css', PATH_ASSETS_CSS + '/*.css'],
        dest: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>-<%= pkg.version %>.concat.css'
      }
    },

    cssmin: {
      all: {
        src: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>-<%= pkg.version %>.concat.css',
        dest: PATH_DEPLOY_ASSETS +
          '/css/<%= pkg.name %>.min-<%= pkg.version %>.css'
      }
    },

    csslint: {
      lax: {
        rules: {
          'box-sizing': false,
          'adjoining-classes': false
        },
        src: [PATH_ASSETS_CSS + '/*.css', '!' + PATH_ASSETS_CSS + '/normalize.css']
      }
    },

    imagemin: {
      png: {
        options: {
          optimizationLevel: 7
        },
        dynamic: [
          {
            expand: true,
            cwd: PATH_ASSETS + '/img',
            src: ['**/*.png'],
            dest: PATH_DEPLOY_ASSETS + '/img'
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
            cwd: PATH_ASSETS + '/img',
            src: ['**/*.jpg'],
            dest: PATH_DEPLOY_ASSETS + '/img'
          }
        ]
      }
    },
    prompt: {
      credentials:{
        options:{
          questions:[
            {
              config:'domain',
              type:'input',
              message:'ntlm domain:',
              default:'globant',
            },
            {
              config:'user',
              type:'input',
              message:'ntlm user:',
              default:'gabriel.pittau',
            },
            {
              config:'pass',
              type:'password',
              message:'ntlm password:',
              default:'',
            }
          ]
        }
      }
    },
    connect: {
      server: {
        options: {
          keepalive:true,
          port: 9001,
          hostname: '*',
          middleware:function(connect,options){
            var
              mount = function (dir) {
                return connect.static(require('path').resolve(dir));
              },
              proxy = require('./ntlm-proxy'),
              log = function (req,resp,next){
                //grunt.log.writeln(JSON.stringify(req.headers));
                next();
              };

            return [
              log,
              proxy({
                baseRE:/^\/service/,
                replace:'',
                target:'https://npr0140dxw01.globant.com',
                domain:grunt.config('domain'),
                user:grunt.config('user'),
                pass:grunt.config('pass'),
              }),
              mount(options.base),
              mount('sample'),
              mount('src')
            ];
          }
        }
      },
    }
  });

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

  grunt.registerTask('build:prod', ['clean', 'bower', 'jshint:all', 'handlebars',
    'csslint:lax', 'requirejs', 'concat', 'cssmin', 'imagemin'
  ]);

  grunt.registerTask('build:dev', ['clean', 'bower', 'jshint:all', 'handlebars',
    'csslint:lax', 'copy', 'concat'
  ]);
    // serve task
  grunt.registerTask('serve', function(){
    grunt.task.run([
    //'connect:livereload',
    'prompt:credentials',
    //'configureProxies:server',
    'connect:server'
    //'watch'
    ]);
  });
};
