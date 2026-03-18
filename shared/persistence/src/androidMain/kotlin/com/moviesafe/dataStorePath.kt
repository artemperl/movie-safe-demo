package com.moviesafe

import android.content.Context
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.context.GlobalContext

internal actual fun dataStorePath(filename: String): Path {
    val context = GlobalContext.get().get<Context>()
    return context.filesDir.resolve("datastore/$filename.preferences_pb").absolutePath.toPath()
}