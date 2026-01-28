// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:9
package controllers {

  // @LINE:21
  class ReverseUserManagementController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:36
    def getUsersByStatus(enabled:Boolean, page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/users/by-status" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[Boolean]].unbind("enabled", enabled)), if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:27
    def getUserByUsername(username:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/users/username/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("username", username)))
    }
  
    // @LINE:24
    def getUserById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:45
    def deleteUsers(): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/users/batch")
    }
  
    // @LINE:21
    def createUser(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/management/users")
    }
  
    // @LINE:42
    def deleteUser(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:39
    def updateUser(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/management/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:30
    def getAllUsers(page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/users" + play.core.routing.queryString(List(if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:33
    def searchUsers(keyword:String, page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/users/search" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("keyword", keyword)), if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
  }

  // @LINE:140
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:140
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:78
  class ReversePermissionManagementController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:87
    def getAllPermissions(page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/permissions" + play.core.routing.queryString(List(if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:102
    def deletePermissions(): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/permissions/batch")
    }
  
    // @LINE:90
    def searchPermissions(keyword:String, page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/permissions/search" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("keyword", keyword)), if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:99
    def deletePermission(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/permissions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:84
    def getPermissionByCode(code:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/permissions/code/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("code", code)))
    }
  
    // @LINE:96
    def updatePermission(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/management/permissions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:93
    def getPermissionsByResource(resource:String, page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/permissions/by-resource" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("resource", resource)), if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:81
    def getPermissionById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/permissions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:78
    def createPermission(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/management/permissions")
    }
  
  }

  // @LINE:51
  class ReverseRoleManagementController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:54
    def getRoleById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/roles/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:57
    def getRoleByCode(code:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/roles/code/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("code", code)))
    }
  
    // @LINE:60
    def getAllRoles(page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/roles" + play.core.routing.queryString(List(if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:51
    def createRole(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/management/roles")
    }
  
    // @LINE:63
    def searchRoles(keyword:String, page:Int = 0, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/management/roles/search" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("keyword", keyword)), if(page == 0) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:66
    def updateRole(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/management/roles/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:72
    def deleteRoles(): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/roles/batch")
    }
  
    // @LINE:69
    def deleteRole(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/management/roles/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
  }

  // @LINE:121
  class ReverseProductController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:125
    def batchCreate(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/products/batch")
    }
  
    // @LINE:127
    def delete(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:123
    def getByCategory(category:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products/category/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("category", category)))
    }
  
    // @LINE:126
    def update(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:122
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:121
    def list(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/products")
    }
  
    // @LINE:124
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/products")
    }
  
  }

  // @LINE:111
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:116
    def delete(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:111
    def list(page:Int = 1, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users" + play.core.routing.queryString(List(if(page == 1) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:115
    def update(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:112
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:113
    def searchByName(name:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/users/search" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[String]].unbind("name", name)))))
    }
  
    // @LINE:114
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

  // @LINE:132
  class ReverseOrderController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:132
    def list(status:java.util.Optional[String]): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/orders" + play.core.routing.queryString(List(Some(implicitly[play.api.mvc.QueryStringBindable[java.util.Optional[String]]].unbind("status", status)))))
    }
  
    // @LINE:133
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:134
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/orders")
    }
  
    // @LINE:135
    def updateStatus(id:Long): Call = {
      
      Call("PATCH", _prefix + { _defaultPrefix } + "api/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)) + "/status")
    }
  
  }


}
