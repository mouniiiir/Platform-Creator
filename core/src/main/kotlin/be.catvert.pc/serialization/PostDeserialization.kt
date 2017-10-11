package be.catvert.pc.serialization

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * Interface permettant de dire au déséralizer que la classe en question requiert une action après la désérialization
 */
interface PostDeserialization {
    fun postDeserialization()
}

/**
 * Factory permettant d'appeler la postDeserialization lorsqu'une classe en a besoin
 */
class PostDeserializationAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>?): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader): T {
                val obj = delegate.read(`in`)
                if (obj is PostDeserialization) {
                    (obj as PostDeserialization).postDeserialization()
                }
                return obj
            }
        }
    }
}