Asynchronous action driven - POC
================================

Goal
------------------

Dependencies
------------------
Here is the library used for this POC

####Front-end
- [scala.js](https://www.scala-js.org)
- [scala-js-dom](http://scala-js.github.io/scala-js-dom/)
- [scalajs-react](https://github.com/japgolly/scalajs-react)
- [diode](https://diode.suzaku.io/)

####Back-end
- [akka-http](https://github.com/akka/akka-http)
- [akka-http-play-json](https://github.com/hseeberger/akka-http-json)
- [scalajs-scripts](https://github.com/vmunier/scalajs-scripts)

##Shared
- [play-json]

Folder structure
-------------------
```bash
.
├── build.sbt
├── client
│   └── src
│
├── LICENSE
├── project
│   ├── build.properties
│   └── plugins.sbt
│
├── README.md
├── server
│   └── src
│
└── shared
    └── src
```
This repository is divided in 3 sub folders:
- **client:** who contains the web application
- **server:** who contains the web server
- **shared:** who contains shared logic between them

How to build (easier than saying 1, 2 3)
---------------
First, checkout the project
```bash
$> git clone git://github.com/jquery/jquery.git
```
Then go to the root folder
```bash
$> cd async-action-driven
```
Finally, compile the project with sbt
```bash
$> sbt compile
```

How to run
---------------
```bash
$> sbt run
[info] Running me.mmcoulombe.aad.WebServer 
Server is listening on 0:0:0:0:0:0:0:0:8080

```
Now you can test it, open a browser to [localhost:8080](http://localhost:8080)

You have now a list of feed displayed. Now, you can open a terminal and run this command
```bash
$> curl -X DELETE http://localhost:8080/api/v1/rss/1 
```

And you will see an item disappear from the user interface without having refreshed the page.