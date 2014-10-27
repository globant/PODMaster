define(
  [
    'backbone',
    'marionette',
    'hbs!app/modules/dashboard/dashboard',
    'app/modules/widgets/agilePodsEcosystem.view'
  ],
  function(Backbone, Marionette, template, Widget) {
    'use strict';

    var DashboardView = Marionette.LayoutView.extend({

      el: 'section.main',

      template: template,
      regions:{
        menu:'#menu',
        content: '#content'
      },
      render:function(){
        var model = new Backbone.Model(
            {
              "nodetype": "root",
              "name": "",
              "children": [
                  {
                      "nodetype": "project",
                      "name": "ProjectName",
                      "children": [
                          {
                              "nodetype": "pod",
                              "name": "POD Dev",
                              "children": [
                                  {
                                      "nodetype": "member",
                                      "name": "Jhon Daemon (TL)",
                                      "initials": "JD",
                                      "role": "TL",
                                      "size": 70
                                  },
                                  {
                                      "nodetype": "member",
                                      "name": "Gerardo de Pardié (DEV)",
                                      "initials": "GP",
                                      "role": "DEV",
                                      "size": 70
                                  },
                                  {
                                      "nodetype": "member",
                                      "name": "Arnold Pyuarzeneguer (TL)",
                                      "initials": "AP",
                                      "role": "TL",
                                      "size": 70
                                  }
                              ]
                          },
                          {
                              "nodetype": "pod",
                              "name": "POD Mgnt",
                              "children": [
                                  {
                                      "nodetype": "member",
                                      "name": "Astrid D3 (BA)",
                                      "initials": "AD",
                                      "role": "BA",
                                      "size": 70
                                  },
                                  {
                                      "nodetype": "member",
                                      "name": "Julia Gandolfo (PM)",
                                      "initials": "JG",
                                      "role": "PM",
                                      "size": 70
                                  },
                                  {
                                      "nodetype": "member",
                                      "name": "Jorgelina Gullrich (BA)",
                                      "initials": "JG",
                                      "role": "BA",
                                      "size": 70
                                  }
                              ]
                          },
                          {
                              "nodetype": "pod",
                              "name": "POD Disenio",
                              "children": [
                                  {
                                      "nodetype": "member",
                                      "name": "Natan Arrivederchi (UX)",
                                      "initials": "NA",
                                      "role": "UX",
                                      "size": 70
                                  },
                                  {
                                      "nodetype": "member",
                                      "name": "An Alala (DUI)",
                                      "initials": "AA",
                                      "role": "DUI",
                                      "size": 70
                                  }
                              ]
                          }
                      ]
                  }
              ]
          }
        );
        Marionette.LayoutView.prototype.render.apply(this,arguments);
        this.content.show(new Widget({model:model}));
        return this;
      }

    });

    return DashboardView;
  }
);
