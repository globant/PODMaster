FORMAT: 1A
HOST: https://agilepods.globant.com

# Agile PODS
Aigle PODS API is a dashboard application service

# Group Users
User related resources

## users [/api/users/{user}]

### GET

+ Parameters
    + user = `~` (optional, string, `kevin`) ... String `user` to perform action with.

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http://10.200.10.223:3000/api/users/~>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/users/~"},
                    "curies":{
                      "name":"ap",
                      "href":"http://10.200.10.223:3002/#page:{rel}",
                      "templated":true
                    },
                    "dashboard":{"href":"http://10.200.10.223:3000/api/users/~/dashboard"},
                }
            }

## Kevin [/api/users/kevin]

### GET

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http://10.200.10.223:3000/api/users/kevin>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/users/kevin"},
                      "curies":[
                      {
                        "name":"ap",
                        "href":"http://10.200.10.223:3002/#page:{rel}",
                        "templated":true
                      }
                    ],
                    "ap:dashboard":{"href":"http://10.200.10.223:3000/api/dashboards/1stLevel"}
                },
                "name":"Kevin",
                "type":"1st level",
                "job title": "SVP Engineering",
                "age": 50,
                "location":"California"
            }

## Wil [/api/users/wil]

### GET

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http://10.200.10.223:3000/api/users/wil>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/users/wil"},
                    "dashboard":{"href":"http://10.200.10.223:3000/api/dashboards/2ndLevel"}
                },
                "name":"Wil",
                "type":"2nd level",
                "job title": "Director Engineering",
                "age": 33,
                "location":"California"
            }

# Group Dashboard
Dashboard related resources

## 1st Level Dashboard [/api/dashboards/1stLevel]

### GET

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http://10.200.10.223:3000/api/dashboards/1stLevel>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/dashboards/1stLevel"},
                    "views":[
                        {"name":"products","href":"http://10.200.10.223:3000/api/views/products"},
                        {"name":"pods","href":"http://10.200.10.223:3000/api/views/pods"}
                    ]
                }
            }


## 2nd Level Dashboard [/api/dashboards/2ndLevel]

### GET

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http://10.200.10.223:3000/api/dashboards/2ndLevel>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/dashboards/2ndLevel"},
                    "views":[
                        {"name":"products","href":"http://10.200.10.223:3000/api/views/products"},
                        {"name":"pods","href":"http://10.200.10.223:3000/api/views/pods"}
                    ]
                }
            }

# Group Views
Views related resources

## views [/api/views/{name}/{instance}]

### GET

+ Parameters
    + name (required, string, `master`) ... String `name` of the View (Template) to perform action with.
    + instance (optional, string, `abcdef1234567890`) ... Guid `id` of the View (Instance) to perform action with.

+ Request (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: </api/views/master>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/views/master"},
                    "instances":[
                        {"name":"master","href":"http://10.200.10.223:3000/api/views/master/abcdef01234567889"}}
                    ]
                }
            }


# Group Widgets
Widgets related resources

## Widgets Collection [/api/widgets]

### GET
+ Request /api/widgets (application/hal+json)


+ Response 200 (application/hal+json)

    + Header

            Link: <http:/agilepods.globant.com/api/widgets/?name=velocity;view=products;view_items=product1,product2;time_period=quarter;when=2014Q1>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/api/widgets/?name=velocity;view=products;view_items=product1,product2;time_period=quarter;when=2014Q1"},
                    "widgets":[
                        {"name":"velocity","href":"http://10.200.10.223:3000/widgets/velocity"},
                        {"name":"ecosystem","href":"http://10.200.10.223:3000/widgets/ecosystem"}
                    ]
                }
            }


## Widget Resource [/widgets/{name}?{view}={view_items};{time_period}={when}]
A single Widget object with all its details

    + Examples:
        * product=product1
        * products=product1,product2
        * pod=pod1
        * pods=pod1,pod2,pod3

+ Parameters
    + name (required, string, `velocity`) ... String `name` of the Widget to perform action with. Has example value.
        + Values
            + `velocity`
            + `ecosystem`
    + view (required, string, `products`) ... String `view` type identifier.
        + Values
            + `products`
            + `product`
            + `pods`
            + `pod`

    + view_items = `all` (optional, list, `product1`) ...
        A comma separated `list` of `view` type items.
        Some (singular) views requires only one item in this list (ie: product and pod)

    + time_period (required, string, `quarter`) ... A `time-period` identifier
        + Values
            + `quarter`
            + `month`
            + `sprint`
    + when (required, time-period, `2014Q1`) ... A `time-period` spec

### Retrieve Widget [GET]
+ Request /widgets/velocity?products=product1,product2;quarter=2014Q1 (application/hal+json)

+ Response 200 (application/hal+json)

    + Header

            Link: <http:/agilepods.globant.com/api?name=velocity;view=products;view_items=product1,product2;time_period=quarter;when=2014Q1>;rel="self"

    + Body

            {
                "_links":{
                    "self":{"href":"http://10.200.10.223:3000/widgets?name=velocity;view=products;items=product1,product2"}
                },

                "_embedded":{
                    "data":{
                    ...{productXXXX:{Q1:[{pod1:{sprint1:{velocity:10}}},...]}}

                    ...{pod1:{Q1:[{product1:{sprint1:{velocity:10}}},...]}}
                    }
                }
            }

