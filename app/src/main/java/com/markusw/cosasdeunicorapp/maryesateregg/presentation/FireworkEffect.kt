package com.markusw.cosasdeunicorapp.maryesateregg.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.map
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

/*
* This code is not from me. The original author is: Mohamed Irsath Kareem
* Github: https://github.com/Mikkareem
* */
private val gravity: Offset = Offset(x = 0f, y = 2f)

private class Particle(
    initialPosition: Offset,
    hu: Float,
    private val isFirework: Boolean = false
) {

    var position: Offset by mutableStateOf(initialPosition)
        private set

    private var lifespan = 255
    private var acceleration = Offset.Zero

    private var velocity: Offset = if(isFirework) {
        Offset(0f, Random.nextFloat(-88f, -58f))
    } else {
        Offset.random2D().times(Random.nextFloat(2f, 15f))
    }

    private val color = HSBColor(hu = hu.roundToInt(), saturation = 255, brightness = 255).toRGBColor()

    fun applyForce(force: Offset) {
        acceleration += force
    }

    fun update() {
        if(!isFirework) {
            velocity *= 0.9f
            lifespan -= 10
        }
        velocity += acceleration
        position += velocity
        acceleration *= 0f
    }

    fun canExplode(): Boolean = velocity.y >= 0

    fun done(): Boolean = lifespan < 0

    fun show(drawScope: DrawScope) {
        drawScope.showParticle()
    }

    private fun DrawScope.showParticle() {
        val alpha = map(lifespan.toFloat(), 0f, 255f, 0f, 1f)
        val drawColor = if(!isFirework) color.copy(alpha = alpha) else color

        drawPoints(
            points = listOf(position),
            pointMode = PointMode.Points,
            color = drawColor,
            strokeWidth = if(!isFirework) 4.dp.toPx() else 10.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

class Firework(
    width: Float,
    height: Float
) {

    private val hu = Random.nextFloat(0f, 255f)
    private val firework = Particle(Offset(Random.nextFloat(0f, width), height), hu, true)

    private var exploded = false
    private var particles = mutableListOf<Particle>()

    fun done(): Boolean = exploded && particles.isEmpty()

    private fun explode() {
        for (i in 1..100) {
            val particle = Particle(firework.position, hu, false)
            particles.add(particle)
        }
    }

    fun update() {
        if(!exploded) {
            firework.applyForce(gravity)
            firework.update()

            if(firework.canExplode()) {
                exploded = true
                explode()
            }
        }

        for (index in particles.indices.reversed()) {
            particles[index].applyForce(gravity)
            particles[index].update()
        }

        if(particles.isNotEmpty()) {
            particles.removeIf { it.done() }
        }
    }

    fun show(drawScope: DrawScope) {
        if(!exploded) {
            firework.show(drawScope)
        }

        for(particle in particles) {
            particle.show(drawScope)
        }
    }
}

@Preview
@Composable
private fun FireworkEffectView() {
    val fireworks = remember { mutableStateListOf<Firework>() }

    var size: IntSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(30)
            fireworks.forEach { it.update() }
        }
    }

    LaunchedEffect(key1 = size) {
        while (true) {
            delay(100)
            fireworks.add(Firework(size.width.toFloat(), size.height.toFloat()))
        }
    }

    Canvas(
        modifier = Modifier
            .onSizeChanged { size = it }
            .fillMaxSize()
            .background(Color.Black)
    ) {
        fireworks.removeIf { it.done() }

        for(firework in fireworks) {
            firework.show(this)
        }
    }
}