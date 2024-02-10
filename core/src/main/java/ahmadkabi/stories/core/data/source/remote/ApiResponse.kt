package ahmadkabi.stories.core.data.source.remote

import source.remote.StatusResponse
import source.remote.StatusResponse.*

class ApiResponse<T>(val status: StatusResponse, val body: T? = null) {
    companion object {

        fun <T> success(body: T?): ApiResponse<T> {
            return ApiResponse(SUCCESS, body)
        }

        fun <T> empty(): ApiResponse<T> {
            return ApiResponse(EMPTY)
        }

        fun <T> error(): ApiResponse<T> {
            return ApiResponse(ERROR)
        }
    }

}

