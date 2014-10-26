define(
  [
    'marionette'
  ],
  function(Marionette) {
    'use strict';

    var AppRouter = Marionette.AppRouter.extend({
      appRoutes: {
        '': 'handleDefaultRoute'
      }
    });

    return AppRouter;
  }
);
