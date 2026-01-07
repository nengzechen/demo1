// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:9
package controllers {

  // @LINE:76
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:76
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:38
  class ReverseProductController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:50
    def batchCreate(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/products/batch")
    }
  
    // @LINE:56
    def delete(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:44
    def getByCategory(category:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products/category/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("category", category)))
    }
  
    // @LINE:53
    def update(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:41
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:38
    def list(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products")
    }
  
    // @LINE:47
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/products")
    }
  
  }

  // @LINE:17
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:32
    def delete(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:17
    def list(page:Int = 1, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users" + play.core.routing.queryString(List(if(page == 1) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:29
    def update(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:20
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:23
    def searchByName(name:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/search" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("name", name)))))
    }
  
    // @LINE:26
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/users")
    }
  
  }

  // @LINE:9
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:10
    def health(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "health")
    }
  
    // @LINE:11
    def appInfo(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "info")
    }
  
  }

  // @LINE:62
  class ReverseOrderController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:62
    def list(status:java.util.Optional[String]): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/orders" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[java.util.Optional[String]]].unbind("status", status)))))
    }
  
    // @LINE:65
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:68
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/orders")
    }
  
    // @LINE:71
    def updateStatus(id:Long): Call = {
      
      Call("PATCH", _prefix + { _defaultPrefix } + "api/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)) + "/status")
    }
  
  }


}
