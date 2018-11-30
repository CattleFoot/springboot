
1. idea java项目转maven项目
[1] 项目上右键 Add Framework Support；
[2] 选择列表项的maven即可；

2. spring boot with web
[1]. https://www.baeldung.com/spring-boot-start

3. spring boot with swagger2 
[1]. https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
[2]. https://blog.csdn.net/itguangit/article/details/78978296
[3]. http://blog.didispace.com/springbootswagger2/

4. spring boot with consul 
[1]. http://www.ityouknow.com/springcloud/2018/07/20/spring-cloud-consul.html



注意：
系列文章：
[1]. https://juejin.im/post/5b1922cdf265da6e173a51ec
[2]. http://www.ityouknow.com/springcloud/2018/07/20/spring-cloud-consul.html


swagger2 注解说明：
[1]. https://juejin.im/post/5b1922cdf265da6e173a51ec
- @Api：用在类上，标志此类是Swagger资源
	value：接口说明
	tags：接口说明，可以在页面中显示。可以配置多个，当配置多个的时候，在页面中会显示多个接口的信息

- @ApiOperation：用在方法上，描述方法的作用

- @ApiImplicitParams：包装器：包含多个ApiImplicitParam对象列表

- @ApiImplicitParam：定义在@ApiImplicitParams注解中，定义单个参数详细信息
		○ paramType：参数放在哪个地方
			§ header-->请求参数的获取：@RequestHeader
			§ query-->请求参数的获取：@RequestParam
			§ path（用于restful接口）-->请求参数的获取：@PathVariable
			§ body（以流的形式提交 仅支持POST）
			§ form（以form表单的形式提交 仅支持POST）
		○ name：参数名
		○ dataType：参数的数据类型 只作为标志说明，并没有实际验证
			§ Long
			§ String
		○ required：参数是否必须传
			§ true
			§ false
		○ value：参数的意义
		○ defaultValue：参数的默认值

- @ApiModel：描述一个Swagger Model的额外信息
	@ApiModel用在类上，表示对类进行说明，用于实体类中的参数接收说明
	
- @ApiModelProperty：在model类的属性添加属性说明

- @ApiParam：用于Controller中方法的参数说明

- @ApiResponses：包装器：包含多个ApiResponse对象列表
	
- @ApiResponse：定义在@ApiResponses注解中，一般用于描述一个错误的响应信息
		○ code：错误码，例如400
		○ message：信息，例如"请求参数没填好"
		○ response：抛出异常的类
		
- @Authorization	Declares an authorization scheme to be used on a resource or an operation.

- @AuthorizationScope	Describes an OAuth2 authorization scope.
