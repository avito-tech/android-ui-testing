package com.avito.android.test.stub

import com.avito.android.test.stub.FindStubResolution.NotFound
import com.avito.android.test.stub.FindStubResolution.OK
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.File

data class TestCoordinates(val testName: String)

sealed class FindStubResolution {
    class OK(val response: Response) : FindStubResolution()
    class NotFound(val exception: Exception) : FindStubResolution()
}

sealed class RequestCategory {
    class Avito : RequestCategory()
    class External : RequestCategory()
}

sealed class RecordMode {
    data class Enabled(val overwrite: Boolean) : RecordMode()
    object Disabled : RecordMode()
}

class NetworkStubInterceptor(
    private val recordMode: RecordMode,
    private val test: TestCoordinates
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        when (requestCategory(request)) {
            is RequestCategory.Avito -> {
                val response: Response = if (recordMode is RecordMode.Enabled) {
                    recordStub(recordMode, test, request, chain.proceed(request))
                } else {
                    val resolution = findStubFor(test, request)
                    when (resolution) {
                        is OK -> resolution.response
                        is NotFound -> error("Stub not found for: $request")
                    }
                }
                return response
            }
            is RequestCategory.External -> {
                return chain.proceed(request)
            }
        }
    }

    private fun requestCategory(request: Request): RequestCategory {
        return if (request.url().host().contains("avito.ru")) {
            RequestCategory.Avito()
        } else {
            RequestCategory.External()
        }
    }

    private fun stubCoordinates(testCoordinates: TestCoordinates, request: Request): File {
        TODO()
    }

    private fun recordStub(
        recordMode: RecordMode.Enabled,
        test: TestCoordinates,
        request: Request,
        response: Response
    ): Response {
        val stubFile = stubCoordinates(test, request)
        return if (stubFile.exists()) {
            if (recordMode.overwrite) {
                writeStub(stubFile, response)
            } else {
                readStub(stubFile)
            }
        } else {
            stubFile.run {
                mkdirs()
                createNewFile()
                writeStub(this, response)
            }
        }
    }

    private fun writeStub(stub: File, response: Response): Response {
        TODO()
    }

    private fun readStub(stub: File): Response {
        TODO()
    }

    private fun findStubFor(test: TestCoordinates, request: Request): FindStubResolution {
        TODO()
    }
}