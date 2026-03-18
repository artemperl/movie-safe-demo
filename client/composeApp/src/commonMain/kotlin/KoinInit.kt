import com.moviesafe.searchModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(): KoinApplication {
    return startKoin {
        modules(searchModule)
    }
}
