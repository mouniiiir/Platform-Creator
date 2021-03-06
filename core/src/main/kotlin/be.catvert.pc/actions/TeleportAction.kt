package be.catvert.pc.actions

import be.catvert.pc.GameObject
import be.catvert.pc.utility.ExposeEditor
import be.catvert.pc.utility.Point
import com.fasterxml.jackson.annotation.JsonCreator

/**
 * Action permettant de téléporter un gameObject à un point précis
 */

//TODO maxInt
class TeleportAction(@ExposeEditor var teleportPoint: Point) : Action {
    @JsonCreator private constructor(): this(Point())

    override fun invoke(gameObject: GameObject) {
        gameObject.rectangle.position = teleportPoint
    }
}