package com.ppdm.app.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @POST("api/logout/{id}")
    suspend fun logout(@Path("id") id: Long): Response<Unit>

    @GET("api/health")
    suspend fun checkHealth(): Response<Unit>

    // Custom GET with Body
    @HTTP(method = "POST", path = "api/users/{id}/projects", hasBody = true)
    suspend fun getUserProjects(
        @Path("id") id: Long,
        @Body request: ProjectPageRequest
    ): Response<ProjectPageResponse>

    @POST("api/projects")
    suspend fun createProject(@Body project: ProjectCreateDto): Response<ProjectDto>

    @PUT("api/projects/{id}")
    suspend fun updateProject(@Path("id") id: Long, @Body project: ProjectUpdateDto): Response<ProjectDto>
}

// --- DTOs ---
data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val token: String,
    val id: Long,
    val username: String
)
data class ProjectPageRequest(val pageNumber: Int, val pageSize: Int, val name: String?, val onlyActive: Boolean?)
data class ProjectPageResponse(val content: List<ProjectDto>)
data class ProjectDto(
    val id: Long,
    val name: String,
    val budget: Long,
    val active: Boolean,
    val officeLatitude: String?,
    val officeLongitude: String?
)
data class ProjectCreateDto(
    val name: String,
    val budget: Long,
    val active: Boolean,
    val officeLatitude: String?,
    val officeLongitude: String?
)
data class ProjectUpdateDto(
    val name: String,
    val budget: Long,
    val active: Boolean,
    val officeLatitude: String?,
    val officeLongitude: String?
)