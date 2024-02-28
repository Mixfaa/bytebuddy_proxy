# Simple proxy lib for building static proxy classes
uses bytebuddy as class generator

Example:

defining method interceptions:
```java
        // method def
        public static void receiveIntercepted(Client client, String message) {
            System.out.println("intercepted message " + message);
        }
        
        var sendInterception = new MethodInterceptionDescription.Builder()
                .setMatcher(ElementMatchers.named("send"))
                .setImlp(MethodCall.run(() -> System.out.println("proxy send"))).build();

        var receiveInterception = new MethodInterceptionDescription.Builder()
                .setMatcher(ElementMatchers.named("receive"))
                .setImlp(MethodCall.invoke(
                                SimpleTest.class.getMethod("receiveIntercepted", Client.class, String.class))
                        .withThis()
                        .withAllArguments()
                ).build();

        var interceptions = List.of(sendInterception, receiveInterception);
```
        
Making proxy class with field "_field" of type String:
```java
      var proxyClass = ProxyClassMaker.makeProxyClass(Client.class, interceptions, (bb) -> bb.defineField("_field", String.class, Visibility.PUBLIC));
```
Creating object instance:
```java
            var object = new ClassInstanceBuilder<>(proxyClass)
                .selectConstructor(int.class)
                .withField("_field","my little field")
                .newInstance(123);
```
Calling methods:
```java
        object.send("send");
        object.receive("receive");
```

