package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ApiResponse;
import dto.PageResponse;
import dto.UserRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;

import javax.inject.Inject;
import java.util.Set;

/**
 * 用户管理Controller
 */
public class UserManagementController extends Controller {

    private final UserService userService;

    @Inject
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    public Result createUser(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            UserRequest userRequest = Json.fromJson(json, UserRequest.class);
            
            User user = userService.createUser(userRequest);
            return ok(Json.toJson(ApiResponse.success("用户创建成功", user)));
        } catch (ResourceAlreadyExistsException e) {
            return status(409, Json.toJson(ApiResponse.error(409, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, "参数错误: " + e.getMessage())));
        }
    }

    public Result getUserById(Long id) {
        try {
            User user = userService.getUserById(id);
            return ok(Json.toJson(ApiResponse.success(user)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getUserByUsername(String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ok(Json.toJson(ApiResponse.success(user)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getAllUsers(int page, int size) {
        try {
            PageResponse<User> users = userService.getAllUsers(page, size);
            return ok(Json.toJson(ApiResponse.success(users)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result searchUsers(String keyword, int page, int size) {
        try {
            PageResponse<User> users = userService.searchUsers(keyword, page, size);
            return ok(Json.toJson(ApiResponse.success(users)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result getUsersByStatus(Boolean enabled, int page, int size) {
        try {
            PageResponse<User> users = userService.getUsersByEnabled(enabled, page, size);
            return ok(Json.toJson(ApiResponse.success(users)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result updateUser(Http.Request request, Long id) {
        try {
            JsonNode json = request.body().asJson();
            UserRequest userRequest = Json.fromJson(json, UserRequest.class);
            
            User user = userService.updateUser(id, userRequest);
            return ok(Json.toJson(ApiResponse.success("用户更新成功", user)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        } catch (ResourceAlreadyExistsException e) {
            return status(409, Json.toJson(ApiResponse.error(409, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }

    public Result deleteUser(Long id) {
        try {
            userService.deleteUser(id);
            return ok(Json.toJson(ApiResponse.success("用户删除成功", null)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result deleteUsers(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            Set<Long> ids = Json.fromJson(json, Set.class);
            userService.deleteUsers(ids);
            return ok(Json.toJson(ApiResponse.success("批量删除成功", null)));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }
}
