package ahmadkabi.storyapp.data.source.remote

import ahmadkabi.storyapp.data.source.remote.StatusResponse.*

class ApiResponse<T>(val status: StatusResponse, val body: T? = null, val message: String? = null) {
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

