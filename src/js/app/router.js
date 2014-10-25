define(
  [
    'marionette',
    './modules/dashboard/dashboard.controller',
  ],
  function(Marionette, DashboardController) {

    var DashboardRouter = Marionette.AppRouter.extend({
      controller : new DashboardController(),

      appRoutes: {
        '': 'handleDefaultRoute'
      },

      initialize: function() {
        Backbone.history.start();
      }

    });

    return DashboardRouter;
  }
);
