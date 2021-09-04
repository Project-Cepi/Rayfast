package dev.emortal.rayfast.util

import java.util.concurrent.ConcurrentHashMap
import java.lang.IllegalArgumentException
import java.util.function.Function
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmName

/**
 * Used to generate converters of specified types
 *
 * @param <C>
</C> */
class Converter<C> {
    private val convertingFunctions: MutableMap<KClass<*>, Function<*, C>> = ConcurrentHashMap()

    /**
     * Registers a conversion function for a specific class
     *
     * @param clazz the class of the converter function
     * @param converterFunction the converter function
     * @param <T> the class
    </T> */
    fun <T : Any> register(clazz: KClass<T>, converterFunction: Function<T, C>) {
        convertingFunctions[clazz] = converterFunction
    }

    fun <T : Any> register(clazz: Class<T>, converterFunction: Function<T, C>) = register(clazz.kotlin, converterFunction)

    /**
     * Converts an object to a Area3d.
     * <br></br><br></br>
     * The object needs to have a registered converting function. These
     * functions can be registered using #register
     */
    fun <T : Any> from(obj: T): C {
        val originalClazz: KClass<out T> = obj::class
        var clazz: KClass<*>? = originalClazz
        while (clazz != null) {
            val convertingFunction = convertingFunctions[clazz] as Function<Any, C>?
            if (convertingFunction != null) {
                if (clazz != originalClazz) {
                    convertingFunctions[originalClazz] = convertingFunction
                }
                return convertingFunction.apply(obj)
            }
            clazz = clazz.superclasses[0]
        }
        throw IllegalArgumentException(originalClazz.simpleName ?: originalClazz.jvmName + "object provided to Converter#from did not have a registered converter")
    }
}