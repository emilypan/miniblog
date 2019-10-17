# MiniBlog
- package war
`$ mvn clean package -DskipTests`
- deploy
`$ docker run -v /${fullpath}/miniblog-0.0.1-SNAPSHOT.war:/usr/local/tomcat/webapps/miniblog.war  --rm -it -p 9001:8080 tomcat`

## Requirement 
Design and implement a mini blog

Basic functionalities:
1. Sign in / sign up.
2. Create / Edit  an account profile.
3. Post / Edit / Delete an article.

Advanced functionalities:
1. Follow a friend.
2. Show articles from friend.
3. Privacy setting for articles. Friend can’t see your article if the article is private.

<!---
Instructions:
(I) Don't need to focus on UI / Java Script. A very simple UI is enough.
(II) Focus on backend and DB parts. Prefer to use Java to build the Web Server. For example, Jetty, tomcat and etc.
(III) Write design document. For example, We may ask questions like why chose this design.
(IV) We would like to know the problems you encounter and how to solve them. 
--->

## Design
* Server: tomcat
  - Single tomcat instance can handle about 300~350 TPS => LB to scale
  - Tomcat is very flexible and scalable
* Backend framework: Spring boot
* Language: Java
* Authentication
  - Authentication
    - confirming your identity
        - login 
  - Authorization
    - access control based on permissions
        - access url, object and methods
  - OAuth 2.0 Authentication
* JWT to standardize the response (token), stateless
* Request with token, https
* Mongodb vs MySQL
  - Mongodb: performance, schemaless, high availability and concurrency(lock granularity: global, database or collection level,)
  - MySQL: relationship, data normalization, high transaction
* Pagination


## Design Spec


Request Type | Endpoint     |    Description
| :------------ | :-------------- | :----- |
  POST          |`/user/signup`    |Return CREATED
  POST          |`/user/login `    |Return JWT token
  GET           |`/user`           |Return current user details
  POST          |`/user`           |GET user details, return given user details, /user/{id}
  PUT           |`/user`           |Update current user details, /user/{id}
  DELETE        |`/user`           |Delete a user given user email on payload
  POST          |`/user/follow`    |Follow users
  GET           |`/user/follow`    |Get followed users
  POST          |`/user/unfollow`  |Unfollow users
  POST          |`/blog`           |Create a new blog and return it
  GET           |`/blog`           |Get all blogs by user
  GET           |`/blog/{id}`      |Return a blog by id and user
  PUT           |`/blog/{id}`      |Update a blog by id and user and return it
  DELETE        |`/blog/{id}`      |Delete a blog by id and user   
  GET           |`/blog/follow`    |Get blogs from all followed users
  POST          |`/blog/follow`    |Get blogs from the given users

####POST `/user/signup`
##### Request
Message Header | Description
------------   | :---
Content-Type   | application/json

Body           | Description           | Required  
------------   | :---         | -------------
email          | register user email   | Y
password       | user password         | Y
name           | user name             | N
##### Response
Status|
------------| 
HTTP/1.1 201 CREATED|

####POST `/user/login`
##### Request
Message Header | Description
------------   | -------------
Content-Type   | application/json

Body           | Description           | Required  
------------   | -------------         | -------------
email          | register user email   | Y
password       | user password         | Y

##### Response
Status|
------------| 
HTTP/1.1 200 OK|

Body           | Description             
------------   | -------------         
token          | user token for further access 

##### Example Response
```json

{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MWFAeW9wbWFpbC5jb20iLCJyb2xlcyI6W3sicm9sZSI6IlVTRVIifV0sImlhdCI6MTU3MDczMjkyNywiZXhwIjoxNTcwNzM2NTI3fQ.0KCDzMydHz2VXNGb-Qbaaqg6dSFRdY2X2UGmQLv8inM"
}
```

####GET `/user`
##### Request
Message Header | Description| Required
------------   | -------------| -------------
Content-Type   | application/json| Y
X-JWT          | JWT token for current request user| Y

##### Response
Status|
------------| 
HTTP/1.1 200 OK|

Body        | Description             
------------| -------------         
id          | userId
email       | user email
name        | user name
createTime  | createTime
updateTime  | updateTime if available
##### Example Response
```json
{
    "id": "5d9f6f86df39a05a7bf3eb8b",
    "email": "test1a@yopmail.com",
    "name": "user a name",
    "createTime": 1570729862691
}
```

####GET `/blog`
##### Request
Message Header | Description| Required
------------   | -------------| -------------
Content-Type   | application/json| Y
X-JWT          | JWT token for current request user| Y

Request Params | Description
------------   | -------------
pageNum          | pagination start, default 0
pageSize          | pagination limit, default 5

##### Response
Status|
------------| 
HTTP/1.1 200 OK|

Body        | Description             
------------| -------------         
count       | count of blogs
blogs       | blogs
blogs[i].id       | blog id
blogs[i].title       | blog title
blogs[i].content       | blog content
blogs[i].size       | blog size, not used
blogs[i].author       | blog author
blogs[i].author.name       | blog author name
blogs[i].author.email       | blog author email
createTime  | createTime
updateTime  | updateTime if available
isPublic    | blog is public to followers

##### Example Response
```json
{
    "count": 5,
    "totalNumber": 11,
    "pageNum": 0,
    "pageSize": 5,
    "blogs": [
        {
            "id": "5d9fd0c9df39a069dd0d8fdf",
            "title": "test blog user 1 9",
            "content": "test blog 1111 aaaa",
            "size": 0,
            "author": {
                "name": "test test test name name name 1",
                "email": "test1@yopmail.com"
            },
            "createTime": 1570754761848
        },
        ......
    ]
}
```

## JWT
[ Header ].[ Payload ].[ Signature ]
```aidl
Decoded Payload
{
  "sub": "test2@yopmail.com",
  "roles": [
    {
      "role": "USER",
      "name": null,
      "userPermissions": null
    }
  ],
  "iat": 1571172394,
  "exp": 1571175994
}
```

