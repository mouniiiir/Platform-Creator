package be.catvert.pc.scenes

import be.catvert.pc.GameKeys
import be.catvert.pc.GameObject
import be.catvert.pc.Level
import be.catvert.pc.PCGame
import be.catvert.pc.components.graphics.AtlasComponent
import be.catvert.pc.serialization.SerializationFactory
import be.catvert.pc.utility.Point
import be.catvert.pc.utility.contains
import be.catvert.pc.utility.setPosition
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import ktx.assets.loadOnDemand

/**
 * Scène de l'éditeur de niveau
 */
class EditorScene(private val level: Level) : Scene() {
    private val cameraMoveSpeed = 10f

    private var selectGameObject: GameObject? = null

    init {
        allowUpdating = false

        addContainer(level)

        if (level.backgroundPath != null)
            backgroundTexture = PCGame.assetManager.loadOnDemand<Texture>(Gdx.files.local(level.backgroundPath).path()).asset
    }

    override fun update() {
        super.update()

        if (Gdx.input.isKeyPressed(GameKeys.EDITOR_CAMERA_LEFT.key))
            camera.translate(cameraMoveSpeed, 0f)
        if (Gdx.input.isKeyPressed(GameKeys.EDITOR_CAMERA_RIGHT.key))
            camera.translate(-cameraMoveSpeed, 0f)
        if (Gdx.input.isKeyPressed(GameKeys.EDITOR_CAMERA_UP.key))
            camera.translate(0f, -cameraMoveSpeed)
        if (Gdx.input.isKeyPressed(GameKeys.EDITOR_CAMERA_DOWN.key))
            camera.translate(0f, cameraMoveSpeed)

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            val mousePosVec3 = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val mousePos = Point(mousePosVec3.x.toInt(), mousePosVec3.y.toInt())
            if (selectGameObject != null)
                selectGameObject!!.rectangle.setPosition(Point(mousePos.x - selectGameObject!!.size().width / 2, mousePos.y - selectGameObject!!.size().height / 2))
            else {
                gameObjects.forEach {
                    if (it.rectangle.contains(mousePos)) {
                        selectGameObject = it
                    }
                }
            }
        } else {
            selectGameObject = null
        }

        camera.update()

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            this += level.createGameObject(Rectangle(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat(), 50f, 50f)) {
                this += AtlasComponent(Gdx.files.local("assets/atlas/Abstract Plateform/spritesheet_complete.atlas"), "blockBrown")
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SerializationFactory.serializeToFile(level, level.levelPath)
            PCGame.setScene(MainMenuScene())
        }
    }
}