# Description
This is a sample application, created in order to demonstrate how we can expire JWT manually without storing it.

###### Token invalidation way:
Using random salt per user, so we can change that salt on logout and invalidate all tokens issued 
with that salt.



### Look here for start:

* `com.jwtdemo.application.service.JwtService` - here api access token is issued and validated.
* `com.jwtdemo.application.auth.DemoAuthenticationProvider` - authorization with JWT token and check salt.
