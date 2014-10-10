//jshint devel:true
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
var httpntlm = require('httpntlm');
var url = require('url');
exports = module.exports = function(options){
  return function(client_req, client_res, next) {
    //console.log('options: ', options );
    var
      parsed = url.parse(client_req.url),
      baseRE = options.baseRE,
      path = parsed.path.replace(baseRE,options.replace),
      to = options.target+path;

    if(!baseRE.test(parsed.path)){
      next();
      return;
    }
    console.log('proxying: ' + to );
    httpntlm.get({
      url: to,
      username: options.user,
      password: options.pass,
      workstation: 'podmaster-dev',
      domain: options.domain
    }, function (err, res) {
        if(err) {
          console.error('error: ', err);
          return err;
        }
        //TODO: setup response headers (content-type:)
        //console.log(res.headers);
        //console.log(res.body);
        client_res.end(res.body);
    });
  };
};
