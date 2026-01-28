// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:9
package controllers.javascript {

  // @LINE:21
  class ReverseUserManagementController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:36
    def getUsersByStatus: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.getUsersByStatus",
      """
        function(enabled0,page1,size2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/by-status" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[Boolean]].javascriptUnbind + """)("enabled", enabled0), (page1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page1)), (size2 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size2))])})
        }
      """
    )
  
    // @LINE:27
    def getUserByUsername: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.getUserByUsername",
      """
        function(username0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/username/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("username", username0))})
        }
      """
    )
  
    // @LINE:24
    def getUserById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.getUserById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:45
    def deleteUsers: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.deleteUsers",
      """
        function() {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/batch"})
        }
      """
    )
  
    // @LINE:21
    def createUser: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.createUser",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users"})
        }
      """
    )
  
    // @LINE:42
    def deleteUser: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.deleteUser",
      """
        function(id0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:39
    def updateUser: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.updateUser",
      """
        function(id0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:30
    def getAllUsers: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.getAllUsers",
      """
        function(page0,size1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users" + _qS([(page0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page0)), (size1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size1))])})
        }
      """
    )
  
    // @LINE:33
    def searchUsers: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserManagementController.searchUsers",
      """
        function(keyword0,page1,size2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/users/search" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("keyword", keyword0), (page1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page1)), (size2 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size2))])})
        }
      """
    )
  
  }

  // @LINE:140
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:140
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:78
  class ReversePermissionManagementController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:87
    def getAllPermissions: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.getAllPermissions",
      """
        function(page0,size1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions" + _qS([(page0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page0)), (size1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size1))])})
        }
      """
    )
  
    // @LINE:102
    def deletePermissions: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.deletePermissions",
      """
        function() {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/batch"})
        }
      """
    )
  
    // @LINE:90
    def searchPermissions: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.searchPermissions",
      """
        function(keyword0,page1,size2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/search" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("keyword", keyword0), (page1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page1)), (size2 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size2))])})
        }
      """
    )
  
    // @LINE:99
    def deletePermission: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.deletePermission",
      """
        function(id0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:84
    def getPermissionByCode: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.getPermissionByCode",
      """
        function(code0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/code/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("code", code0))})
        }
      """
    )
  
    // @LINE:96
    def updatePermission: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.updatePermission",
      """
        function(id0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:93
    def getPermissionsByResource: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.getPermissionsByResource",
      """
        function(resource0,page1,size2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/by-resource" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("resource", resource0), (page1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page1)), (size2 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size2))])})
        }
      """
    )
  
    // @LINE:81
    def getPermissionById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.getPermissionById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:78
    def createPermission: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.PermissionManagementController.createPermission",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/permissions"})
        }
      """
    )
  
  }

  // @LINE:51
  class ReverseRoleManagementController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:54
    def getRoleById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.getRoleById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:57
    def getRoleByCode: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.getRoleByCode",
      """
        function(code0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/code/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("code", code0))})
        }
      """
    )
  
    // @LINE:60
    def getAllRoles: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.getAllRoles",
      """
        function(page0,size1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles" + _qS([(page0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page0)), (size1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size1))])})
        }
      """
    )
  
    // @LINE:51
    def createRole: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.createRole",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles"})
        }
      """
    )
  
    // @LINE:63
    def searchRoles: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.searchRoles",
      """
        function(keyword0,page1,size2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/search" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("keyword", keyword0), (page1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page1)), (size2 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size2))])})
        }
      """
    )
  
    // @LINE:66
    def updateRole: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.updateRole",
      """
        function(id0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:72
    def deleteRoles: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.deleteRoles",
      """
        function() {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/batch"})
        }
      """
    )
  
    // @LINE:69
    def deleteRole: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.RoleManagementController.deleteRole",
      """
        function(id0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/management/roles/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
  }

  // @LINE:121
  class ReverseProductController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:125
    def batchCreate: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.batchCreate",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products/batch"})
        }
      """
    )
  
    // @LINE:127
    def delete: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.delete",
      """
        function(id0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:123
    def getByCategory: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.getByCategory",
      """
        function(category0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products/category/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("category", category0))})
        }
      """
    )
  
    // @LINE:126
    def update: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.update",
      """
        function(id0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:122
    def getById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.getById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:121
    def list: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.list",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products"})
        }
      """
    )
  
    // @LINE:124
    def create: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProductController.create",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/products"})
        }
      """
    )
  
  }

  // @LINE:111
  class ReverseUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:116
    def delete: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.delete",
      """
        function(id0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:111
    def list: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.list",
      """
        function(page0,size1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users" + _qS([(page0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("page", page0)), (size1 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Int]].javascriptUnbind + """)("size", size1))])})
        }
      """
    )
  
    // @LINE:115
    def update: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.update",
      """
        function(id0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:112
    def getById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.getById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:113
    def searchByName: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.searchByName",
      """
        function(name0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users/search" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[String]].javascriptUnbind + """)("name", name0)])})
        }
      """
    )
  
    // @LINE:114
    def create: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.create",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users"})
        }
      """
    )
  
  }

  // @LINE:9
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
    // @LINE:10
    def health: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.health",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "health"})
        }
      """
    )
  
    // @LINE:11
    def appInfo: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.appInfo",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "info"})
        }
      """
    )
  
  }

  // @LINE:132
  class ReverseOrderController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:132
    def list: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.OrderController.list",
      """
        function(status0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/orders" + _qS([(""" + implicitly[play.api.mvc.QueryStringBindable[java.util.Optional[String]]].javascriptUnbind + """)("status", status0)])})
        }
      """
    )
  
    // @LINE:133
    def getById: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.OrderController.getById",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/orders/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:134
    def create: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.OrderController.create",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/orders"})
        }
      """
    )
  
    // @LINE:135
    def updateStatus: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.OrderController.updateStatus",
      """
        function(id0) {
          return _wA({method:"PATCH", url:"""" + _prefix + { _defaultPrefix } + """" + "api/orders/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0)) + "/status"})
        }
      """
    )
  
  }


}
