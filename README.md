# login 구현


### 인증, 인가 구현을 하다가 막히거나 새로 공부한 것들
* WebSecurityAdapter가 deprecated 되었기 때문에 authenticationManager()나 void config(HttpSecurity)을 사용하는 것 대신에 AuthenticationConfiguration.getAuthentication()과 SecurityFilterChain config(HttpSecurity)를 사용했다.
* Filter를 구현할 때 @Autowired 자동주입이 불가능하기 때문에 직접 넣어줘야 한다.
