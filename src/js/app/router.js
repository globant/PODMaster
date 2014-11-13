define(
  [
    'marionette'
  ],
  function(Marionette) {
    'use strict';

    var AppRouter = Marionette.AppRouter.extend({
      appRoutes: {
        'index/*url': 'handleDefaultRoute'
      }
    });

    return AppRouter;
  }
);
