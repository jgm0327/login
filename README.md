# login

* WebSecurityAdapter가 deprecated 되었기 때문에 authenticationManager()나 void config(HttpSecurity)을 사용하는 것 대신에 AuthenticationConfiguration.getAuthentication()과 SecurityFilterChain config(HttpSecurity)를 사용했다.
