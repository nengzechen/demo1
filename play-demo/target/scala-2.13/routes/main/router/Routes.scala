// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:9
  HomeController_7: controllers.HomeController,
  // @LINE:21
  UserManagementController_3: controllers.UserManagementController,
  // @LINE:51
  RoleManagementController_0: controllers.RoleManagementController,
  // @LINE:78
  PermissionManagementController_2: controllers.PermissionManagementController,
  // @LINE:111
  UserController_5: controllers.UserController,
  // @LINE:121
  ProductController_4: controllers.ProductController,
  // @LINE:132
  OrderController_1: controllers.OrderController,
  // @LINE:140
  Assets_6: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:9
    HomeController_7: controllers.HomeController,
    // @LINE:21
    UserManagementController_3: controllers.UserManagementController,
    // @LINE:51
    RoleManagementController_0: controllers.RoleManagementController,
    // @LINE:78
    PermissionManagementController_2: controllers.PermissionManagementController,
    // @LINE:111
    UserController_5: controllers.UserController,
    // @LINE:121
    ProductController_4: controllers.ProductController,
    // @LINE:132
    OrderController_1: controllers.OrderController,
    // @LINE:140
    Assets_6: controllers.Assets
  ) = this(errorHandler, HomeController_7, UserManagementController_3, RoleManagementController_0, PermissionManagementController_2, UserController_5, ProductController_4, OrderController_1, Assets_6, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_7, UserManagementController_3, RoleManagementController_0, PermissionManagementController_2, UserController_5, ProductController_4, OrderController_1, Assets_6, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """health""", """controllers.HomeController.health()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """info""", """controllers.HomeController.appInfo()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users""", """controllers.UserManagementController.createUser(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/""" + "$" + """id<[^/]+>""", """controllers.UserManagementController.getUserById(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/username/""" + "$" + """username<[^/]+>""", """controllers.UserManagementController.getUserByUsername(username:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users""", """controllers.UserManagementController.getAllUsers(page:Int ?= 0, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/search""", """controllers.UserManagementController.searchUsers(keyword:String, page:Int ?= 0, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/by-status""", """controllers.UserManagementController.getUsersByStatus(enabled:Boolean, page:Int ?= 0, size:Int ?= 10)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/""" + "$" + """id<[^/]+>""", """controllers.UserManagementController.updateUser(request:Request, id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/""" + "$" + """id<[^/]+>""", """controllers.UserManagementController.deleteUser(id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/users/batch""", """controllers.UserManagementController.deleteUsers(request:Request)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles""", """controllers.RoleManagementController.createRole(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/""" + "$" + """id<[^/]+>""", """controllers.RoleManagementController.getRoleById(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/code/""" + "$" + """code<[^/]+>""", """controllers.RoleManagementController.getRoleByCode(code:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles""", """controllers.RoleManagementController.getAllRoles(page:Int ?= 0, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/search""", """controllers.RoleManagementController.searchRoles(keyword:String, page:Int ?= 0, size:Int ?= 10)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/""" + "$" + """id<[^/]+>""", """controllers.RoleManagementController.updateRole(request:Request, id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/""" + "$" + """id<[^/]+>""", """controllers.RoleManagementController.deleteRole(id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/roles/batch""", """controllers.RoleManagementController.deleteRoles(request:Request)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions""", """controllers.PermissionManagementController.createPermission(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/""" + "$" + """id<[^/]+>""", """controllers.PermissionManagementController.getPermissionById(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/code/""" + "$" + """code<[^/]+>""", """controllers.PermissionManagementController.getPermissionByCode(code:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions""", """controllers.PermissionManagementController.getAllPermissions(page:Int ?= 0, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/search""", """controllers.PermissionManagementController.searchPermissions(keyword:String, page:Int ?= 0, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/by-resource""", """controllers.PermissionManagementController.getPermissionsByResource(resource:String, page:Int ?= 0, size:Int ?= 10)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/""" + "$" + """id<[^/]+>""", """controllers.PermissionManagementController.updatePermission(request:Request, id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/""" + "$" + """id<[^/]+>""", """controllers.PermissionManagementController.deletePermission(id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/management/permissions/batch""", """controllers.PermissionManagementController.deletePermissions(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users""", """controllers.UserController.list(page:Int ?= 1, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """id<[^/]+>""", """controllers.UserController.getById(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/search""", """controllers.UserController.searchByName(name:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users""", """controllers.UserController.create(request:Request)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """id<[^/]+>""", """controllers.UserController.update(request:Request, id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users/""" + "$" + """id<[^/]+>""", """controllers.UserController.delete(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products""", """controllers.ProductController.list()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products/""" + "$" + """id<[^/]+>""", """controllers.ProductController.getById(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products/category/""" + "$" + """category<[^/]+>""", """controllers.ProductController.getByCategory(category:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products""", """controllers.ProductController.create(request:Request)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products/batch""", """controllers.ProductController.batchCreate(request:Request)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products/""" + "$" + """id<[^/]+>""", """controllers.ProductController.update(request:Request, id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/products/""" + "$" + """id<[^/]+>""", """controllers.ProductController.delete(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/orders""", """controllers.OrderController.list(status:java.util.Optional[String])"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/orders/""" + "$" + """id<[^/]+>""", """controllers.OrderController.getById(id:Long)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/orders""", """controllers.OrderController.create(request:Request)"""),
    ("""PATCH""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/orders/""" + "$" + """id<[^/]+>/status""", """controllers.OrderController.updateStatus(request:Request, id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:9
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_7.index(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ ----------------------
 首页路由
 ----------------------""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_HomeController_health1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("health")))
  )
  private[this] lazy val controllers_HomeController_health1_invoker = createInvoker(
    HomeController_7.health(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "health",
      Nil,
      "GET",
      this.prefix + """health""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_HomeController_appInfo2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("info")))
  )
  private[this] lazy val controllers_HomeController_appInfo2_invoker = createInvoker(
    HomeController_7.appInfo(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "appInfo",
      Nil,
      "GET",
      this.prefix + """info""",
      """""",
      Seq()
    )
  )

  // @LINE:21
  private[this] lazy val controllers_UserManagementController_createUser3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users")))
  )
  private[this] lazy val controllers_UserManagementController_createUser3_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserManagementController_3.createUser(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "createUser",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/management/users""",
      """ ----------------------
 用户管理接口 (User Management API)
 ----------------------
 创建用户""",
      Seq()
    )
  )

  // @LINE:24
  private[this] lazy val controllers_UserManagementController_getUserById4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserManagementController_getUserById4_invoker = createInvoker(
    UserManagementController_3.getUserById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "getUserById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/management/users/""" + "$" + """id<[^/]+>""",
      """ 获取用户详情""",
      Seq()
    )
  )

  // @LINE:27
  private[this] lazy val controllers_UserManagementController_getUserByUsername5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/username/"), DynamicPart("username", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserManagementController_getUserByUsername5_invoker = createInvoker(
    UserManagementController_3.getUserByUsername(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "getUserByUsername",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/management/users/username/""" + "$" + """username<[^/]+>""",
      """ 根据用户名获取用户""",
      Seq()
    )
  )

  // @LINE:30
  private[this] lazy val controllers_UserManagementController_getAllUsers6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users")))
  )
  private[this] lazy val controllers_UserManagementController_getAllUsers6_invoker = createInvoker(
    UserManagementController_3.getAllUsers(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "getAllUsers",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/users""",
      """ 获取用户列表（分页）""",
      Seq()
    )
  )

  // @LINE:33
  private[this] lazy val controllers_UserManagementController_searchUsers7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/search")))
  )
  private[this] lazy val controllers_UserManagementController_searchUsers7_invoker = createInvoker(
    UserManagementController_3.searchUsers(fakeValue[String], fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "searchUsers",
      Seq(classOf[String], classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/users/search""",
      """ 搜索用户""",
      Seq()
    )
  )

  // @LINE:36
  private[this] lazy val controllers_UserManagementController_getUsersByStatus8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/by-status")))
  )
  private[this] lazy val controllers_UserManagementController_getUsersByStatus8_invoker = createInvoker(
    UserManagementController_3.getUsersByStatus(fakeValue[Boolean], fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "getUsersByStatus",
      Seq(classOf[Boolean], classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/users/by-status""",
      """ 根据状态查询用户""",
      Seq()
    )
  )

  // @LINE:39
  private[this] lazy val controllers_UserManagementController_updateUser9_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserManagementController_updateUser9_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserManagementController_3.updateUser(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "updateUser",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/management/users/""" + "$" + """id<[^/]+>""",
      """ 更新用户""",
      Seq()
    )
  )

  // @LINE:42
  private[this] lazy val controllers_UserManagementController_deleteUser10_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserManagementController_deleteUser10_invoker = createInvoker(
    UserManagementController_3.deleteUser(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "deleteUser",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/management/users/""" + "$" + """id<[^/]+>""",
      """ 删除用户""",
      Seq()
    )
  )

  // @LINE:45
  private[this] lazy val controllers_UserManagementController_deleteUsers11_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/users/batch")))
  )
  private[this] lazy val controllers_UserManagementController_deleteUsers11_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserManagementController_3.deleteUsers(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserManagementController",
      "deleteUsers",
      Seq(classOf[play.mvc.Http.Request]),
      "DELETE",
      this.prefix + """api/management/users/batch""",
      """ 批量删除用户""",
      Seq()
    )
  )

  // @LINE:51
  private[this] lazy val controllers_RoleManagementController_createRole12_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles")))
  )
  private[this] lazy val controllers_RoleManagementController_createRole12_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      RoleManagementController_0.createRole(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "createRole",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/management/roles""",
      """ ----------------------
 角色管理接口 (Role Management API)
 ----------------------
 创建角色""",
      Seq()
    )
  )

  // @LINE:54
  private[this] lazy val controllers_RoleManagementController_getRoleById13_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_RoleManagementController_getRoleById13_invoker = createInvoker(
    RoleManagementController_0.getRoleById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "getRoleById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/management/roles/""" + "$" + """id<[^/]+>""",
      """ 获取角色详情""",
      Seq()
    )
  )

  // @LINE:57
  private[this] lazy val controllers_RoleManagementController_getRoleByCode14_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/code/"), DynamicPart("code", """[^/]+""",true)))
  )
  private[this] lazy val controllers_RoleManagementController_getRoleByCode14_invoker = createInvoker(
    RoleManagementController_0.getRoleByCode(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "getRoleByCode",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/management/roles/code/""" + "$" + """code<[^/]+>""",
      """ 根据编码获取角色""",
      Seq()
    )
  )

  // @LINE:60
  private[this] lazy val controllers_RoleManagementController_getAllRoles15_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles")))
  )
  private[this] lazy val controllers_RoleManagementController_getAllRoles15_invoker = createInvoker(
    RoleManagementController_0.getAllRoles(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "getAllRoles",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/roles""",
      """ 获取角色列表（分页）""",
      Seq()
    )
  )

  // @LINE:63
  private[this] lazy val controllers_RoleManagementController_searchRoles16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/search")))
  )
  private[this] lazy val controllers_RoleManagementController_searchRoles16_invoker = createInvoker(
    RoleManagementController_0.searchRoles(fakeValue[String], fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "searchRoles",
      Seq(classOf[String], classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/roles/search""",
      """ 搜索角色""",
      Seq()
    )
  )

  // @LINE:66
  private[this] lazy val controllers_RoleManagementController_updateRole17_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_RoleManagementController_updateRole17_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      RoleManagementController_0.updateRole(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "updateRole",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/management/roles/""" + "$" + """id<[^/]+>""",
      """ 更新角色""",
      Seq()
    )
  )

  // @LINE:69
  private[this] lazy val controllers_RoleManagementController_deleteRole18_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_RoleManagementController_deleteRole18_invoker = createInvoker(
    RoleManagementController_0.deleteRole(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "deleteRole",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/management/roles/""" + "$" + """id<[^/]+>""",
      """ 删除角色""",
      Seq()
    )
  )

  // @LINE:72
  private[this] lazy val controllers_RoleManagementController_deleteRoles19_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/roles/batch")))
  )
  private[this] lazy val controllers_RoleManagementController_deleteRoles19_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      RoleManagementController_0.deleteRoles(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.RoleManagementController",
      "deleteRoles",
      Seq(classOf[play.mvc.Http.Request]),
      "DELETE",
      this.prefix + """api/management/roles/batch""",
      """ 批量删除角色""",
      Seq()
    )
  )

  // @LINE:78
  private[this] lazy val controllers_PermissionManagementController_createPermission20_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions")))
  )
  private[this] lazy val controllers_PermissionManagementController_createPermission20_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PermissionManagementController_2.createPermission(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "createPermission",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/management/permissions""",
      """ ----------------------
 权限管理接口 (Permission Management API)
 ----------------------
 创建权限""",
      Seq()
    )
  )

  // @LINE:81
  private[this] lazy val controllers_PermissionManagementController_getPermissionById21_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_PermissionManagementController_getPermissionById21_invoker = createInvoker(
    PermissionManagementController_2.getPermissionById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "getPermissionById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/management/permissions/""" + "$" + """id<[^/]+>""",
      """ 获取权限详情""",
      Seq()
    )
  )

  // @LINE:84
  private[this] lazy val controllers_PermissionManagementController_getPermissionByCode22_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/code/"), DynamicPart("code", """[^/]+""",true)))
  )
  private[this] lazy val controllers_PermissionManagementController_getPermissionByCode22_invoker = createInvoker(
    PermissionManagementController_2.getPermissionByCode(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "getPermissionByCode",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/management/permissions/code/""" + "$" + """code<[^/]+>""",
      """ 根据编码获取权限""",
      Seq()
    )
  )

  // @LINE:87
  private[this] lazy val controllers_PermissionManagementController_getAllPermissions23_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions")))
  )
  private[this] lazy val controllers_PermissionManagementController_getAllPermissions23_invoker = createInvoker(
    PermissionManagementController_2.getAllPermissions(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "getAllPermissions",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/permissions""",
      """ 获取权限列表（分页）""",
      Seq()
    )
  )

  // @LINE:90
  private[this] lazy val controllers_PermissionManagementController_searchPermissions24_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/search")))
  )
  private[this] lazy val controllers_PermissionManagementController_searchPermissions24_invoker = createInvoker(
    PermissionManagementController_2.searchPermissions(fakeValue[String], fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "searchPermissions",
      Seq(classOf[String], classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/permissions/search""",
      """ 搜索权限""",
      Seq()
    )
  )

  // @LINE:93
  private[this] lazy val controllers_PermissionManagementController_getPermissionsByResource25_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/by-resource")))
  )
  private[this] lazy val controllers_PermissionManagementController_getPermissionsByResource25_invoker = createInvoker(
    PermissionManagementController_2.getPermissionsByResource(fakeValue[String], fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "getPermissionsByResource",
      Seq(classOf[String], classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/management/permissions/by-resource""",
      """ 根据资源查询权限""",
      Seq()
    )
  )

  // @LINE:96
  private[this] lazy val controllers_PermissionManagementController_updatePermission26_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_PermissionManagementController_updatePermission26_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PermissionManagementController_2.updatePermission(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "updatePermission",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/management/permissions/""" + "$" + """id<[^/]+>""",
      """ 更新权限""",
      Seq()
    )
  )

  // @LINE:99
  private[this] lazy val controllers_PermissionManagementController_deletePermission27_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_PermissionManagementController_deletePermission27_invoker = createInvoker(
    PermissionManagementController_2.deletePermission(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "deletePermission",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/management/permissions/""" + "$" + """id<[^/]+>""",
      """ 删除权限""",
      Seq()
    )
  )

  // @LINE:102
  private[this] lazy val controllers_PermissionManagementController_deletePermissions28_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/management/permissions/batch")))
  )
  private[this] lazy val controllers_PermissionManagementController_deletePermissions28_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PermissionManagementController_2.deletePermissions(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.PermissionManagementController",
      "deletePermissions",
      Seq(classOf[play.mvc.Http.Request]),
      "DELETE",
      this.prefix + """api/management/permissions/batch""",
      """ 批量删除权限""",
      Seq()
    )
  )

  // @LINE:111
  private[this] lazy val controllers_UserController_list29_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UserController_list29_invoker = createInvoker(
    UserController_5.list(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "list",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/users""",
      """ ----------------------
 用户相关接口 (User API - Legacy)
 ----------------------""",
      Seq()
    )
  )

  // @LINE:112
  private[this] lazy val controllers_UserController_getById30_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_getById30_invoker = createInvoker(
    UserController_5.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:113
  private[this] lazy val controllers_UserController_searchByName31_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/search")))
  )
  private[this] lazy val controllers_UserController_searchByName31_invoker = createInvoker(
    UserController_5.searchByName(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "searchByName",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/users/search""",
      """""",
      Seq()
    )
  )

  // @LINE:114
  private[this] lazy val controllers_UserController_create32_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UserController_create32_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserController_5.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/users""",
      """""",
      Seq()
    )
  )

  // @LINE:115
  private[this] lazy val controllers_UserController_update33_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_update33_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserController_5.update(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "update",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:116
  private[this] lazy val controllers_UserController_delete34_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_delete34_invoker = createInvoker(
    UserController_5.delete(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "delete",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:121
  private[this] lazy val controllers_ProductController_list35_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products")))
  )
  private[this] lazy val controllers_ProductController_list35_invoker = createInvoker(
    ProductController_4.list(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "list",
      Nil,
      "GET",
      this.prefix + """api/products""",
      """ ----------------------
 产品相关接口 (Product API)
 ----------------------""",
      Seq()
    )
  )

  // @LINE:122
  private[this] lazy val controllers_ProductController_getById36_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_getById36_invoker = createInvoker(
    ProductController_4.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:123
  private[this] lazy val controllers_ProductController_getByCategory37_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/category/"), DynamicPart("category", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_getByCategory37_invoker = createInvoker(
    ProductController_4.getByCategory(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "getByCategory",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/products/category/""" + "$" + """category<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:124
  private[this] lazy val controllers_ProductController_create38_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products")))
  )
  private[this] lazy val controllers_ProductController_create38_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_4.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/products""",
      """""",
      Seq()
    )
  )

  // @LINE:125
  private[this] lazy val controllers_ProductController_batchCreate39_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/batch")))
  )
  private[this] lazy val controllers_ProductController_batchCreate39_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_4.batchCreate(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "batchCreate",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/products/batch""",
      """""",
      Seq()
    )
  )

  // @LINE:126
  private[this] lazy val controllers_ProductController_update40_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_update40_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_4.update(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "update",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:127
  private[this] lazy val controllers_ProductController_delete41_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_delete41_invoker = createInvoker(
    ProductController_4.delete(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "delete",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:132
  private[this] lazy val controllers_OrderController_list42_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders")))
  )
  private[this] lazy val controllers_OrderController_list42_invoker = createInvoker(
    OrderController_1.list(fakeValue[java.util.Optional[String]]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "list",
      Seq(classOf[java.util.Optional[String]]),
      "GET",
      this.prefix + """api/orders""",
      """ ----------------------
 订单相关接口 (Order API)
 ----------------------""",
      Seq()
    )
  )

  // @LINE:133
  private[this] lazy val controllers_OrderController_getById43_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_OrderController_getById43_invoker = createInvoker(
    OrderController_1.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/orders/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:134
  private[this] lazy val controllers_OrderController_create44_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders")))
  )
  private[this] lazy val controllers_OrderController_create44_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      OrderController_1.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/orders""",
      """""",
      Seq()
    )
  )

  // @LINE:135
  private[this] lazy val controllers_OrderController_updateStatus45_route = Route("PATCH",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders/"), DynamicPart("id", """[^/]+""",true), StaticPart("/status")))
  )
  private[this] lazy val controllers_OrderController_updateStatus45_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      OrderController_1.updateStatus(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "updateStatus",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PATCH",
      this.prefix + """api/orders/""" + "$" + """id<[^/]+>/status""",
      """""",
      Seq()
    )
  )

  // @LINE:140
  private[this] lazy val controllers_Assets_versioned46_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned46_invoker = createInvoker(
    Assets_6.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ ----------------------
 静态资源
 ----------------------""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:9
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_7.index())
      }
  
    // @LINE:10
    case controllers_HomeController_health1_route(params@_) =>
      call { 
        controllers_HomeController_health1_invoker.call(HomeController_7.health())
      }
  
    // @LINE:11
    case controllers_HomeController_appInfo2_route(params@_) =>
      call { 
        controllers_HomeController_appInfo2_invoker.call(HomeController_7.appInfo())
      }
  
    // @LINE:21
    case controllers_UserManagementController_createUser3_route(params@_) =>
      call { 
        controllers_UserManagementController_createUser3_invoker.call(
          req => UserManagementController_3.createUser(req))
      }
  
    // @LINE:24
    case controllers_UserManagementController_getUserById4_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserManagementController_getUserById4_invoker.call(UserManagementController_3.getUserById(id))
      }
  
    // @LINE:27
    case controllers_UserManagementController_getUserByUsername5_route(params@_) =>
      call(params.fromPath[String]("username", None)) { (username) =>
        controllers_UserManagementController_getUserByUsername5_invoker.call(UserManagementController_3.getUserByUsername(username))
      }
  
    // @LINE:30
    case controllers_UserManagementController_getAllUsers6_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_UserManagementController_getAllUsers6_invoker.call(UserManagementController_3.getAllUsers(page, size))
      }
  
    // @LINE:33
    case controllers_UserManagementController_searchUsers7_route(params@_) =>
      call(params.fromQuery[String]("keyword", None), params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (keyword, page, size) =>
        controllers_UserManagementController_searchUsers7_invoker.call(UserManagementController_3.searchUsers(keyword, page, size))
      }
  
    // @LINE:36
    case controllers_UserManagementController_getUsersByStatus8_route(params@_) =>
      call(params.fromQuery[Boolean]("enabled", None), params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (enabled, page, size) =>
        controllers_UserManagementController_getUsersByStatus8_invoker.call(UserManagementController_3.getUsersByStatus(enabled, page, size))
      }
  
    // @LINE:39
    case controllers_UserManagementController_updateUser9_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserManagementController_updateUser9_invoker.call(
          req => UserManagementController_3.updateUser(req, id))
      }
  
    // @LINE:42
    case controllers_UserManagementController_deleteUser10_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserManagementController_deleteUser10_invoker.call(UserManagementController_3.deleteUser(id))
      }
  
    // @LINE:45
    case controllers_UserManagementController_deleteUsers11_route(params@_) =>
      call { 
        controllers_UserManagementController_deleteUsers11_invoker.call(
          req => UserManagementController_3.deleteUsers(req))
      }
  
    // @LINE:51
    case controllers_RoleManagementController_createRole12_route(params@_) =>
      call { 
        controllers_RoleManagementController_createRole12_invoker.call(
          req => RoleManagementController_0.createRole(req))
      }
  
    // @LINE:54
    case controllers_RoleManagementController_getRoleById13_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_RoleManagementController_getRoleById13_invoker.call(RoleManagementController_0.getRoleById(id))
      }
  
    // @LINE:57
    case controllers_RoleManagementController_getRoleByCode14_route(params@_) =>
      call(params.fromPath[String]("code", None)) { (code) =>
        controllers_RoleManagementController_getRoleByCode14_invoker.call(RoleManagementController_0.getRoleByCode(code))
      }
  
    // @LINE:60
    case controllers_RoleManagementController_getAllRoles15_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_RoleManagementController_getAllRoles15_invoker.call(RoleManagementController_0.getAllRoles(page, size))
      }
  
    // @LINE:63
    case controllers_RoleManagementController_searchRoles16_route(params@_) =>
      call(params.fromQuery[String]("keyword", None), params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (keyword, page, size) =>
        controllers_RoleManagementController_searchRoles16_invoker.call(RoleManagementController_0.searchRoles(keyword, page, size))
      }
  
    // @LINE:66
    case controllers_RoleManagementController_updateRole17_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_RoleManagementController_updateRole17_invoker.call(
          req => RoleManagementController_0.updateRole(req, id))
      }
  
    // @LINE:69
    case controllers_RoleManagementController_deleteRole18_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_RoleManagementController_deleteRole18_invoker.call(RoleManagementController_0.deleteRole(id))
      }
  
    // @LINE:72
    case controllers_RoleManagementController_deleteRoles19_route(params@_) =>
      call { 
        controllers_RoleManagementController_deleteRoles19_invoker.call(
          req => RoleManagementController_0.deleteRoles(req))
      }
  
    // @LINE:78
    case controllers_PermissionManagementController_createPermission20_route(params@_) =>
      call { 
        controllers_PermissionManagementController_createPermission20_invoker.call(
          req => PermissionManagementController_2.createPermission(req))
      }
  
    // @LINE:81
    case controllers_PermissionManagementController_getPermissionById21_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_PermissionManagementController_getPermissionById21_invoker.call(PermissionManagementController_2.getPermissionById(id))
      }
  
    // @LINE:84
    case controllers_PermissionManagementController_getPermissionByCode22_route(params@_) =>
      call(params.fromPath[String]("code", None)) { (code) =>
        controllers_PermissionManagementController_getPermissionByCode22_invoker.call(PermissionManagementController_2.getPermissionByCode(code))
      }
  
    // @LINE:87
    case controllers_PermissionManagementController_getAllPermissions23_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_PermissionManagementController_getAllPermissions23_invoker.call(PermissionManagementController_2.getAllPermissions(page, size))
      }
  
    // @LINE:90
    case controllers_PermissionManagementController_searchPermissions24_route(params@_) =>
      call(params.fromQuery[String]("keyword", None), params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (keyword, page, size) =>
        controllers_PermissionManagementController_searchPermissions24_invoker.call(PermissionManagementController_2.searchPermissions(keyword, page, size))
      }
  
    // @LINE:93
    case controllers_PermissionManagementController_getPermissionsByResource25_route(params@_) =>
      call(params.fromQuery[String]("resource", None), params.fromQuery[Int]("page", Some(0)), params.fromQuery[Int]("size", Some(10))) { (resource, page, size) =>
        controllers_PermissionManagementController_getPermissionsByResource25_invoker.call(PermissionManagementController_2.getPermissionsByResource(resource, page, size))
      }
  
    // @LINE:96
    case controllers_PermissionManagementController_updatePermission26_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_PermissionManagementController_updatePermission26_invoker.call(
          req => PermissionManagementController_2.updatePermission(req, id))
      }
  
    // @LINE:99
    case controllers_PermissionManagementController_deletePermission27_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_PermissionManagementController_deletePermission27_invoker.call(PermissionManagementController_2.deletePermission(id))
      }
  
    // @LINE:102
    case controllers_PermissionManagementController_deletePermissions28_route(params@_) =>
      call { 
        controllers_PermissionManagementController_deletePermissions28_invoker.call(
          req => PermissionManagementController_2.deletePermissions(req))
      }
  
    // @LINE:111
    case controllers_UserController_list29_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(1)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_UserController_list29_invoker.call(UserController_5.list(page, size))
      }
  
    // @LINE:112
    case controllers_UserController_getById30_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_getById30_invoker.call(UserController_5.getById(id))
      }
  
    // @LINE:113
    case controllers_UserController_searchByName31_route(params@_) =>
      call(params.fromQuery[String]("name", None)) { (name) =>
        controllers_UserController_searchByName31_invoker.call(UserController_5.searchByName(name))
      }
  
    // @LINE:114
    case controllers_UserController_create32_route(params@_) =>
      call { 
        controllers_UserController_create32_invoker.call(
          req => UserController_5.create(req))
      }
  
    // @LINE:115
    case controllers_UserController_update33_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_update33_invoker.call(
          req => UserController_5.update(req, id))
      }
  
    // @LINE:116
    case controllers_UserController_delete34_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_delete34_invoker.call(UserController_5.delete(id))
      }
  
    // @LINE:121
    case controllers_ProductController_list35_route(params@_) =>
      call { 
        controllers_ProductController_list35_invoker.call(ProductController_4.list())
      }
  
    // @LINE:122
    case controllers_ProductController_getById36_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_getById36_invoker.call(ProductController_4.getById(id))
      }
  
    // @LINE:123
    case controllers_ProductController_getByCategory37_route(params@_) =>
      call(params.fromPath[String]("category", None)) { (category) =>
        controllers_ProductController_getByCategory37_invoker.call(ProductController_4.getByCategory(category))
      }
  
    // @LINE:124
    case controllers_ProductController_create38_route(params@_) =>
      call { 
        controllers_ProductController_create38_invoker.call(
          req => ProductController_4.create(req))
      }
  
    // @LINE:125
    case controllers_ProductController_batchCreate39_route(params@_) =>
      call { 
        controllers_ProductController_batchCreate39_invoker.call(
          req => ProductController_4.batchCreate(req))
      }
  
    // @LINE:126
    case controllers_ProductController_update40_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_update40_invoker.call(
          req => ProductController_4.update(req, id))
      }
  
    // @LINE:127
    case controllers_ProductController_delete41_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_delete41_invoker.call(ProductController_4.delete(id))
      }
  
    // @LINE:132
    case controllers_OrderController_list42_route(params@_) =>
      call(params.fromQuery[java.util.Optional[String]]("status", None)) { (status) =>
        controllers_OrderController_list42_invoker.call(OrderController_1.list(status))
      }
  
    // @LINE:133
    case controllers_OrderController_getById43_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_OrderController_getById43_invoker.call(OrderController_1.getById(id))
      }
  
    // @LINE:134
    case controllers_OrderController_create44_route(params@_) =>
      call { 
        controllers_OrderController_create44_invoker.call(
          req => OrderController_1.create(req))
      }
  
    // @LINE:135
    case controllers_OrderController_updateStatus45_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_OrderController_updateStatus45_invoker.call(
          req => OrderController_1.updateStatus(req, id))
      }
  
    // @LINE:140
    case controllers_Assets_versioned46_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned46_invoker.call(Assets_6.versioned(path, file))
      }
  }
}
