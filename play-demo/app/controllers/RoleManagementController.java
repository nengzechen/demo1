package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ApiResponse;
import dto.PageResponse;
import dto.RoleRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Role;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.RoleService;

import javax.inject.Inject;
import java.util.Set;

/**
 * 角色管理Controller
 */
public class RoleManagementController extends Controller {

    private final RoleService roleService;

    @Inject
    public RoleManagementController(RoleService roleService) {
        this.roleService = roleService;
    }

    public Result createRole(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            RoleRequest roleRequest = Json.fromJson(json, RoleRequest.class);
            
            Role role = roleService.createRole(roleRequest);
            return ok(Json.toJson(ApiResponse.success("角色创建成功", role)));
        } catch (ResourceAlreadyExistsException e) {
            return status(409, Json.toJson(ApiResponse.error(409, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }

    public Result getRoleById(Long id) {
        try {
            Role role = roleService.getRoleById(id);
            return ok(Json.toJson(ApiResponse.success(role)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getRoleByCode(String code) {
        try {
            Role role = roleService.getRoleByCode(code);
            return ok(Json.toJson(ApiResponse.success(role)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getAllRoles(int page, int size) {
        try {
            PageResponse<Role> roles = roleService.getAllRoles(page, size);
            return ok(Json.toJson(ApiResponse.success(roles)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result searchRoles(String keyword, int page, int size) {
        try {
            PageResponse<Role> roles = roleService.searchRoles(keyword, page, size);
            return ok(Json.toJson(ApiResponse.success(roles)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result updateRole(Http.Request request, Long id) {
        try {
            JsonNode json = request.body().asJson();
            RoleRequest roleRequest = Json.fromJson(json, RoleRequest.class);
            
            Role role = roleService.updateRole(id, roleRequest);
            return ok(Json.toJson(ApiResponse.success("角色更新成功", role)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        } catch (ResourceAlreadyExistsException e) {
            return status(409, Json.toJson(ApiResponse.error(409, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }

    public Result deleteRole(Long id) {
        try {
            roleService.deleteRole(id);
            return ok(Json.toJson(ApiResponse.success("角色删除成功", null)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result deleteRoles(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            Set<Long> ids = Json.fromJson(json, Set.class);
            roleService.deleteRoles(ids);
            return ok(Json.toJson(ApiResponse.success("批量删除成功", null)));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }
}
