# Welcome to Facepalm Account Management System

To start using the application. Download or clone the project and run any of the two gradle files depending on your OS.

In Linux:

```bash gradlew bootRun```

In Windows:

```gradlew bootRun```

### URL

You can access the REST services using http://localhost:8080

### Provisioning Emails
WARNING: the API's will not work unless you provision emails first. You can do this by a simple API.

To create an Account, send a Post request to '''/accounts''' with the following json body:
```
{
email: 'andy@example.com'
}
```

### The following are the mappings of each USER STORY:

##### User Story 1:
* /account-management/befriend
* RequestMethod: POST
* Request body:
```
{
friends:
[
'andy@example.com',
'john@example.com'
]
}
```
* Response Body:
```
{
"success": true
}
```
##### User Story 2:
* /account-management/get-friends
* RequestMethod: POST
* Request body:
```
{
email: 'andy@example.com'
}
```
* Response Body:
```
{
"success": true,
"friends" :
[
'john@example.com'
],
"count" : 1
}
```

##### User Story 3:
* /account-management/get-common-friends
* RequestMethod: POST
* Request body:
```
{
friends:
[
'andy@example.com',
'john@example.com'
]
}
```
* Response Body:
```
{
"success": true,
"friends" :
[
'common@example.com'
],
"count" : 1
}
```

##### User Story 4:
* /account-management/subscribe-updates
* RequestMethod: POST
* Request body:
```
{
"requestor": "lisa@example.com",
"target": "john@example.com"
}
```
* Response Body:
```
{
"success": true
}
```

##### User Story 5:
* /account-management/block-account
* RequestMethod: POST
* Request body:
```
{
"requestor": "andy@example.com",
"target": "john@example.com"
}
```
* Response Body:
```
{
"success": true
}
```

##### User Story 6:
* /account-management/get-update-recipients
* RequestMethod: POST
* Request body:
```
{
"sender": "john@example.com",
"text": "Hello World! kate@example.com"
}
```
* Response Body:
```
{
"success": true
"recipients":
[
"lisa@example.com",
"kate@example.com"
]
}
```


