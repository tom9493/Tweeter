# Tweeter
This is a full-stack implementation of a Twitter mockup using java. There is a front end that uses the observer pattern and MVP design pattern to set up View, Presenter, and Model layers. These then work through Amazon Web Services (AWS) Lambda functions to connect to the back end. The back end uses a similar layering system with DAO classes and the abstract factory pattern. These allow implementation of classes that connect to databases. The implemented database code uses DynamoDB (also through AWS). There are many other design principles adhered to throughout the app, which are described in the Design Principles repository. 

The app has full functionality for 14 actions: login, register, logout, post a status, follow, unfollow, get followers, get following, get followers count, get following count, check if an account follows another account, get feed, get story, and get user. 

This took a lot of time to learn and implement. I had to learn the concept and recreate it for 14 different operations through all layers of both front and back ends. Making it work through AWS was also a long and tedious process.

Note: Amazon Web Services continued to charge me despite not using the service, so I deleted the associated account. The app is not functional anymore because of this, although the correct implementation of the app is still visible through examining the java code. 
