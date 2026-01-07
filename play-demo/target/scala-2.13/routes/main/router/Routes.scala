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
  HomeController_4: controllers.HomeController,
  // @LINE:17
  UserController_2: controllers.UserController,
  // @LINE:38
  ProductController_1: controllers.ProductController,
  // @LINE:62
  OrderController_0: controllers.OrderController,
  // @LINE:76
  Assets_3: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

  @javax.inject.Inject()
  def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:9
    HomeController_4: controllers.HomeController,
    // @LINE:17
    UserController_2: controllers.UserController,
    // @LINE:38
    ProductController_1: controllers.ProductController,
    // @LINE:62
    OrderController_0: controllers.OrderController,
    // @LINE:76
    Assets_3: controllers.Assets
  ) = this(errorHandler, HomeController_4, UserController_2, ProductController_1, OrderController_0, Assets_3, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_4, UserController_2, ProductController_1, OrderController_0, Assets_3, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """health""", """controllers.HomeController.health()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """info""", """controllers.HomeController.appInfo()"""),
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
  ).foldLeft(Seq.empty[(String, String, String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String, String, String)]
    case l => s ++ l.asInstanceOf[List[(String, String, String)]]
  }}


  // @LINE:9
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_4.index(),
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
    HomeController_4.health(),
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
    HomeController_4.appInfo(),
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

  // @LINE:17
  private[this] lazy val controllers_UserController_list3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UserController_list3_invoker = createInvoker(
    UserController_2.list(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "list",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/users""",
      """ ----------------------
 用户相关接口 (User API)
 ----------------------
 获取用户列表（支持分页）""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val controllers_UserController_getById4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_getById4_invoker = createInvoker(
    UserController_2.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """ 根据 ID 获取用户""",
      Seq()
    )
  )

  // @LINE:23
  private[this] lazy val controllers_UserController_searchByName5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/search")))
  )
  private[this] lazy val controllers_UserController_searchByName5_invoker = createInvoker(
    UserController_2.searchByName(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "searchByName",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/users/search""",
      """ 根据用户名搜索""",
      Seq()
    )
  )

  // @LINE:26
  private[this] lazy val controllers_UserController_create6_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val controllers_UserController_create6_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserController_2.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/users""",
      """ 创建用户""",
      Seq()
    )
  )

  // @LINE:29
  private[this] lazy val controllers_UserController_update7_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_update7_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      UserController_2.update(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "update",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """ 更新用户""",
      Seq()
    )
  )

  // @LINE:32
  private[this] lazy val controllers_UserController_delete8_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_UserController_delete8_invoker = createInvoker(
    UserController_2.delete(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "delete",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/users/""" + "$" + """id<[^/]+>""",
      """ 删除用户""",
      Seq()
    )
  )

  // @LINE:38
  private[this] lazy val controllers_ProductController_list9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products")))
  )
  private[this] lazy val controllers_ProductController_list9_invoker = createInvoker(
    ProductController_1.list(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "list",
      Nil,
      "GET",
      this.prefix + """api/products""",
      """ ----------------------
 产品相关接口 (Product API)
 ----------------------
 获取产品列表""",
      Seq()
    )
  )

  // @LINE:41
  private[this] lazy val controllers_ProductController_getById10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_getById10_invoker = createInvoker(
    ProductController_1.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """ 根据 ID 获取产品""",
      Seq()
    )
  )

  // @LINE:44
  private[this] lazy val controllers_ProductController_getByCategory11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/category/"), DynamicPart("category", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_getByCategory11_invoker = createInvoker(
    ProductController_1.getByCategory(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "getByCategory",
      Seq(classOf[String]),
      "GET",
      this.prefix + """api/products/category/""" + "$" + """category<[^/]+>""",
      """ 根据类别筛选产品""",
      Seq()
    )
  )

  // @LINE:47
  private[this] lazy val controllers_ProductController_create12_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products")))
  )
  private[this] lazy val controllers_ProductController_create12_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_1.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/products""",
      """ 创建产品""",
      Seq()
    )
  )

  // @LINE:50
  private[this] lazy val controllers_ProductController_batchCreate13_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/batch")))
  )
  private[this] lazy val controllers_ProductController_batchCreate13_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_1.batchCreate(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "batchCreate",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/products/batch""",
      """ 批量创建产品""",
      Seq()
    )
  )

  // @LINE:53
  private[this] lazy val controllers_ProductController_update14_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_update14_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      ProductController_1.update(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "update",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PUT",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """ 更新产品""",
      Seq()
    )
  )

  // @LINE:56
  private[this] lazy val controllers_ProductController_delete15_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/products/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProductController_delete15_invoker = createInvoker(
    ProductController_1.delete(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProductController",
      "delete",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/products/""" + "$" + """id<[^/]+>""",
      """ 删除产品""",
      Seq()
    )
  )

  // @LINE:62
  private[this] lazy val controllers_OrderController_list16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders")))
  )
  private[this] lazy val controllers_OrderController_list16_invoker = createInvoker(
    OrderController_0.list(fakeValue[java.util.Optional[String]]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "list",
      Seq(classOf[java.util.Optional[String]]),
      "GET",
      this.prefix + """api/orders""",
      """ ----------------------
 订单相关接口 (Order API)
 ----------------------
 获取订单列表""",
      Seq()
    )
  )

  // @LINE:65
  private[this] lazy val controllers_OrderController_getById17_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_OrderController_getById17_invoker = createInvoker(
    OrderController_0.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/orders/""" + "$" + """id<[^/]+>""",
      """ 根据 ID 获取订单详情""",
      Seq()
    )
  )

  // @LINE:68
  private[this] lazy val controllers_OrderController_create18_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders")))
  )
  private[this] lazy val controllers_OrderController_create18_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      OrderController_0.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """api/orders""",
      """ 创建订单""",
      Seq()
    )
  )

  // @LINE:71
  private[this] lazy val controllers_OrderController_updateStatus19_route = Route("PATCH",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/orders/"), DynamicPart("id", """[^/]+""",true), StaticPart("/status")))
  )
  private[this] lazy val controllers_OrderController_updateStatus19_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      OrderController_0.updateStatus(fakeValue[play.mvc.Http.Request], fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.OrderController",
      "updateStatus",
      Seq(classOf[play.mvc.Http.Request], classOf[Long]),
      "PATCH",
      this.prefix + """api/orders/""" + "$" + """id<[^/]+>/status""",
      """ 更新订单状态""",
      Seq()
    )
  )

  // @LINE:76
  private[this] lazy val controllers_Assets_versioned20_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned20_invoker = createInvoker(
    Assets_3.versioned(fakeValue[String], fakeValue[Asset]),
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
        controllers_HomeController_index0_invoker.call(HomeController_4.index())
      }
  
    // @LINE:10
    case controllers_HomeController_health1_route(params@_) =>
      call { 
        controllers_HomeController_health1_invoker.call(HomeController_4.health())
      }
  
    // @LINE:11
    case controllers_HomeController_appInfo2_route(params@_) =>
      call { 
        controllers_HomeController_appInfo2_invoker.call(HomeController_4.appInfo())
      }
  
    // @LINE:17
    case controllers_UserController_list3_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(1)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_UserController_list3_invoker.call(UserController_2.list(page, size))
      }
  
    // @LINE:20
    case controllers_UserController_getById4_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_getById4_invoker.call(UserController_2.getById(id))
      }
  
    // @LINE:23
    case controllers_UserController_searchByName5_route(params@_) =>
      call(params.fromQuery[String]("name", None)) { (name) =>
        controllers_UserController_searchByName5_invoker.call(UserController_2.searchByName(name))
      }
  
    // @LINE:26
    case controllers_UserController_create6_route(params@_) =>
      call { 
        controllers_UserController_create6_invoker.call(
          req => UserController_2.create(req))
      }
  
    // @LINE:29
    case controllers_UserController_update7_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_update7_invoker.call(
          req => UserController_2.update(req, id))
      }
  
    // @LINE:32
    case controllers_UserController_delete8_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_UserController_delete8_invoker.call(UserController_2.delete(id))
      }
  
    // @LINE:38
    case controllers_ProductController_list9_route(params@_) =>
      call { 
        controllers_ProductController_list9_invoker.call(ProductController_1.list())
      }
  
    // @LINE:41
    case controllers_ProductController_getById10_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_getById10_invoker.call(ProductController_1.getById(id))
      }
  
    // @LINE:44
    case controllers_ProductController_getByCategory11_route(params@_) =>
      call(params.fromPath[String]("category", None)) { (category) =>
        controllers_ProductController_getByCategory11_invoker.call(ProductController_1.getByCategory(category))
      }
  
    // @LINE:47
    case controllers_ProductController_create12_route(params@_) =>
      call { 
        controllers_ProductController_create12_invoker.call(
          req => ProductController_1.create(req))
      }
  
    // @LINE:50
    case controllers_ProductController_batchCreate13_route(params@_) =>
      call { 
        controllers_ProductController_batchCreate13_invoker.call(
          req => ProductController_1.batchCreate(req))
      }
  
    // @LINE:53
    case controllers_ProductController_update14_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_update14_invoker.call(
          req => ProductController_1.update(req, id))
      }
  
    // @LINE:56
    case controllers_ProductController_delete15_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_ProductController_delete15_invoker.call(ProductController_1.delete(id))
      }
  
    // @LINE:62
    case controllers_OrderController_list16_route(params@_) =>
      call(params.fromQuery[java.util.Optional[String]]("status", None)) { (status) =>
        controllers_OrderController_list16_invoker.call(OrderController_0.list(status))
      }
  
    // @LINE:65
    case controllers_OrderController_getById17_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_OrderController_getById17_invoker.call(OrderController_0.getById(id))
      }
  
    // @LINE:68
    case controllers_OrderController_create18_route(params@_) =>
      call { 
        controllers_OrderController_create18_invoker.call(
          req => OrderController_0.create(req))
      }
  
    // @LINE:71
    case controllers_OrderController_updateStatus19_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_OrderController_updateStatus19_invoker.call(
          req => OrderController_0.updateStatus(req, id))
      }
  
    // @LINE:76
    case controllers_Assets_versioned20_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned20_invoker.call(Assets_3.versioned(path, file))
      }
  }
}
