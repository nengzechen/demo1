package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ApiResponse;
import dto.PageResponse;
import dto.PermissionRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Permission;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.PermissionService;

import javax.inject.Inject;
import java.util.Set;

/**
 * 权限管理Controller
 */
public class PermissionManagementController extends Controller {

    private final PermissionService permissionService;

    @Inject
    public PermissionManagementController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public Result createPermission(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            PermissionRequest permissionRequest = Json.fromJson(json, PermissionRequest.class);
            
            Permission permission = permissionService.createPermission(permissionRequest);
            return ok(Json.toJson(ApiResponse.success("权限创建成功", permission)));
        } catch (ResourceAlreadyExistsException e) {
            return status(409, Json.toJson(ApiResponse.error(409, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }

    public Result getPermissionById(Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            return ok(Json.toJson(ApiResponse.success(permission)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getPermissionByCode(String code) {
        try {
            Permission permission = permissionService.getPermissionByCode(code);
            return ok(Json.toJson(ApiResponse.success(permission)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result getAllPermissions(int page, int size) {
        try {
            PageResponse<Permission> permissions = permissionService.getAllPermissions(page, size);
            return ok(Json.toJson(ApiResponse.success(permissions)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result searchPermissions(String keyword, int page, int size) {
        try {
            PageResponse<Permission> permissions = permissionService.searchPermissions(keyword, page, size);
            return ok(Json.toJson(ApiResponse.success(permissions)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result getPermissionsByResource(String resource, int page, int size) {
        try {
            PageResponse<Permission> permissions = permissionService.getPermissionsByResource(resource, page, size);
            return ok(Json.toJson(ApiResponse.success(permissions)));
        } catch (Exception e) {
            return internalServerError(Json.toJson(ApiResponse.error(e.getMessage())));
        }
    }

    public Result updatePermission(Http.Request request, Long id) {
        try {
            JsonNode json = request.body().asJson();
            PermissionRequest permissionRequest = Json.fromJson(json, PermissionRequest.class);
            
            Permission permission = permissionService.updatePermission(id, permissionRequest);
            return ok(Json.toJson(ApiResponse.success("权限更新成功", permission)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }

    public Result deletePermission(Long id) {
        try {
            permissionService.deletePermission(id);
            return ok(Json.toJson(ApiResponse.success("权限删除成功", null)));
        } catch (ResourceNotFoundException e) {
            return notFound(Json.toJson(ApiResponse.error(404, e.getMessage())));
        }
    }

    public Result deletePermissions(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            Set<Long> ids = Json.fromJson(json, Set.class);
            permissionService.deletePermissions(ids);
            return ok(Json.toJson(ApiResponse.success("批量删除成功", null)));
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.error(400, e.getMessage())));
        }
    }
}
