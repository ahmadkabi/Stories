package ahmadkabi.stories.data.source.remote

import ahmadkabi.stories.data.source.remote.StatusResponse.*

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

