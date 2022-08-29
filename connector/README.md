# Healthcheck Extension

## 1.0.0 version - Release notes

New Features:
* (tmp) 

Improvements:
* (tmp)

Bug fixes:
* (tmp)


## Operations

### healthcheck:create-healthcheck-object

It creates the healtcheck object from the connector config and a list of dependencies performing some logic :
- bubbling up of dependencies errors
- computation of the time as the maximum of the time of the depenendecies
- set of HEALTHY/UNHEALTY in the "status" field

![create-healthcheck-2](doc_resources/2.create-healthcheck-object.png)

|Parameter|Note|
|---|---|
|Dependencies|List of Healthcheck Object that represent the dependencies of the service|


### healthcheck:http

Perform an HTTP request with the specified parameters in the inputs and return an Healthcheck Object based on outcome of the response.

|Parameter|required|Note|
|---|---|---|
|Service Name|true|Name of the service|
|HTTP Module Configuration|true|Indicate which HTTP request config should be used with this healtcheck.|
|Method|true|HTTP method to perform on external system|
|Path|true|Path where the request will be sent|
|Headers|false|HTTP headers the message should include.|
|Uri Params|false|URI parameters that should be used to create the request.|
|Http query params|false|Query parameters the request should include.|

### healthcheck:db

Perform the query specified as parameter and return an Healthcheck Object based on outcome of the response.

|Parameter|required|Note|
|---|---|---|
|Service Name|true|Name of the service|
|DB Module Configuration|true|Indicate which DB config should be used with this healtcheck.|
|SQL Query Text|true|The query to perform|


### healthcheck:jms

Perform the following action on the specified queue:
- publish a message
- consume the message

and return an Healthcheck Object based on outcome of the response.

|Parameter|required|Note|
|---|---|---|
|Service Name|true|Name of the service|
|JMS Module Configuration|true|Indicate which JMS config should be used with this healtcheck.|
|Queue Destination|true|The queue to use for perform healtcheck|


### healthcheck:jms

Perform the following action on the specified queue:
- publish a message
- consume the message

and return an Healthcheck Object based on outcome of the response.

|Parameter|required|Note|
|---|---|---|
|Service Name|true|Name of the service|
|JMS Module Configuration|true|Indicate which JMS config should be used with this healtcheck.|
|Queue Destination|true|The queue to use for perform healtcheck|

### healthcheck:amqp (WIP: FIX TO DO )

To be filled after the fix has be done



### healthcheck:internal-services (to-review)

|Parameter|required|Note|
|---|---|---|
|applyCustomMapping|true|Whetever if the script specified in custom-mapping parameter should be used to extract Healthcheck object from internal services|
|custom-mapping|true|Dataweave script to extract Healtcheck Object from internal services |


### healthcheck:hc-scope (to-review)
A custom scope that return an Healthcheck Object based on the outcome of the processor contained.

|Parameter|required|Note|
|---|---|---|
|Service Name|true|Name of the service|
|Service Type|true|Type of the service|

