## 1.背景
基于前后端分离项目的后端模块；
## 2.相关技术
* springboot全家桶
    * web模块
    * security模块；用于权限的验证
    * mongodb 模块；集成mogodb模块
* jwt 用于token的生成
* mongodb
* lomok
* 后续会细分出更多的模块。用上springcloud全家桶

## 3.权限验证流程
### 3.1 构建User对象
实现security的UserDetail。之后所有权限获取都是从这个对象中返回

> 重写的默认属性必须返回true，不然在登录那块验证该属性是不是true。如果默认返回false，会报出各种用户相关的异常

```java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUser implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

   
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
```

### 3.JwtUserDetailsServiceImpl
重写security的UserDaiService的loadByusername方法，实现自定义的权限验证

```java
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //设置查询条件，邮箱是唯一的
        User queryUser = new User();
        queryUser.setEmail(username);
        List<User> userList = null;
        try {
            userList = this.userService.getUser(queryUser);

            if (CollectionUtils.isEmpty(userList)) {
                //return new JwtUser(username, queryUser.getPwd(), authorities);
                throw new UsernameNotFoundException("用户账号：" + username + "，不存在");
            } else {
                queryUser = userList.get(0);
                Set<GrantedAuthority> authorities = new HashSet<>();
                //获取该用户所有的权限信息
                this.userService.getRoleByUserId(queryUser.getId()).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
                });

                return new JwtUser(username, queryUser.getPwd(), authorities);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
```
### 3.3 token生成方法
```java
@Component
public class JwtTokenUtil implements Serializable {
    /**
     * 密钥
     */
    private final String secret = "code4fun";

    final static Long TIMESTAMP = 86400000L;
    final static String TOKEN_PREFIX = "Bearer";


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + TIMESTAMP);
        return TOKEN_PREFIX + " " +Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
```

### 3.4 token校验过滤器
每次请求的时候都会被该过滤器过滤拦截。主要是校验token的有效性
```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 每个请求都被拦截
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";

        if (authHeader != null && authHeader.startsWith(tokenHead)) {

            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //返回jwtUser
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //将该用户的权限信息存放到threadlocal中
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

```
### 3.4 webSecurity配置
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
//    private RestAccessDeniedHandler restAccessDeniedHandler;


    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
    * 注入密码BCryptPasswordEncoder
    * 在添加用户的时候，要用 BCryptPasswordEncoder.encode()加密
    * @return  
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/user/**", "/login",
                        "/js/**", "/bootstrap/**", "/css/**", "/images/**",  "/fonts/**").permitAll() //静态文件拦截

                .anyRequest().authenticated()
                .and().headers().cacheControl();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
```
> 至此，相关的配置就配置完了。在登录操作的时候需要注意一下:
用户信息的验证全部交给spring security来操作，代码如下：
```java
    /**
     * 登录操作，返回token
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public String login(String userName, String password) throws Exception {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return jwtTokenUtil.generateToken(userDetails);
    }
    
  
```
### 3.4 用户验证过程
```java
UsernamePasswordAuthenticationToken
authenticationManager.authenticate(upToken);
//通过这个创建一个代理（ProviderManager）对象
delegate = this.delegateBuilder.getObject();
//调用代理对象的认证方法
delegate.authenticate(authentication)
	1.代理对象调用父类的 parent.authenticate(authentication);认证方法
		1.进到parent.authenticate方法，去定ProvideManager的具体类型是DaoProviderManager
	2.provider.authenticate(authentication);	//此时的provider是DaoProviderManager
		1.判断参数authentication是不是UsernamePasswordAuthenticationToken类型；不是则跑出异常
		2.取出唯一标识字段username
			1.判断userCache是否包含user缓存
				1.不在缓存中，创建user对象并存放到缓存中
					//调用这个方法转换成user对象
					1.user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
						//调用用户自定义实现了UserDetailService的方法来获得user对象
						1.UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
				2.preAuthenticationChecks.check(user);
					1.preAuthenticationChecks.check校验上一部返回的user对象的属性，只要用户实现的userDetail的get，set方法赋上值就好了
				  additionalAuthenticationChecks(user,
					(UsernamePasswordAuthenticationToken) authentication);
					  1.uthentication.getCredentials() == null判断密码是不是为空
					  2.presentedPassword = authentication.getCredentials().toString(); 获取页面传递过来的密码
					  3.passwordEncoder.matches(presentedPassword, userDetails.getPassword())判断页面上传递过来的密码跟数据库中的密码是不是一致。
					  	 1.调用BCrypt.checkpw(rawPassword.toString(), encodedPassword)比对
					  	 		1.调用 hashpw 来加密页面传递过来的密码信息。然后与数据库中的密码比对。如果相同则返回成功，不同则报错


```

### 4 整合RabbitMQ
#### 4.1 RabbitMQ的相关概念
* 组成部分
    * 队列（Queue）
    
    声明队列
    ```java
      @Bean
      public Queue addUserQueue() {
          return new Queue("demo-user-add");
      }

    ```
    * 交换机（Exchange）
    
    用于转发消息，但是它不会做存储 ，如果没有 Queue bind 到 Exchange 的话，它会直接丢弃掉 Producer 发送过来的消息。
    这里有一个比较重要的概念：**路由键** 。消息到交换机的时候，交互机会转发到对应的队列中，那么究竟转发到哪个队列，就要根据该路由键。
    **交换机的功能主要是接收消息并且转发到绑定的队列，交换机不存储消息，在启用ack模式后，交换机找不到队列会返回错误**。交换机有四种类型：Direct, topic, Headers and Fanout
    **Direct**
    > direct 类型的行为是"先匹配, 再投送". 即在绑定时设定一个 routing_key, 消息的routing_key 匹配时, 才会被交换器投送到绑定的队列中去。Direct Exchange是RabbitMQ默认的交换机模式，也是最简单的模式，根据key全文匹配去寻找队列。
    
    * **topic**
    > 类似Director模式，但是更加灵活，可以根据通配符去寻找对应的exchange。
    > * 匹配一个字符
    > `#` 匹配多个字符
    **Headers**
    > 设置header attribute参数类型的交换机
    **Fanout**
    > 转发消息到所有绑定队列；
    > 消息广播的模式，不管路由键或者是路由模式，会把消息发给绑定给它的全部队列，如果配置了routing_key会被忽略。
    
    代码声明一个exchange：
    ```java
    @Bean
    public TopicExchange demoTestTopicExchange() {
        return new TopicExchange("demoTestTopic");
    }
    ```
    * 绑定（Binding）
    通过routing key声明exchange与queue之间关系的。从而确定了我这个msg发到哪个exchange上面
    。然后与exchange再路由到对应的queue上面，从而发给了对应的消费者
    ```java
        @Bean
        public Binding addUserBinding() {
            return BindingBuilder.bind(addUserQueue()).to(addUserTopicExchange()).with("cn.com.user.add");
    
        }
    ```
#### 4.2 发送、接收消息
##### 4.2.1 发送消息
```java
@Slf4j
@Component
public class UserMQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     * @param exchangeNanme 队列名称
     * @param routingKey 路由key
     * @param msg 具体消息内容
     * @throws Exception
     */
    public void sendUserMQ(String exchangeNanme, String routingKey, String msg) throws Exception {
        log.info("向交换机：{}，匹配规则：{}， 发送消息：{}", exchangeNanme, routingKey, msg);
        this.amqpTemplate.convertAndSend(exchangeNanme, routingKey, msg);
    }

}
```
##### 4.2.2 接收消息
* 通过注解的方式主动监听接收
> 声明我要监听哪个queue即可
```java
    @RabbitListener(queues = "demo-user-add")
    public void getMsg(String msg) throws Exception{
        log.info("获取消息{}", msg);
        User user = (User) JSONObject.toBean(JSONObject.fromObject(msg), User.class);
        userService.addUser(user);
    }
```
* 被动接收
```java
String data = (String) this.amqpTemplate.receiveAndConvert(queueName);
```
#### 4.3 模拟高并发取值
```java
@Test
public void testGetMsg() throws Exception{
    ExecutorService service = Executors.newCachedThreadPool(); //创建一个线程池
    final CountDownLatch beginCountDownLatch = new CountDownLatch(1);
    final CountDownLatch countDownLatch = new CountDownLatch(100);

    for (int i = 0; i < 100; i++) {
        Runnable runnable = new Runnable() {
            int index = 1;
            @Override
            public void run() {
                try {
                    /**
                     *如果调用对象上的await()方法，那么调用者就会一直阻塞在这里，直到别人通过cutDown方法，将计数减到0，才可以继续执行。
                     * 这里先调用beginCountDownLatch的await方法，等到循环结束后，内存中就有100个线程等待去运行。
                     * 所以等到beginCountDownLatch调用countDown的时候，100个线程就开始执行
                     */
                    beginCountDownLatch.await();
                    log.info("------->index:{}", index);
                    String data = (String) amqpTemplate.receiveAndConvert("demo-test");
                    log.info("==============>消息n内容：{}", data);

                    countDownLatch.countDown();
                    index++;

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        service.execute(runnable);
    }
    //释放主线程，之前声明的100个线程就开始执行
    beginCountDownLatch.countDown();
    //
    countDownLatch.await();

}
```

    
       









