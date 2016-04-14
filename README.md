# Test for CDI-494

    mvn clean test -Pweld1
    mvn clean test -Pweld2
    mvn clean test -Powb12
    mvn clean test -Powb16

* **testAnyListObserver()** 
  * should always fail for CDI 1.0
  * for 1.1+ it's not clear
  * see also https://issues.jboss.org/browse/CDI-494
* **testAnyListObserverBeanManager()** should always fail
* **testFoo()** 
  * is almost identical to TCK
  * should always fail for CDI 1.0
  * for 1.1+ should only pass if we clafiry a wildcard is an unresolvable type variable
  * see also https://issues.jboss.org/browse/CDITCK-510
* **testAnyListObserverBoundWildcard()**
  * is very similar to TCK, but uses more complicated type hierarchy
  * should always fail for CDI 1.0
  * for 1.1+ should only pass if we clafiry a wildcard is an unresolvable type variable
  * passes on OWB 1.6 which is probably some bug caused during type variable resolution

| Test  | Weld 1.1 | Weld 2.3 | OWB 1.2 | OWB 1.6 |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| testAnyListObserver()  | fails  | fails | fails | passes |
| testAnyListObserverBeanManager()  | passes  | passes | passes | passes |
| testAnyListObserverBoundWildcard()  | passes | passes | passes | fails |
| testFoo()  | passes | passes | passes | passes |
